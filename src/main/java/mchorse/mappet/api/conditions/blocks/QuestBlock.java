package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.conditions.utils.Target;
import mchorse.mappet.api.states.States;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.utils.EnumUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class QuestBlock extends TargetBlock
{
    public QuestCheck quest = QuestCheck.COMPLETED;

    @Override
    public boolean evaluate(DataContext context)
    {
        if (this.target == Target.GLOBAL)
        {
            States states = this.getStates(context);

            if (this.quest == QuestCheck.ABSENT)
            {
                return !states.wasQuestCompleted(this.id) && this.hasServerInProgress(context);
            }
            else if (this.quest == QuestCheck.PRESENT)
            {
                return this.hasServerInProgress(context);
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
                if (this.quest == QuestCheck.ABSENT)
                {
                    return !character.getStates().wasQuestCompleted(this.id) && !character.getQuests().has(this.id);
                }
                else if (this.quest == QuestCheck.PRESENT)
                {
                    return character.getQuests().has(this.id);
                }
                else
                {
                    return character.getStates().wasQuestCompleted(this.id);
                }
            }
        }

        return false;
    }

    private boolean hasServerInProgress(DataContext context)
    {
        for (EntityPlayer player : context.server.getPlayerList().getPlayers())
        {
            if (Character.get(player).getQuests().has(this.id))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String stringify()
    {
        if (this.quest == QuestCheck.ABSENT)
        {
            return I18n.format("mappet.gui.conditions.quest.is_absent", this.id);
        }
        else if (this.quest == QuestCheck.PRESENT)
        {
            return I18n.format("mappet.gui.conditions.quest.is_present", this.id);
        }

        return I18n.format("mappet.gui.conditions.quest.is_completed", this.id);
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

    public static enum QuestCheck
    {
        ABSENT, PRESENT, COMPLETED
    }
}