package mchorse.mappet.api.scripts.code.entities;

import io.netty.buffer.Unpooled;
import mchorse.aperture.network.common.PacketCameraState;
import mchorse.mappet.api.scripts.code.items.ScriptInventory;
import mchorse.mappet.api.scripts.code.mappet.MappetQuests;
import mchorse.mappet.api.scripts.code.mappet.MappetUIBuilder;
import mchorse.mappet.api.scripts.code.mappet.MappetUIContext;
import mchorse.mappet.api.scripts.code.nbt.ScriptNBTCompound;
import mchorse.mappet.api.scripts.user.data.ScriptVector;
import mchorse.mappet.api.scripts.user.entities.IScriptPlayer;
import mchorse.mappet.api.scripts.user.items.IScriptInventory;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
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
import mchorse.mappet.network.common.scripts.PacketEntityRotations;
import mchorse.mappet.network.common.scripts.PacketSound;
import mchorse.mappet.network.common.ui.PacketCloseUI;
import mchorse.mappet.network.common.ui.PacketUI;
import mchorse.mappet.utils.WorldUtils;
import mchorse.metamorph.api.MorphAPI;
import mchorse.metamorph.api.MorphUtils;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.capabilities.morphing.IMorphing;
import mchorse.metamorph.capabilities.morphing.Morphing;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

import java.util.HashSet;
import java.util.Set;

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
    public void executeCommand(String command)
    {
        this.entity.world.getMinecraftServer().getCommandManager().executeCommand(this.entity, command);
    }

    @Override
    public void setSpawnPoint(double x, double y, double z)
    {
        this.entity.setSpawnPoint(new BlockPos(x, y, z), true);
    }

    @Override
    public ScriptVector getSpawnPoint()
    {
        BlockPos pos = this.entity.getBedLocation(this.entity.dimension);

        if (pos == null)
        {
            pos = this.entity.world.getSpawnPoint();
        }

        return new ScriptVector(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public boolean isFlying()
    {
        return this.entity.capabilities.isFlying;
    }

    @Override
    public float getWalkSpeed()
    {
        return this.entity.capabilities.getWalkSpeed();
    }

    @Override
    public float getFlySpeed()
    {
        return this.entity.capabilities.getFlySpeed();
    }

    @Override
    public void setFlySpeed(float speed)
    {
        this.entity.capabilities.setFlySpeed(speed);
        this.entity.sendPlayerAbilities();
    }

    @Override
    public void setWalkSpeed(float speed)
    {
        this.entity.capabilities.setPlayerWalkSpeed(speed);
        this.entity.sendPlayerAbilities();
    }

    @Override
    public float getCooldown(IScriptItemStack item)
    {
        return this.entity.getCooldownTracker().getCooldown(item.getMinecraftItemStack().getItem(), 0);
    }

    @Override
    public void setCooldown(IScriptItemStack item, int ticks)
    {
        this.entity.getCooldownTracker().setCooldown(item.getMinecraftItemStack().getItem(), ticks);
    }

    @Override
    public void resetCooldown(IScriptItemStack item)
    {
        this.entity.getCooldownTracker().removeCooldown(item.getMinecraftItemStack().getItem());
    }

    @Override
    public int getHotbarIndex()
    {
        return this.entity.inventory.currentItem;
    }

    @Override
    public void setHotbarIndex(int slot)
    {
        if (slot < 0 || slot >= 9)
        {
            return;
        }

        this.entity.inventory.currentItem = slot;

        this.entity.connection.sendPacket(new SPacketHeldItemChange(slot));
    }

    @Override
    public boolean giveItem(IScriptItemStack itemStack)
    {
        return this.giveItem(itemStack, true);
    }

    public boolean giveItem(IScriptItemStack itemStack, boolean playSound)
    {
        ItemStack minecraftItemStack = itemStack.getMinecraftItemStack();

        if (minecraftItemStack.isEmpty())
        {
            return false;
        }

        boolean result = this.entity.inventory.addItemStackToInventory(minecraftItemStack.copy());

        if (result)
        {
            if (playSound)
            {
                float pitch = ((this.entity.getRNG().nextFloat() - this.entity.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F;
                this.entity.world.playSound(null, this.entity.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 0.2F, pitch);
            }

            this.entity.inventoryContainer.detectAndSendChanges();
        }

        return result;
    }

    @Override
    public void send(String message)
    {
        this.entity.sendMessage(new TextComponentString(message));
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
        return "mp.skins:" + this.getName();
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
        boolean noContext = character.getUIContext() == null;

        if (!noContext)
        {
            character.getUIContext().close();
        }

        UI ui = builder.getUI();
        UIContext context = new UIContext(ui, this.entity, builder.getScript(), builder.getFunction());

        character.setUIContext(context);
        Dispatcher.sendTo(new PacketUI(ui), this.getMinecraftPlayer());

        if (defaultData)
        {
            context.populateDefaultData();
        }

        context.clearChanges();

        return noContext;
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

    @Override
    public Set<String> getFactions()
    {
        Set<String> factions = new HashSet<>();

        ICharacter character = Character.get(entity);
        if (character != null)
        {
            factions = character.getStates().getFactionNames();
        }

        return factions;
    }


    /* HUD scenes API */

    @Override
    public boolean setupHUD(String id)
    {
        return Character.get(this.entity).setupHUD(id, true);
    }

    @Override
    public void changeHUDMorph(String id, int index, AbstractMorph morph)
    {
        if (morph == null)
        {
            return;
        }

        Character.get(this.entity).changeHUDMorph(id, index, MorphUtils.toNBT(morph));
    }

    @Override
    public void changeHUDMorph(String id, int index, INBTCompound morph)
    {
        if (morph == null)
        {
            return;
        }

        Character.get(this.entity).changeHUDMorph(id, index, morph.getNBTTagCompound());
    }

    @Override
    public void closeHUD(String id)
    {
        Character.get(this.entity).closeHUD(id);
    }

    @Override
    public void closeAllHUD()
    {
        Character.get(this.entity).closeAllHUD();
    }

    @Override
    public INBTCompound getDisplayedHUDs()
    {
        ICharacter character = Character.get(this.entity);
        NBTTagCompound tag = ((Character) character).getDisplayedHUDsTag();
        return new ScriptNBTCompound(tag);
    }

    @Override
    public INBTCompound getGlobalDisplayedHUDs()
    {
        ICharacter character = Character.get(this.entity);
        NBTTagCompound tag = ((Character) character).getGlobalDisplayedHUDsTag();
        return new ScriptNBTCompound(tag);
    }

    /* Aperture API */

    @Override
    public void playScene(String sceneName)
    {
        if (Loader.isModLoaded("aperture"))
        {
            this.playApertureScene(sceneName, true);
        }
    }

    @Override
    public void stopScene()
    {
        if (Loader.isModLoaded("aperture"))
        {
            this.playApertureScene("", false);
        }
    }

    @Optional.Method(modid = "aperture")
    private void playApertureScene(String sceneName, boolean toPlay)
    {
        mchorse.aperture.network.Dispatcher.sendTo(new PacketCameraState(sceneName, toPlay), this.entity);
    }
}