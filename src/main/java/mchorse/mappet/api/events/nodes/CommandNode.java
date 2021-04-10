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
    public boolean execute(EventContext context)
    {
        boolean result = context.server.getCommandManager().executeCommand(context.server, this.command) > 0;

        context.log("Executed \"" + this.command + "\" " + (result ? "successfully" : "unsuccessfully"));

        return result;
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