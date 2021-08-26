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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommandMorphAddWorld extends MappetCommandBase
{
    @Override
    public String getName()
    {
        return "world";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.morph.add.world";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}morph add world{r} {7}<expiration> <x> <y> <z> <yaw> <pitch> <morph>{r}";
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
        Vec3d pos = sender.getPositionVector();
        double baseYaw = 0;
        double basePitch = 0;

        if (sender instanceof EntityLivingBase)
        {
            EntityLivingBase entity = (EntityLivingBase) sender;

            baseYaw = entity.rotationYaw;
            basePitch = entity.rotationPitch;
        }

        int expiration = CommandBase.parseInt(args[0]);
        double x = CommandBase.parseDouble(pos.x, args[1], false);
        double y = CommandBase.parseDouble(pos.y, args[2], false);
        double z = CommandBase.parseDouble(pos.z, args[3], false);
        float yaw = (float) CommandBase.parseDouble(baseYaw, args[4], false);
        float pitch = (float) CommandBase.parseDouble(basePitch, args[5], false);
        String nbt = Strings.join(SubCommandBase.dropFirstArguments(args, 6), " ");
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
        worldMorph.x = x;
        worldMorph.y = y;
        worldMorph.z = z;
        worldMorph.yaw = yaw;
        worldMorph.pitch = pitch;
        worldMorph.morph = morph;

        PacketWorldMorph message = new PacketWorldMorph(worldMorph);

        int dimension = sender.getEntityWorld().provider.getDimension();
        NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(dimension, x, y, z, 64);

        Dispatcher.DISPATCHER.get().sendToAllAround(message, point);
    }
}