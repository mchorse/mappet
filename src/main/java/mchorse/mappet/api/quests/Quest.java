package mchorse.mappet.api.quests;

import mchorse.mappet.api.quests.objectives.IObjective;
import mchorse.mappet.api.quests.objectives.KillObjective;
import mchorse.mappet.api.quests.rewards.IReward;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

public class Quest implements INBTSerializable<NBTTagCompound>
{
    private String id = "";
    public String customTitle = "";
    public String customStory = "";

    public QuestTrigger accept = new QuestTrigger();
    public QuestTrigger decline = new QuestTrigger();
    public QuestTrigger complete = new QuestTrigger();

    public final List<IObjective> objectives = new ArrayList<IObjective>();
    public final List<IReward> rewards = new ArrayList<IReward>();

    public Quest()
    {}

    public Quest(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    /* Quest building */

    public Quest setStory(String title, String story)
    {
        this.customTitle = title;
        this.customStory = story;

        return this;
    }

    public Quest addObjective(IObjective objective)
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
        for (IObjective objective : this.objectives)
        {
            if (objective instanceof KillObjective)
            {
                ((KillObjective) objective).playerKilled(player, entity);
            }
        }
    }

    /* Rewards */

    public boolean isComplete(EntityPlayer player)
    {
        boolean result = true;

        for (IObjective objective : this.objectives)
        {
            result = result && objective.isComplete(player);
        }

        return result;
    }

    public void reward(EntityPlayer player)
    {
        for (IObjective objective : this.objectives)
        {
            objective.complete(player);
        }

        for (IReward reward : this.rewards)
        {
            reward.reward(player);
        }

        this.complete.trigger(player);
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

    public Quest copy()
    {
        Quest quest = new Quest(this.id);

        quest.customTitle = this.customTitle;
        quest.customStory = this.customStory;

        quest.accept.copy(this.accept);
        quest.decline.copy(this.decline);
        quest.complete.copy(this.complete);

        for (IObjective objective : this.objectives)
        {
            quest.objectives.add(objective.copy());
        }

        for (IReward reward : this.rewards)
        {
            quest.rewards.add(reward.copy());
        }

        return quest;
    }

    /* NBT stuff */

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList objectives = new NBTTagList();
        NBTTagList rewards = new NBTTagList();

        tag.setString("Id", this.id);
        tag.setString("Title", this.customTitle);
        tag.setString("Story", this.customStory);

        NBTTagCompound accept = this.accept.serializeNBT();
        NBTTagCompound decline = this.decline.serializeNBT();
        NBTTagCompound complete = this.complete.serializeNBT();

        if (accept.getSize() > 0) tag.setTag("Accept", accept);
        if (decline.getSize() > 0) tag.setTag("Decline", decline);
        if (complete.getSize() > 0) tag.setTag("Complete", complete);

        tag.setTag("Objectives", objectives);
        tag.setTag("Rewards", rewards);

        for (IObjective objective : this.objectives)
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
        this.id = tag.getString("Id");
        this.customTitle = tag.getString("Title");
        this.customStory = tag.getString("Story");

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
                IObjective objective = IObjective.fromType(tagCompound.getString("Type"));

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