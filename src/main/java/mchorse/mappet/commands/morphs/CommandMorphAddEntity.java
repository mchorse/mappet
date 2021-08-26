package mchorse.mappet.commands.morphs;

import joptsimple.internal.Strings;
import mchorse.mappet.client.morphs.WorldMorph;
import mchorse.mappet.commands.MappetCommandBase;
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

public class CommandMorphAddEntity extends MappetCommandBase
{
    @Override
    public String getName()
    {
        return "entity";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.morph.add.entity";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}morph add{r} {7}<target> <expiration> <rotate> <x> <y> <z> <yaw> <pitch> <morph>{r}";
    }

    @Override
    public int getRequiredArgs()
    {
        return 9;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        Entity entity = CommandBase.getEntity(server, sender, args[0]);
        int expiration = CommandBase.parseInt(args[1]);
        boolean rotate = CommandBase.parseBoolean(args[2]);
        double x = CommandBase.parseDouble(args[3]);
        double y = CommandBase.parseDouble(args[4]);
        double z = CommandBase.parseDouble(args[5]);
        float yaw = (float) CommandBase.parseDouble(args[6]);
        float pitch = (float) CommandBase.parseDouble(args[7]);
        String nbt = Strings.join(SubCommandBase.dropFirstArguments(args, 8), " ");
        AbstractMorph morph;

        try
        {
            morph = MorphManager.INSTANCE.morphFromNBT(JsonToNBT.getTagFromJson(nbt));
        }
        catch (Exception e)
        {
            throw new CommandException("morph.nbt");
        }

        if (morph == null)
        {
            throw new CommandException("morph.unrecognized", nbt);
        }

        WorldMorph worldMorph = new WorldMorph();

        worldMorph.expiration = expiration;
        worldMorph.rotate = rotate;
        worldMorph.x = x;
        worldMorph.y = y;
        worldMorph.z = z;
        worldMorph.yaw = yaw;
        worldMorph.pitch = pitch;
        worldMorph.morph = morph;
        worldMorph.entity = entity;

        PacketWorldMorph message = new PacketWorldMorph(worldMorph);

        Dispatcher.sendToTracked(entity, message);

        if (entity instanceof EntityPlayerMP)
        {
            Dispatcher.sendTo(message, (EntityPlayerMP) entity);
        }
    }
}