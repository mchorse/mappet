package mchorse.mappet.commands.dialogues;

import mchorse.mappet.commands.MappetSubCommandBase;
import net.minecraft.command.ICommandSender;

public class CommandDialogue extends MappetSubCommandBase
{
    public CommandDialogue()
    {
        this.add(new CommandDialogueOpen());
    }

    @Override
    public String getName()
    {
        return "dialogue";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.dialogue.help";
    }
}
