package mchorse.mappet.api.scripts.code.entities;

import mchorse.mappet.api.scripts.code.items.ScriptInventory;
import mchorse.mappet.api.scripts.code.mappet.MappetQuests;
import mchorse.mappet.api.scripts.code.mappet.MappetUIBuilder;
import mchorse.mappet.api.scripts.user.entities.IScriptPlayer;
import mchorse.mappet.api.scripts.user.items.IScriptInventory;
import mchorse.mappet.api.scripts.user.mappet.IMappetQuests;
import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.ui.PacketUI;
import mchorse.metamorph.api.MorphAPI;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityVelocity;
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

        this.entity.connection.setPlayerLocation(this.entity.posX, this.entity.posY, this.entity.posZ, yaw, pitch);
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
    public void send(String message)
    {
        this.entity.sendMessage(new TextComponentString(message));
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
    public boolean openUI(IMappetUIBuilder in)
    {
        if (!(in instanceof MappetUIBuilder))
        {
            return false;
        }

        MappetUIBuilder builder = (MappetUIBuilder) in;

        ICharacter character = Character.get(this.entity);

        if (character.getUIContext() == null)
        {
            UIContext context = new UIContext(this.entity, builder.getScript(), builder.getFunction());

            character.setUIContext(context);
            Dispatcher.sendTo(new PacketUI(builder.getUI()), this.getMinecraftPlayer());

            return true;
        }

        return false;
    }
}