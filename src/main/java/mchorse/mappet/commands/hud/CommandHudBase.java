package mchorse.mappet.commands.hud;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.hud.HUDScene;
import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.commands.MappetCommandBase;
import net.minecraft.command.CommandException;

public abstract class CommandHudBase extends MappetCommandBase
{
    protected HUDScene getHud(String id) throws CommandException
    {
        HUDScene scene = Mappet.huds.load(id);

        if (scene == null)
        {
            throw new CommandException("hud.missing", id);
        }

        return scene;
    }

    @Override
    public int getRequiredArgs()
    {
        return 2;
    }
}