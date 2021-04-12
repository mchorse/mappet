package mchorse.mappet.api.quests;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.EventContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.INBTSerializable;

public class QuestTrigger implements INBTSerializable<NBTTagCompound>
{
    public String soundEvent = "";
    public String triggerEvent = "";

    public void copy(QuestTrigger trigger)
    {
        this.soundEvent = trigger.soundEvent;
        this.triggerEvent = trigger.triggerEvent;
    }

    public void trigger(EntityPlayer player)
    {
        SoundEvent event = null;

        if (!this.soundEvent.isEmpty())
        {
            event = SoundEvent.REGISTRY.getObject(new ResourceLocation(this.soundEvent));
        }

        if (event != null)
        {
            player.world.playSound(null, player.posX, player.posY, player.posZ, event, SoundCategory.MASTER, 1, 1);
        }

        if (!this.triggerEvent.isEmpty())
        {
            Mappet.events.execute(this.triggerEvent, new EventContext(player.getServer(), player));
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
    }
}