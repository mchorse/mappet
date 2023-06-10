package mchorse.mappet.commands.huds;

import mchorse.mappet.capabilities.character.Character;
import mchorse.mclib.commands.SubCommandBase;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class CommandHudMorph extends CommandHudBase
{
    @Override
    public String getName()
    {
        return "morph";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.hud.morph";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}hud morph{r} {7}<target> <id> <index> <nbt>{r}";
    }

    @Override
    public int getRequiredArgs()
    {
        return 4;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP player = getPlayer(server, sender, args[0]);
        String scene = args[1];
        int index = CommandBase.parseInt(args[2]);
        NBTTagCompound tag = null;

        try
        {
            tag = JsonToNBT.getTagFromJson(String.join(" ", SubCommandBase.dropFirstArguments(args, 3)));
        }
        catch (Exception e)
        {
        }

        Character.get(player).changeHUDMorph(scene, index, tag);
    }
}