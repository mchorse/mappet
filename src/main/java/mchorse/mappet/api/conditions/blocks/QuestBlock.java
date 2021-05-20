package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.conditions.utils.Target;
import mchorse.mappet.api.states.States;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.utils.EnumUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class QuestBlock extends TargetBlock
{
    public QuestCheck quest = QuestCheck.COMPLETED;

    @Override
    public int getColor()
    {
        return 0xffaa00;
    }

    @Override
    public boolean evaluate(DataContext context)
    {
        if (this.target == Target.GLOBAL)
        {
            States states = this.getStates(context);

            if (this.quest == QuestCheck.ABSENT)
            {
                for (EntityPlayer player : context.server.getPlayerList().getPlayers())
                {
                    if (Character.get(player).getQuests().has(this.id))
                    {
                        return false;
                    }
                }

                return !states.wasQuestCompleted(this.id);
            }
            else if (this.quest == QuestCheck.PRESENT)
            {
                for (EntityPlayer player : context.server.getPlayerList().getPlayers())
                {
                    if (Character.get(player).getQuests().has(this.id))
                    {
                        return true;
                    }
                }
            }
            else
            {
                return states.wasQuestCompleted(this.id);
            }
        }
        else
        {
            ICharacter character = this.getCharacter(context);

            if (character != null)
            {
                boolean result = false;

                if (this.quest == QuestCheck.ABSENT)
                {
                    result = !character.getStates().wasQuestCompleted(this.id) && !character.getQuests().has(this.id);
                }
                else if (this.quest == QuestCheck.PRESENT)
                {
                    result = character.getQuests().has(this.id);
                }
                else
                {
                    result = character.getStates().wasQuestCompleted(this.id);
                }

                return result;
            }
        }

        return false;
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setInteger("Quest", this.quest.ordinal());
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.quest = EnumUtils.getValue(tag.getInteger("Quest"), QuestCheck.values(), QuestCheck.COMPLETED);
    }

    @Override
    public String stringify()
    {
        return this.id;
    }

    public static enum QuestCheck
    {
        ABSENT, PRESENT, COMPLETED
    }
}