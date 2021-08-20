package mchorse.mappet.api.events.nodes;

import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.api.triggers.blocks.DialogueTriggerBlock;
import mchorse.mappet.api.triggers.blocks.EventTriggerBlock;
import mchorse.mappet.api.triggers.blocks.ScriptTriggerBlock;
import mchorse.mappet.api.triggers.blocks.StringTriggerBlock;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TriggerNode extends EventBaseNode
{
    public Trigger trigger = new Trigger();
    public String customData = "";
    public boolean cancel;

    @Override
    @SideOnly(Side.CLIENT)
    protected String getDisplayTitle()
    {
        return I18n.format("mappet.gui.trigger.quantity", this.trigger.blocks.size());
    }

    @Override
    public int execute(EventContext context)
    {
        DataContext newContext = this.apply(context);

        this.trigger.trigger(newContext);

        if (this.cancel)
        {
            if (!context.data.isCanceled())
            {
                context.data.cancel(newContext.isCanceled());
            }

            return this.booleanToExecutionCode(true);
        }

        return this.booleanToExecutionCode(!newContext.isCanceled());
    }

    public DataContext apply(EventContext event)
    {
        DataContext context = event.data.copy();

        context.parse(event.data.process(this.customData));

        return context;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        tag.setTag("Trigger", this.trigger.serializeNBT());
        tag.setString("CustomData", this.customData);
        tag.setBoolean("Cancel", this.cancel);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.trigger.deserializeNBT(tag.getCompoundTag("Trigger"));
        this.customData = tag.getString("CustomData");
        this.cancel = tag.getBoolean("Cancel");

        /* Backward compatibility with gamma build */
        String type = tag.getString("Type");
        StringTriggerBlock block = null;

        if (type.equals("event"))
        {
            block = new EventTriggerBlock();
            block.string = tag.getString("DataId");
        }
        else if (type.equals("dialogue"))
        {
            block = new DialogueTriggerBlock();
            block.string = tag.getString("DataId");
        }
        else if (type.equals("script"))
        {
            block = new ScriptTriggerBlock();
            block.string = tag.getString("DataId");

            ((ScriptTriggerBlock) block).function = tag.getString("Function");
        }

        if (block != null)
        {
            this.trigger.blocks.add(block);
        }
    }
}