package mchorse.mappet.api.scripts.code.entities;

import mchorse.mappet.api.scripts.code.items.ScriptInventory;
import mchorse.mappet.api.scripts.code.mappet.MappetQuests;
import mchorse.mappet.api.scripts.user.entities.IScriptPlayer;
import mchorse.mappet.api.scripts.user.items.IScriptInventory;
import mchorse.mappet.api.scripts.user.mappet.IMappetQuests;
import mchorse.mappet.capabilities.character.Character;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

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
    public boolean send(String message)
    {
        this.entity.sendMessage(new TextComponentString(message));

        return this.isPlayer();
    }

    /* Mappet stuff */

    @Override
    public IMappetQuests getQuests()
    {
        if (this.quests == null && this.isPlayer())
        {
            this.quests = new MappetQuests(Character.get(this.entity).getQuests(), this.entity);
        }

        return this.quests;
    }
}