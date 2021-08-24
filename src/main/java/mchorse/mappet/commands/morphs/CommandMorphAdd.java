package mchorse.mappet.commands.morphs;

import joptsimple.internal.Strings;
import mchorse.mappet.client.morphs.WorldMorph;
import mchorse.mappet.commands.huds.CommandHudBase;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.scripts.PacketWorldMorph;
import mchorse.mclib.commands.SubCommandBase;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommandMorphAdd extends CommandHudBase
{
    @Override
    public String getName()
    {
        return "add";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.morph.add";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}morph add{r} {7}<target> <rotate> <expiration> <x> <y> <z> <morph>{r}";
    }

    @Override
    public int getRequiredArgs()
    {
        return 7;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        Entity entity = args[0].equals("~") ? null : CommandBase.getEntity(server, sender, args[0]);
        int expiration = CommandBase.parseInt(args[1]);
        boolean rotate = CommandBase.parseBoolean(args[2]);
        double x = CommandBase.parseDouble(args[3]);
        double y = CommandBase.parseDouble(args[4]);
        double z = CommandBase.parseDouble(args[5]);
        AbstractMorph morph;

        try
        {
            String nbt = Strings.join(SubCommandBase.dropFirstArguments(args, 6), " ");

            morph = MorphManager.INSTANCE.morphFromNBT(JsonToNBT.getTagFromJson(nbt));
        }
        catch (Exception e)
        {
            throw new CommandException("morph.nbt");
        }

        if (morph == null)
        {
            return;
        }

        WorldMorph worldMorph = new WorldMorph();

        worldMorph.expiration = expiration;
        worldMorph.rotate = rotate;
        worldMorph.x = x;
        worldMorph.y = y;
        worldMorph.z = z;
        worldMorph.morph = morph;
        worldMorph.entity = entity;

        PacketWorldMorph message = new PacketWorldMorph(worldMorph);

        if (entity == null)
        {
            int dimension = sender.getEntityWorld().provider.getDimension();
            NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(dimension, x, y, z, 64);

            Dispatcher.DISPATCHER.get().sendToAllAround(message, point);
        }
        else
        {
            Dispatcher.sendToTracked(entity, message);

            if (entity instanceof EntityPlayerMP)
            {
                Dispatcher.sendTo(message, (EntityPlayerMP) entity);
            }
        }
    }
}