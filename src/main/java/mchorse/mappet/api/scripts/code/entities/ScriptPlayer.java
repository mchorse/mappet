package mchorse.mappet.api.scripts.code.entities;

import io.netty.buffer.Unpooled;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.huds.HUDScene;
import mchorse.mappet.api.scripts.code.items.ScriptInventory;
import mchorse.mappet.api.scripts.code.mappet.MappetQuests;
import mchorse.mappet.api.scripts.code.mappet.MappetUIBuilder;
import mchorse.mappet.api.scripts.code.mappet.MappetUIContext;
import mchorse.mappet.api.scripts.user.entities.IScriptPlayer;
import mchorse.mappet.api.scripts.user.items.IScriptInventory;
import mchorse.mappet.api.scripts.user.mappet.IMappetQuests;
import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.scripts.user.mappet.IMappetUIContext;
import mchorse.mappet.api.scripts.user.nbt.INBT;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.mappet.api.ui.UI;
import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.huds.PacketHUDMorph;
import mchorse.mappet.network.common.huds.PacketHUDScene;
import mchorse.mappet.network.common.scripts.PacketEntityRotations;
import mchorse.mappet.network.common.scripts.PacketSound;
import mchorse.mappet.network.common.scripts.PacketStopSound;
import mchorse.mappet.network.common.ui.PacketCloseUI;
import mchorse.mappet.network.common.ui.PacketUI;
import mchorse.mappet.utils.WorldUtils;
import mchorse.metamorph.api.MorphAPI;
import mchorse.metamorph.api.MorphUtils;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.capabilities.morphing.IMorphing;
import mchorse.metamorph.capabilities.morphing.Morphing;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;

public class ScriptPlayer extends ScriptEntity<EntityPlayerMP> implements IScriptPlayer
{
    private IMappetQuests quests;
    private IScriptInventory inventory;
    private IScriptInventory enderChest;

    public ScriptPlayer(EntityPlayerMP entity)
    {
        super(entity);
    }

    @Override
    public EntityPlayerMP getMinecraftPlayer()
    {
        return this.entity;
    }

    @Override
    public void setMotion(double x, double y, double z)
    {
        super.setMotion(x, y, z);

        this.entity.connection.sendPacket(new SPacketEntityVelocity(this.entity.getEntityId(), x, y, z));
    }

    @Override
    public void setRotations(float pitch, float yaw, float yawHead)
    {
        super.setRotations(pitch, yaw, yawHead);

        Dispatcher.sendTo(new PacketEntityRotations(this.entity.getEntityId(), yaw, yawHead, pitch), this.entity);
    }

    @Override
    public void swingArm(int arm)
    {
        super.swingArm(arm);

        this.entity.connection.sendPacket(new SPacketAnimation(this.entity, arm == 1 ? 3 : 0));
    }

    /* Player's methods */

    @Override
    public int getGameMode()
    {
        return this.entity.interactionManager.getGameType().getID();
    }

    @Override
    public void setGameMode(int gameMode)
    {
        GameType type = GameType.getByID(gameMode);

        if (type.getID() >= 0)
        {
            this.entity.setGameType(type);
        }
    }

    @Override
    public IScriptInventory getInventory()
    {
        if (this.inventory == null)
        {
            this.inventory = new ScriptInventory(this.entity.inventory);
        }

        return this.inventory;
    }

    @Override
    public IScriptInventory getEnderChest()
    {
        if (this.enderChest == null)
        {
            this.enderChest = new ScriptInventory(this.entity.getInventoryEnderChest());
        }

        return this.enderChest;
    }

    @Override
    public void send(Object message)
    {
        this.entity.sendMessage(new TextComponentString(message.toString()));
    }

    @Override
    public void sendRaw(INBT message)
    {
        ITextComponent component = ITextComponent.Serializer.fromJsonLenient(message.stringify());

        if (component != null)
        {
            this.entity.sendMessage(component);
        }
    }

    @Override
    public String getSkin()
    {
        ICharacter character = Character.get(this.entity);
        String skin = character.getSkin();

        return skin == null ? "" : skin;
    }

    @Override
    public void sendTitleDurations(int fadeIn, int idle, int fadeOut)
    {
        SPacketTitle packet = new SPacketTitle(fadeIn, idle, fadeOut);

        this.getMinecraftPlayer().connection.sendPacket(packet);
    }

    @Override
    public void sendTitle(String title)
    {
        SPacketTitle packet = new SPacketTitle(SPacketTitle.Type.TITLE, new TextComponentString(title));

        this.getMinecraftPlayer().connection.sendPacket(packet);
    }

    @Override
    public void sendSubtitle(String title)
    {
        SPacketTitle packet = new SPacketTitle(SPacketTitle.Type.SUBTITLE, new TextComponentString(title));

        this.getMinecraftPlayer().connection.sendPacket(packet);
    }

    @Override
    public void sendActionBar(String title)
    {
        SPacketTitle packet = new SPacketTitle(SPacketTitle.Type.ACTIONBAR, new TextComponentString(title));

        this.getMinecraftPlayer().connection.sendPacket(packet);
    }

    /* XP methods */

    @Override
    public void setXp(int level, int points)
    {
        this.entity.addExperienceLevel(-this.getXpLevel() - 1);
        this.entity.addExperienceLevel(level);
        this.entity.addExperience(points);
    }

    @Override
    public void addXp(int points)
    {
        this.entity.addExperience(points);
    }

    @Override
    public int getXpLevel()
    {
        return this.entity.experienceLevel;
    }

    @Override
    public int getXpPoints()
    {
        return (int) (this.entity.experience * this.entity.xpBarCap());
    }

    @Override
    public void setHunger(int value)
    {
        this.entity.getFoodStats().setFoodLevel(value);
    }

    @Override
    public int getHunger()
    {
        return this.entity.getFoodStats().getFoodLevel();
    }

    @Override
    public void setSaturation(float value)
    {
        this.entity.getFoodStats().setFoodSaturationLevel(value);
    }

    @Override
    public float getSaturation()
    {
        return this.entity.getFoodStats().getSaturationLevel();
    }

    /* Sounds */

    @Override
    public void playSound(String event, double x, double y, double z, float volume, float pitch)
    {
        WorldUtils.playSound(this.entity, event, x, y, z, volume, pitch);
    }

    @Override
    public void playSound(String event, String soundCategory, double x, double y, double z, float volume, float pitch)
    {
        WorldUtils.playSound(this.entity, event, soundCategory, x, y, z, volume, pitch);
    }

    @Override
    public void playSound(String event, String soundCategory, double x, double y, double z)
    {
        WorldUtils.playSound(this.entity, event, soundCategory, x, y, z, 1F, 1F);
    }

    @Override
    public void stopSound(String event, String category)
    {
        PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());

        packetbuffer.writeString(category);
        packetbuffer.writeString(event);

        this.entity.connection.sendPacket(new SPacketCustomPayload("MC|StopSound", packetbuffer));
    }

    @Override
    public void playStaticSound(String event, float volume, float pitch)
    {
        this.playStaticSound(event, "master", volume, pitch);
    }

    @Override
    public void playStaticSound(String event, String soundCategory, float volume, float pitch)
    {
        Dispatcher.sendTo(new PacketSound(event, soundCategory, volume, pitch), this.entity);
    }

    @Override
    public void stopSound(String event, String soundCategory)
    {
        Dispatcher.sendTo(new PacketStopSound(event, soundCategory), this.entity);
    }

    /* Mappet stuff */

    @Override
    public IMappetQuests getQuests()
    {
        if (this.quests == null)
        {
            this.quests = new MappetQuests(Character.get(this.entity).getQuests(), this.entity);
        }

        return this.quests;
    }

    @Override
    public AbstractMorph getMorph()
    {
        IMorphing cap = Morphing.get(this.entity);

        if (cap != null)
        {
            return cap.getCurrentMorph();
        }

        return super.getMorph();
    }

    @Override
    public boolean setMorph(AbstractMorph morph)
    {
        if (morph == null)
        {
            MorphAPI.demorph(this.entity);
        }
        else
        {
            MorphAPI.morph(this.entity, morph, true);
        }

        return true;
    }

    @Override
    public boolean openUI(IMappetUIBuilder in, boolean defaultData)
    {
        if (!(in instanceof MappetUIBuilder))
        {
            return false;
        }

        MappetUIBuilder builder = (MappetUIBuilder) in;

        ICharacter character = Character.get(this.entity);

        if (character.getUIContext() == null)
        {
            UI ui = builder.getUI();
            UIContext context;
            if (builder.getFunctionObject() == null || builder.getFunctionObject().isUndefined())
                context = new UIContext(ui, this.entity, builder.getScript(), builder.getFunction());
            else
                context = new UIContext(ui, this.entity, builder.getFunctionObject());

            character.setUIContext(context);
            Dispatcher.sendTo(new PacketUI(ui), this.getMinecraftPlayer());

            if (defaultData)
            {
                context.populateDefaultData();
            }

            context.clearChanges();

            return true;
        }

        return false;
    }

    @Override
    public void closeUI()
    {
        Dispatcher.sendTo(new PacketCloseUI(), this.getMinecraftPlayer());
    }

    @Override
    public IMappetUIContext getUIContext()
    {
        ICharacter character = Character.get(this.entity);
        UIContext context = character.getUIContext();

        return context == null ? null : new MappetUIContext(context);
    }

    /* HUD scenes API */

    @Override
    public boolean setupHUD(String id)
    {
        HUDScene scene = Mappet.huds.load(id);

        if (scene != null)
        {
            Dispatcher.sendTo(new PacketHUDScene(scene.getId(), scene.serializeNBT()), this.entity);
        }

        return scene != null;
    }

    @Override
    public void changeHUDMorph(String id, int index, AbstractMorph morph)
    {
        if (morph == null)
        {
            return;
        }

        this.changeHUDMorph(id, index, MorphUtils.toNBT(morph));
    }

    @Override
    public void changeHUDMorph(String id, int index, INBTCompound morph)
    {
        if (morph == null)
        {
            return;
        }

        this.changeHUDMorph(id, index, morph.getNBTTagCompound());
    }

    private void changeHUDMorph(String id, int index, NBTTagCompound tag)
    {
        Dispatcher.sendTo(new PacketHUDMorph(id, index, tag), this.entity);
    }

    @Override
    public void closeHUD(String id)
    {
        Dispatcher.sendTo(new PacketHUDScene(id == null ? "" : id, null), this.entity);
    }
}