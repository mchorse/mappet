package mchorse.mappet.api.utils;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.dialogues.Dialogue;
import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.utils.WorldUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class Trigger implements INBTSerializable<NBTTagCompound>
{
    public String soundEvent = "";
    public String triggerEvent = "";
    public String command = "";
    public String dialogue = "";
    public String script = "";
    public String scriptFunction = "";

    private boolean empty;

    public void copy(Trigger trigger)
    {
        this.soundEvent = trigger.soundEvent;
        this.triggerEvent = trigger.triggerEvent;
        this.command = trigger.command;
        this.dialogue = trigger.dialogue;
        this.script = trigger.script;
        this.scriptFunction = trigger.scriptFunction;

        this.recalculateEmpty();
    }

    public void recalculateEmpty()
    {
        this.empty = this.soundEvent.isEmpty() && this.triggerEvent.isEmpty() && this.command.isEmpty() && this.dialogue.isEmpty() && this.script.isEmpty();
    }

    public void trigger(EntityLivingBase target)
    {
        this.trigger(new DataContext(target));
    }

    public void trigger(DataContext context)
    {
        if (!this.soundEvent.isEmpty())
        {
            for (EntityPlayerMP player : context.server.getPlayerList().getPlayers())
            {
                WorldUtils.playSound(player, this.soundEvent);
            }
        }

        if (!this.command.isEmpty())
        {
            context.execute(this.command);
        }

        if (!this.triggerEvent.isEmpty())
        {
            Mappet.events.execute(this.triggerEvent, new EventContext(context));
        }

        if (!this.dialogue.isEmpty())
        {
            EntityPlayer player = context.getPlayer();

            if (player instanceof EntityPlayerMP)
            {
                Dialogue dialogue = Mappet.dialogues.load(this.dialogue);

                if (dialogue != null)
                {
                    Mappet.dialogues.open((EntityPlayerMP) player, dialogue, new DialogueContext(context));
                }
            }
        }

        if (!this.script.isEmpty())
        {
            try
            {
                Mappet.scripts.execute(this.script, this.scriptFunction.isEmpty() ? "main" : this.scriptFunction.trim(), context);
            }
            catch (Exception e)
            {
                e.printStackTrace();
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

        if (!this.script.isEmpty())
        {
            tag.setString("Script", this.script);
            tag.setString("ScriptFunction", this.scriptFunction);
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

        if (tag.hasKey("Script"))
        {
            this.script = tag.getString("Script");
        }

        if (tag.hasKey("ScriptFunction"))
        {
            this.scriptFunction = tag.getString("ScriptFunction");
        }

        this.recalculateEmpty();
    }

    public boolean isEmpty()
    {
        return this.empty;
    }
}