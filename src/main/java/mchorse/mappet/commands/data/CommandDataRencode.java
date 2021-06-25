package mchorse.mappet.commands.data;

import mchorse.mappet.Mappet;
import mchorse.mappet.commands.MappetCommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.DimensionManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;

public class CommandDataRencode extends MappetCommandBase
{
    @Override
    public String getName()
    {
        return "rencode";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.data.rencode";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}data rencode{r} {7}<from> <to>{r}";
    }

    @Override
    public int getRequiredArgs()
    {
        return 2;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        File file = new File(DimensionManager.getCurrentSaveRootDirectory(), Mappet.MOD_ID);
        Charset from = this.getCharset(args[0]);
        Charset to = this.getCharset(args[1]);

        if (from == null)
        {
            this.getL10n().error(sender, "data.empty_charset", args[0]);

            return;
        }

        if (to == null)
        {
            this.getL10n().error(sender, "data.empty_charset", args[1]);

            return;
        }

        if (file.isDirectory())
        {
            this.recode(file, from, to);

            this.getL10n().info(sender, "data.rencoded", from.name(), to.name());
        }
    }

    private void recode(File target, Charset from, Charset to)
    {
        File[] files = target.listFiles();

        if (files == null)
        {
            return;
        }

        for (File file : files)
        {
            if (file.isFile())
            {
                this.recodeFile(file, from, to);
            }
            else if (file.isDirectory())
            {
                this.recode(file, from, to);
            }
        }
    }

    private void recodeFile(File file, Charset from, Charset to)
    {
        try
        {
            FileUtils.writeStringToFile(file, FileUtils.readFileToString(file, from), to);
        }
        catch (Exception e)
        {}
    }

    private Charset getCharset(String arg)
    {
        try
        {
            return Charset.forName(arg);
        }
        catch (Exception e)
        {}

        return null;
    }
}