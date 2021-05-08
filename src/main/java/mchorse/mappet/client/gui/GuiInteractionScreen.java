package mchorse.mappet.client.gui;

import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.api.dialogues.DialogueFragment;
import mchorse.mappet.api.quests.chains.QuestInfo;
import mchorse.mappet.api.quests.chains.QuestStatus;
import mchorse.mappet.client.gui.crafting.GuiCrafting;
import mchorse.mappet.client.gui.crafting.ICraftingScreen;
import mchorse.mappet.client.gui.quests.GuiQuestCard;
import mchorse.mappet.client.gui.utils.GuiClickableText;
import mchorse.mappet.client.gui.utils.GuiMorphRenderer;
import mchorse.mappet.client.gui.utils.GuiText;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.crafting.PacketCraftingTable;
import mchorse.mappet.network.common.dialogue.PacketDialogueFragment;
import mchorse.mappet.network.common.dialogue.PacketPickReply;
import mchorse.mappet.network.common.quests.PacketQuestAction;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiLabelListElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiStringListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDrawable;
import mchorse.mclib.client.gui.utils.Label;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import sun.security.x509.AVA;

import java.util.List;

public class GuiInteractionScreen extends GuiBase implements ICraftingScreen
{
    public GuiElement area;

    public GuiMorphRenderer morph;
    public GuiButtonElement back;

    /* Dialogue */
    public GuiScrollElement reaction;
    public GuiText reactionText;
    public GuiScrollElement replies;

    private PacketDialogueFragment fragment;

    /* Crafting */
    public GuiCrafting crafting;

    private CraftingTable table;

    /* Quests */
    public GuiElement quest;
    public GuiLabelListElement<QuestInfo> quests;
    public GuiScrollElement questArea;
    public GuiButtonElement actionQuest;

    private List<QuestInfo> questInfos;

    public GuiInteractionScreen(PacketDialogueFragment fragment)
    {
        super();

        Minecraft mc = Minecraft.getMinecraft();

        /* General */
        this.morph = new GuiMorphRenderer(mc);
        this.morph.flex().relative(this.viewport).x(0.4F).w(0.6F).h(1F);

        this.back = new GuiButtonElement(mc, IKey.str("Back"), (b) -> Dispatcher.sendToServer(new PacketPickReply(-1)));

        /* Hardcoded viewport values */
        this.morph.fov = 40;
        this.morph.setScale(2.1F);
        this.morph.setRotation(-27, 5);
        this.morph.setPosition(-0.1313307F, 1.3154614F, 0.0359409F);
        this.morph.setEnabled(false);

        this.area = new GuiElement(mc);
        this.area.flex().relative(this.viewport).x(0.2F).y(20).w(0.4F).h(1F, -20);

        /* Dialogue */
        this.reaction = new GuiScrollElement(mc);
        this.reactionText = new GuiText(mc);
        this.replies = new GuiScrollElement(mc);

        this.reaction.flex().relative(this.area).w(1F).hTo(this.replies.area).column(5).vertical().stretch().scroll().padding(10);
        this.replies.flex().relative(this.area).y(0.75F).w(1F).hTo(this.area.area, 1F).column(10).vertical().stretch().scroll().padding(10);

        /* Crafting */
        this.crafting = new GuiCrafting(mc);
        this.crafting.flex().relative(this.area).y(0.45F).w(1F).hTo(this.area.area, 1F);

        /* Quests */
        this.quest = new GuiElement(mc);
        this.quests = new GuiLabelListElement<QuestInfo>(mc, (l) -> this.pickQuest(l.get(0).value));
        this.questArea = new GuiScrollElement(mc);
        this.actionQuest = new GuiButtonElement(mc, IKey.str("Accept"), (b) -> this.actionQuest());

        this.quest.flex().relative(this.area).y(0.25F).w(1F).hTo(this.area.area, 1F);
        this.quests.background().flex().relative(this.quest).y(10).w(1F).h(56);
        this.questArea.flex().relative(this.quest).y(66).w(1F).hTo(this.actionQuest.area, -5).column(5).vertical().stretch().scroll().padding(10);
        this.actionQuest.flex().relative(this.quest).x(1F, -10).y(1F, -10).wh(80, 20).anchor(1F, 1F);

        this.quest.add(this.actionQuest, this.questArea, this.quests);

        GuiDrawable drawable = new GuiDrawable((context) ->
        {
            Gui.drawRect(0, 0, this.area.area.x(0.65F), this.area.area.ey(), 0xaa000000);
            GuiDraw.drawHorizontalGradientRect(this.area.area.x(0.65F), 0, this.area.area.x(1.125F), this.area.area.ey(), 0xaa000000, 0);
        });

        this.area.add(this.quest, this.crafting, this.replies, this.reaction);
        this.root.add(this.morph, drawable, this.area);
        this.reaction.add(this.reactionText);

        this.pickReply(fragment);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void updateVisibility()
    {
        this.back.removeFromParent();

        if (this.questInfos != null)
        {
            this.reaction.flex().hTo(this.quest.area);

            this.back.flex().reset().relative(this.quest).x(10).y(1F, -10).wh(80, 20).anchorY(1F);
            this.quest.add(this.back);
        }
        else if (this.table != null)
        {
            this.reaction.flex().hTo(this.crafting.area);

            this.back.flex().reset().relative(this.crafting).x(10).y(1F, -10).wh(80, 20).anchorY(1F);
            this.crafting.add(this.back);
        }
        else
        {
            this.reaction.flex().hTo(this.replies.area);
        }

        this.quest.setVisible(this.questInfos != null);
        this.crafting.setVisible(this.table != null);
        this.replies.setVisible(this.questInfos == null && this.table == null);
    }

    /* Dialogue */

    private void setFragment(PacketDialogueFragment fragment)
    {
        if (fragment.morph != null)
        {
            this.morph.morph.set(fragment.morph);
        }

        this.fragment = fragment;
        this.table = null;
        this.questInfos = null;

        this.reactionText.text(fragment.reaction.getProcessedText());
        this.reactionText.color(fragment.reaction.color, true);
        this.replies.removeAll();

        for (DialogueFragment reply : fragment.replies)
        {
            GuiClickableText replyElement = new GuiClickableText(Minecraft.getMinecraft());

            replyElement.callback(this::pickReply);
            replyElement.color(reply.color, true);
            replyElement.hoverColor(ColorUtils.multiplyColor(reply.color, 0.7F));
            this.replies.add(replyElement.text("> " + reply.getProcessedText()));
        }

        this.updateVisibility();
        this.root.resize();
    }

    private void pickReply(GuiClickableText text)
    {
        Dispatcher.sendToServer(new PacketPickReply(this.replies.getChildren().indexOf(text)));
    }

    public void pickReply(PacketDialogueFragment fragment)
    {
        if (!fragment.quests.isEmpty())
        {
            this.setFragment(fragment);
            this.setQuests(fragment.quests);
        }
        else if (fragment.table != null)
        {
            this.setFragment(fragment);
            this.setCraftingTable(fragment.table);
        }
        else if (fragment.replies.isEmpty())
        {
            this.mc.displayGuiScreen(null);
        }
        else
        {
            this.setFragment(fragment);
        }
    }

    /* Crafting */

    public void setCraftingTable(CraftingTable table)
    {
        this.table = table;

        this.crafting.set(table);
        this.updateVisibility();
        this.root.resize();
    }

    @Override
    public void refresh()
    {
        this.crafting.refresh();
    }

    /* Quests */

    public void setQuests(List<QuestInfo> quests)
    {
        this.questInfos = quests;

        this.quests.clear();

        for (QuestInfo info : quests)
        {
            String title = info.quest.title;

            if (info.status == QuestStatus.COMPLETED)
            {
                title = TextFormatting.GOLD + title;
            }
            else if (info.status == QuestStatus.UNAVAILABLE)
            {
                title = TextFormatting.GRAY + title;
            }

            this.quests.add(IKey.str(title), info);
        }

        this.quests.sort();

        this.quests.setIndex(0);
        this.pickQuest(this.quests.getCurrentFirst().value);

        this.updateVisibility();
        this.root.resize();
    }

    public void pickQuest(QuestInfo info)
    {
        this.questArea.removeAll();
        this.actionQuest.label.set(info != null && info.status == QuestStatus.COMPLETED ? "Complete" : "Accept");

        if (info != null)
        {
            GuiQuestCard.fillQuest(this.questArea, info.quest, true);
        }

        this.questArea.resize();
        this.actionQuest.setEnabled(info != null && info.status != QuestStatus.UNAVAILABLE);
    }

    public void actionQuest()
    {
        Label<QuestInfo> label = this.quests.getCurrentFirst();

        Dispatcher.sendToServer(new PacketQuestAction(label.value.quest.getId(), label.value.status));

        if (label.value.status == QuestStatus.AVAILABLE)
        {
            label.value.status = QuestStatus.UNAVAILABLE;
            label.title.set(TextFormatting.GRAY + label.value.quest.title);

            this.actionQuest.setEnabled(false);
        }
        else if (label.value.status == QuestStatus.COMPLETED)
        {
            this.quests.remove(label);
            this.quests.setIndex(-1);

            this.quests.setIndex(0);

            label = this.quests.getCurrentFirst();

            this.pickQuest(label == null ? null : label.value);
        }
    }

    @Override
    protected void closeScreen()
    {
        super.closeScreen();

        if (this.table != null)
        {
            Dispatcher.sendToServer(new PacketCraftingTable(null));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        super.drawScreen(mouseX, mouseY, partialTicks);

        int y = this.table != null ? this.crafting.area.y - 1 : (this.questInfos != null ? this.quest.area.y - 1 : this.replies.area.y - 1);

        GlStateManager.color(1F, 1F, 1F, 1F);
        GuiDraw.drawHorizontalGradientRect(this.replies.area.x - 20, y, this.replies.area.mx(), y + 1, 0, 0x88ffffff);
        GuiDraw.drawHorizontalGradientRect(this.replies.area.mx(), y, this.replies.area.ex() + 20, y + 1, 0x88ffffff, 0);

        if (this.questInfos != null && this.quests.getList().isEmpty())
        {
            int w = (int) (this.questArea.area.w / 1.5F);

            GuiDraw.drawMultiText(this.fontRenderer, "There are no more available quests for you here...", this.questArea.area.mx() - w / 2, this.quest.area.my(), 0xffffff, w, 12, 0.5F, 0.5F);
        }

        this.drawCenteredString(this.fontRenderer, this.fragment.title, this.reaction.area.mx(), 10, 0xffffff);
    }
}