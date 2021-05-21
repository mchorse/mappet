package mchorse.mappet.api.quests;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.quests.objectives.AbstractObjective;
import mchorse.mappet.api.quests.objectives.StateObjective;
import mchorse.mappet.api.quests.objectives.KillObjective;
import mchorse.mappet.api.quests.rewards.IReward;
import mchorse.mappet.api.utils.AbstractData;
import mchorse.mappet.api.utils.Trigger;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mclib.utils.TextUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class Quest extends AbstractData implements INBTPartialSerializable
{
    public String title = "";
    public String story = "";
    public boolean cancelable = true;

    public Trigger accept = new Trigger();
    public Trigger decline = new Trigger();
    public Trigger complete = new Trigger();

    public final List<AbstractObjective> objectives = new ArrayList<AbstractObjective>();
    public final List<IReward> rewards = new ArrayList<IReward>();

    public Quest()
    {}

    public String getProcessedTitle()
    {
        return TextUtils.processColoredText(this.title);
    }

    /* Quest building */

    public Quest setStory(String title, String story)
    {
        this.title = title;
        this.story = story;

        return this;
    }

    public Quest addObjective(AbstractObjective objective)
    {
        this.objectives.add(objective);

        return this;
    }

    public Quest addReward(IReward reward)
    {
        this.rewards.add(reward);

        return this;
    }

    /* Quest hooks */

    public void mobWasKilled(EntityPlayer player, Entity entity)
    {
        for (AbstractObjective objective : this.objectives)
        {
            if (objective instanceof KillObjective)
            {
                ((KillObjective) objective).playerKilled(player, entity);
            }
        }
    }

    public boolean stateWasUpdated(EntityPlayer player)
    {
        int i = 0;

        for (AbstractObjective objective : this.objectives)
        {
            if (objective instanceof StateObjective)
            {
                i += ((StateObjective) objective).updateValue(player) ? 1 : 0;
            }
        }

        return i > 0;
    }

    /* Rewards */

    public boolean isComplete(EntityPlayer player)
    {
        boolean result = true;

        for (AbstractObjective objective : this.objectives)
        {
            result = result && objective.isComplete(player);
        }

        return result;
    }

    public void reward(EntityPlayer player)
    {
        for (AbstractObjective objective : this.objectives)
        {
            objective.complete(player);
        }

        for (IReward reward : this.rewards)
        {
            reward.reward(player);
        }

        this.complete.trigger(player);

        /* Write down that the quest was completed */
        ICharacter character = Character.get(player);

        if (character != null)
        {
            character.getStates().completeQuest(this.getId());
        }

        Mappet.states.completeQuest(this.getId());
    }

    public boolean rewardIfComplete(EntityPlayer player)
    {
        if (!this.isComplete(player))
        {
            return false;
        }

        this.reward(player);

        return true;
    }

    /* NBT stuff */

    @Override
    public NBTTagCompound partialSerializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList objectives = new NBTTagList();

        tag.setTag("Objectives", objectives);

        for (AbstractObjective objective : this.objectives)
        {
            objectives.appendTag(objective.partialSerializeNBT());
        }

        return tag;
    }

    @Override
    public void partialDeserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Objectives", Constants.NBT.TAG_LIST))
        {
            NBTTagList list = tag.getTagList("Objectives", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < Math.min(list.tagCount(), this.objectives.size()); i++)
            {
                this.objectives.get(i).partialDeserializeNBT(list.getCompoundTagAt(i));
            }
        }
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList objectives = new NBTTagList();
        NBTTagList rewards = new NBTTagList();

        tag.setString("Title", this.title);
        tag.setString("Story", this.story);

        if (!this.cancelable)
        {
            tag.setBoolean("Cancelable", this.cancelable);
        }

        NBTTagCompound accept = this.accept.serializeNBT();
        NBTTagCompound decline = this.decline.serializeNBT();
        NBTTagCompound complete = this.complete.serializeNBT();

        if (accept.getSize() > 0) tag.setTag("Accept", accept);
        if (decline.getSize() > 0) tag.setTag("Decline", decline);
        if (complete.getSize() > 0) tag.setTag("Complete", complete);

        tag.setTag("Objectives", objectives);
        tag.setTag("Rewards", rewards);

        for (AbstractObjective objective : this.objectives)
        {
            NBTTagCompound item = objective.serializeNBT();

            item.setString("Type", objective.getType());
            objectives.appendTag(item);
        }

        for (IReward reward : this.rewards)
        {
            NBTTagCompound item = reward.serializeNBT();

            item.setString("Type", reward.getType());
            rewards.appendTag(item);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.title = tag.getString("Title");
        this.story = tag.getString("Story");

        if (tag.hasKey("Cancelable"))
        {
            this.cancelable = tag.getBoolean("Cancelable");
        }

        if (tag.hasKey("Accept"))
        {
            this.accept.deserializeNBT(tag.getCompoundTag("Accept"));
        }

        if (tag.hasKey("Decline"))
        {
            this.decline.deserializeNBT(tag.getCompoundTag("Decline"));
        }

        if (tag.hasKey("Complete"))
        {
            this.complete.deserializeNBT(tag.getCompoundTag("Complete"));
        }

        if (tag.hasKey("Objectives"))
        {
            NBTTagList list = tag.getTagList("Objectives", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < list.tagCount(); i ++)
            {
                NBTTagCompound tagCompound = list.getCompoundTagAt(i);
                AbstractObjective objective = AbstractObjective.fromType(tagCompound.getString("Type"));

                if (objective != null)
                {
                    objective.deserializeNBT(tagCompound);
                    this.objectives.add(objective);
                }
            }
        }

        if (tag.hasKey("Rewards"))
        {
            NBTTagList list = tag.getTagList("Rewards", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < list.tagCount(); i ++)
            {
                NBTTagCompound tagCompound = list.getCompoundTagAt(i);
                IReward reward = IReward.fromType(tagCompound.getString("Type"));

                if (reward != null)
                {
                    reward.deserializeNBT(tagCompound);
                    this.rewards.add(reward);
                }
            }
        }
    }
}