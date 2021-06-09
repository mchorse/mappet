package mchorse.mappet.api.events.nodes;

import mchorse.mappet.api.events.EventContext;
import net.minecraft.nbt.NBTTagCompound;

public class CommandNode extends EventBaseNode
{
    public String command = "";

    public CommandNode()
    {}

    public CommandNode(String command)
    {
        this.command = command;
    }

    @Override
    protected String getDisplayTitle()
    {
        return this.command;
    }

    @Override
    public int execute(EventContext context)
    {
        boolean result = context.data.execute(this.command) > 0;

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