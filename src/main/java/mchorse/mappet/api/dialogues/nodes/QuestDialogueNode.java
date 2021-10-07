package mchorse.mappet.api.dialogues.nodes;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.events.nodes.EventBaseNode;
import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class QuestDialogueNode extends EventBaseNode
{
    public String quest = "";
    public boolean skipIfCompleted;

    @Override
    public int execute(EventContext context)
    {
        if (context instanceof DialogueContext)
        {
            EntityPlayer player = context.data.getPlayer();

            if (this.skipIfCompleted && this.isPlayerCompletedQuest(player))
            {
                return EventBaseNode.ALL;
            }

            ((DialogueContext) context).setQuest(this);
        }

        return EventBaseNode.HALT;
    }

    private boolean isPlayerCompletedQuest(EntityPlayer player)
    {
        Quest quest = Mappet.quests.load(this.quest);

        if (quest != null)
        {
            ICharacter character = Character.get(player);

            if (character != null)
            {
                return character.getStates().wasQuestCompleted(this.quest);
            }
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String getDisplayTitle()
    {
        return this.quest;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        tag.setString("Quest", this.quest);
        tag.setBoolean("SkipIfCompleted", this.skipIfCompleted);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Quest"))
        {
            this.quest = tag.getString("Quest");
        }

        this.skipIfCompleted = tag.getBoolean("SkipIfCompleted");
    }
}
