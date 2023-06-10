package mchorse.mappet.commands.crafting;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.crafting.CraftingRecipe;
import mchorse.mappet.api.crafting.CraftingTable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandCraftingDrop extends CommandCraftingBase
{
    @Override
    public String getName()
    {
        return "drop";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.crafting.drop";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}crafting drop{r} {7}<id> [index] [x] [y] [z] [mx] [my] [mz]{r}";
    }

    @Override
    public int getRequiredArgs()
    {
        return 1;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        String id = args[0];
        CraftingTable table = this.getCraftingTable(id);

        if (table.recipes.isEmpty())
        {
            throw new CommandException("crafting.empty", id);
        }

        int index = (int) Math.min(Math.round(Math.random() * table.recipes.size()), table.recipes.size() - 1);

        if (args.length > 1 && !args[1].equals("@r"))
        {
            index = CommandBase.parseInt(args[1], 0, table.recipes.size() - 1);
        }

        Vec3d pos = sender.getPositionVector();
        double x = args.length > 2 ? CommandBase.parseDouble(pos.x, args[2], false) : pos.x;
        double y = args.length > 3 ? CommandBase.parseDouble(pos.y, args[3], false) : pos.y;
        double z = args.length > 4 ? CommandBase.parseDouble(pos.z, args[4], false) : pos.z;

        CraftingRecipe recipe = table.recipes.get(index);

        for (ItemStack stack : recipe.output)
        {
            if (stack == null || stack.isEmpty())
            {
                throw new CommandException("crafting.empty_output", id, index);
            }
        }

        double mx = args.length > 5 ? CommandBase.parseDouble(args[5]) : 0;
        double my = args.length > 6 ? CommandBase.parseDouble(args[6]) : 0;
        double mz = args.length > 7 ? CommandBase.parseDouble(args[7]) : 0;

        for (int i = 0; i < recipe.output.size(); i++)
        {
            ItemStack stack = recipe.output.get(i);
            EntityItem item = new EntityItem(sender.getEntityWorld(), x, y, z, stack.copy());

            item.motionX = mx;
            item.motionY = my;
            item.motionZ = mz;

            if (i > 0)
            {
                item.motionX += (Math.random() - 0.5) * 0.1;
                item.motionY += (Math.random() - 0.5) * 0.1;
                item.motionZ += (Math.random() - 0.5) * 0.1;
            }

            sender.getEntityWorld().spawnEntity(item);
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, Mappet.crafting.getKeys());
        }

        if (args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, "@r");
        }

        return Collections.<String>emptyList();
    }
}
