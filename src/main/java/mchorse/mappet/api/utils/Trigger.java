package mchorse.mappet.api.utils;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.dialogues.Dialogue;
import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.events.EventContext;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.INBTSerializable;

public class Trigger implements INBTSerializable<NBTTagCompound>
{
    public String soundEvent = "";
    public String triggerEvent = "";
    public String command = "";
    public String dialogue = "";

    public void copy(Trigger trigger)
    {
        this.soundEvent = trigger.soundEvent;
        this.triggerEvent = trigger.triggerEvent;
        this.command = trigger.command;
        this.dialogue = trigger.dialogue;
    }

    public void trigger(EntityLivingBase target)
    {
        this.trigger(new DataContext(target));
    }

    public void trigger(DataContext context)
    {
        EntityLivingBase target = context.subject;

        if (!this.command.isEmpty())
        {
            context.execute(this.command);
        }

        SoundEvent event = null;

        if (!this.soundEvent.isEmpty())
        {
            event = SoundEvent.REGISTRY.getObject(new ResourceLocation(this.soundEvent));
        }

        if (event != null && target != null)
        {
            target.world.playSound(null, target.posX, target.posY, target.posZ, event, SoundCategory.MASTER, 1, 1);
        }

        if (!this.triggerEvent.isEmpty())
        {
            Mappet.events.execute(this.triggerEvent, new EventContext(context));
        }

        if (!this.dialogue.isEmpty())
        {
            EntityPlayerMP player = null;

            if (context.subject instanceof EntityPlayerMP)
            {
                player = (EntityPlayerMP) context.subject;
            }
            else if (context.object instanceof EntityPlayerMP)
            {
                player = (EntityPlayerMP) context.object;
            }

            if (player != null)
            {
                Dialogue dialogue = Mappet.dialogues.load(this.dialogue);

                if (dialogue != null)
                {
                    Mappet.dialogues.open(player, dialogue, new DialogueContext(context));
                }
            }
        }
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        if (!this.soundEvent.isEmpty())
        {
            tag.setString("Sound", this.soundEvent);
        }

        if (!this.triggerEvent.isEmpty())
        {
            tag.setString("Trigger", this.triggerEvent);
        }

        if (!this.command.isEmpty())
        {
            tag.setString("Command", this.command);
        }

        if (!this.dialogue.isEmpty())
        {
            tag.setString("Dialogue", this.dialogue);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Sound"))
        {
            this.soundEvent = tag.getString("Sound");
        }

        if (tag.hasKey("Trigger"))
        {
            this.triggerEvent = tag.getString("Trigger");
        }

        if (tag.hasKey("Command"))
        {
            this.command = tag.getString("Command");
        }

        if (tag.hasKey("Dialogue"))
        {
            this.dialogue = tag.getString("Dialogue");
        }
    }

    public boolean isEmpty()
    {
        return this.triggerEvent.isEmpty() && this.command.isEmpty() && this.dialogue.isEmpty();
    }
}