package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.quests.GuiObjectives;
import mchorse.mappet.client.gui.quests.GuiRewards;
import mchorse.mappet.client.gui.utils.GuiTriggerElement;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.framework.elements.utils.GuiInventoryElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiQuestPanel extends GuiMappetDashboardPanel<Quest>
{
    public GuiTextElement title;
    public GuiTextElement story;
    public GuiToggleElement cancelable;

    public GuiTriggerElement accept;
    public GuiTriggerElement decline;
    public GuiTriggerElement complete;

    public GuiObjectives objectives;
    public GuiRewards rewards;

    public GuiQuestPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.title = new GuiTextElement(mc, 1000, (text) -> this.data.title = text);
        this.story = new GuiTextElement(mc, 1000, (text) -> this.data.story = text);
        this.cancelable = new GuiToggleElement(mc, IKey.str("Cancelable"), (b) -> this.data.cancelable = b.isToggled());

        this.accept = new GuiTriggerElement(mc);
        this.decline = new GuiTriggerElement(mc);
        this.complete = new GuiTriggerElement(mc);

        this.objectives = new GuiObjectives(mc);
        this.objectives.marginBottom(20);
        this.rewards = new GuiRewards(mc);

        this.add(this.inventory);

        GuiLabel objectiveLabel = Elements.label(IKey.str("Quest objectives")).background();
        GuiLabel rewardLabel = Elements.label(IKey.str("Quest rewards")).background();
        GuiIconElement addObjective = new GuiIconElement(mc, Icons.ADD, (b) -> GuiBase.getCurrent().replaceContextMenu(this.objectives.getAdds()));
        GuiIconElement addReward = new GuiIconElement(mc, Icons.ADD, (b) -> GuiBase.getCurrent().replaceContextMenu(this.rewards.getAdds()));

        addObjective.flex().relative(objectiveLabel).xy(1F, 0.5F).w(10).anchor(1F, 0.5F);
        addReward.flex().relative(rewardLabel).xy(1F, 0.5F).w(10).anchor(1F, 0.5F);
        objectiveLabel.marginTop(12).marginBottom(5).add(addObjective);
        rewardLabel.marginBottom(5).add(addReward);

        this.editor.flex().column(0).padding(10);
        this.editor.add(Elements.label(IKey.str("Quest's title")), this.title);
        this.editor.add(Elements.label(IKey.str("Quest's description")).marginTop(12), this.story);
        this.editor.add(this.cancelable.marginTop(6));
        this.editor.add(objectiveLabel, this.objectives);
        this.editor.add(rewardLabel, this.rewards);
        this.editor.add(Elements.label(IKey.str("Accept quest trigger")).background().marginTop(20).marginBottom(4), this.accept);
        this.editor.add(Elements.label(IKey.str("Decline quest trigger")).background().marginTop(12).marginBottom(4), this.decline);
        this.editor.add(Elements.label(IKey.str("Complete quest trigger")).background().marginTop(12).marginBottom(4), this.complete);
        this.editor.scroll.opposite = true;

        this.fill("", null);
    }

    @Override
    public ContentType getType()
    {
        return ContentType.QUEST;
    }

    @Override
    public String getTitle()
    {
        return "Quests";
    }

    @Override
    public void fill(String id, Quest data)
    {
        super.fill(id, data);

        this.editor.setVisible(data != null);

        if (data != null)
        {
            this.title.setText(data.title);
            this.story.setText(data.story);
            this.cancelable.toggled(data.cancelable);

            this.accept.set(data.accept);
            this.decline.set(data.decline);
            this.complete.set(data.complete);

            this.objectives.set(data.objectives, () -> this.inventory);
            this.rewards.set(data.rewards, () -> this.inventory);
        }

        /* Hack: due to grid resizer with custom width can't access
         * width for reasons it not being assigned yet, I have to double
         * resize it so the second time it could calculate correct height
         * if the editor */
        this.resize();
        this.resize();
    }

    @Override
    public void draw(GuiContext context)
    {
        super.draw(context);

        if (!this.editor.isVisible())
        {
            int w = this.editor.area.w / 2;
            int x = this.editor.area.mx() - w / 2;

            GuiDraw.drawMultiText(this.font, "Select or create a quest in the list on the right, to start editing...", x, this.area.my(), 0xffffff, w, 12, 0.5F, 0.5F);
        }
    }
}