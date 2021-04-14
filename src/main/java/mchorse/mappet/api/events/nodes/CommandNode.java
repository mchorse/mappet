package mchorse.mappet.api.events.nodes;

import mchorse.mappet.api.events.EventContext;
import net.minecraft.nbt.NBTTagCompound;

public class CommandNode extends EventNode
{
    public String command = "";

    public CommandNode()
    {}

    public CommandNode(String command)
    {
        this.command = command;
    }

    @Override
    public int getColor()
    {
        return 0x942aff;
    }

    @Override
    public int execute(EventContext context)
    {
        String command = context.processCommand(this.command);
        boolean result = context.server.getCommandManager().executeCommand(context.server, command) > 0;

        context.log("Executed \"" + this.command + "\" " + (result ? "successfully" : "unsuccessfully"));

        return this.booleanToExecutionCode(result);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        if (!this.command.isEmpty())
        {
            tag.setString("Command", this.command);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Command"))
        {
            this.command = tag.getString("Command");
        }
    }
}