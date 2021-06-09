package mchorse.mappet.api.triggers;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.api.triggers.blocks.AbstractTriggerBlock;
import mchorse.mappet.api.triggers.blocks.CommandTriggerBlock;
import mchorse.mappet.api.triggers.blocks.DialogueTriggerBlock;
import mchorse.mappet.api.triggers.blocks.EventTriggerBlock;
import mchorse.mappet.api.triggers.blocks.ScriptTriggerBlock;
import mchorse.mappet.api.triggers.blocks.SoundTriggerBlock;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

public class Trigger implements INBTSerializable<NBTTagCompound>
{
    public final List<AbstractTriggerBlock> blocks = new ArrayList<AbstractTriggerBlock>();

    private boolean empty;

    public void copy(Trigger trigger)
    {
        this.blocks.clear();

        for (AbstractTriggerBlock block : trigger.blocks)
        {
            String type = CommonProxy.getTriggerBlocks().getType(block);
            AbstractTriggerBlock newBlock = CommonProxy.getTriggerBlocks().create(type);

            newBlock.deserializeNBT(block.serializeNBT());

            this.blocks.add(newBlock);
        }

        this.recalculateEmpty();
    }

    public void recalculateEmpty()
    {
        this.empty = true;

        for (AbstractTriggerBlock block : this.blocks)
        {
            if (!block.isEmpty())
            {
                this.empty = false;
            }
        }
    }

    public void trigger(EntityLivingBase target)
    {
        this.trigger(new DataContext(target));
    }

    public void trigger(DataContext context)
    {
        for (AbstractTriggerBlock block : this.blocks)
        {
            block.triggerWithDelay(context);
        }
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList blocks = new NBTTagList();

        for (AbstractTriggerBlock block : this.blocks)
        {
            NBTTagCompound blockTag = block.serializeNBT();

            blockTag.setString("Type", CommonProxy.getTriggerBlocks().getType(block));
            blocks.appendTag(blockTag);
        }

        tag.setTag("Blocks", blocks);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.blocks.clear();

        /* Backward compatibility with alpha and beta builds */
        if (tag.hasKey("Sound")) this.blocks.add(new SoundTriggerBlock(tag.getString("Sound")));
        if (tag.hasKey("Trigger")) this.blocks.add(new EventTriggerBlock(tag.getString("Trigger")));
        if (tag.hasKey("Command")) this.blocks.add(new CommandTriggerBlock(tag.getString("Command")));
        if (tag.hasKey("Dialogue")) this.blocks.add(new DialogueTriggerBlock(tag.getString("Dialogue")));
        if (tag.hasKey("Script")) this.blocks.add(new ScriptTriggerBlock(tag.getString("Script"), tag.getString("ScriptFunction")));

        if (tag.hasKey("Blocks"))
        {
            NBTTagList blocks = tag.getTagList("Blocks", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < blocks.tagCount(); i++)
            {
                NBTTagCompound blockTag = blocks.getCompoundTagAt(i);
                AbstractTriggerBlock block = CommonProxy.getTriggerBlocks().create(blockTag.getString("Type"));

                if (block != null)
                {
                    block.deserializeNBT(blockTag);
                    this.blocks.add(block);
                }
            }
        }

        this.recalculateEmpty();
    }

    public boolean isEmpty()
    {
        return this.empty;
    }
}