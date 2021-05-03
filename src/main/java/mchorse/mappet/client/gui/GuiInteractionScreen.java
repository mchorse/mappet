package mchorse.mappet.client.gui;

import mchorse.mappet.api.dialogues.DialogueFragment;
import mchorse.mappet.client.gui.utils.GuiClickableText;
import mchorse.mappet.client.gui.utils.GuiText;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.dialogue.PacketDialogueFragment;
import mchorse.mappet.network.common.dialogue.PacketPickReply;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiStringListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.util.List;
import java.util.stream.Collectors;

public class GuiInteractionScreen extends GuiBase
{
    public GuiElement area;

    /* Dialogue */
    public GuiScrollElement reaction;
    public GuiText reactionText;
    public GuiScrollElement replies;

    private PacketDialogueFragment fragment;

    public GuiInteractionScreen(PacketDialogueFragment fragment)
    {
        super();

        Minecraft mc = Minecraft.getMinecraft();

        this.area = new GuiElement(mc);
        this.area.flex().relative(this.viewport).x(0.2F).y(20).w(0.4F).h(1F, -20);

        this.reaction = new GuiScrollElement(mc);
        this.reactionText = new GuiText(mc);
        this.replies = new GuiScrollElement(mc);

        this.reaction.flex().relative(this.area).w(1F).hTo(this.replies.area).column(5).vertical().stretch().scroll().padding(10);
        this.replies.flex().relative(this.area).y(0.75F).w(1F).hTo(this.area.area, 1F).column(10).vertical().stretch().scroll().padding(10);

        this.area.add(this.replies, this.reaction);
        this.root.add(this.area);
        this.reaction.add(this.reactionText);

        this.setFragment(fragment);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    private void setFragment(PacketDialogueFragment fragment)
    {
        this.fragment = fragment;

        this.reactionText.text(fragment.reaction.text.replace("\\n", "\n"));
        this.replies.removeAll();

        for (DialogueFragment reply : fragment.replies)
        {
            GuiClickableText replyElement = new GuiClickableText(Minecraft.getMinecraft());

            replyElement.callback(this::pickReply);
            this.replies.add(replyElement.text("> " + reply.text.replace("\\n", "\n")));
        }

        this.root.resize();
    }

    private void pickReply(GuiClickableText text)
    {
        Dispatcher.sendToServer(new PacketPickReply(this.replies.getChildren().indexOf(text)));
    }

    public void pickReply(PacketDialogueFragment fragment)
    {
        if (fragment.replies.isEmpty())
        {
            this.mc.displayGuiScreen(null);
        }
        else
        {
            this.setFragment(fragment);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        Gui.drawRect(0, 0, this.area.area.x(0.65F), this.area.area.x(1.125F), 0xaa000000);
        GuiDraw.drawHorizontalGradientRect(this.area.area.x(0.65F), 0, this.area.area.x(1.125F), this.area.area.ey(), 0xaa000000, 0);

        GuiDraw.drawHorizontalGradientRect(this.replies.area.x - 20, this.replies.area.y - 1, this.replies.area.mx(), this.replies.area.y, 0, 0x88ffffff);
        GuiDraw.drawHorizontalGradientRect(this.replies.area.mx(), this.replies.area.y - 1, this.replies.area.ex() + 20, this.replies.area.y, 0x88ffffff, 0);

        super.drawScreen(mouseX, mouseY, partialTicks);

        this.drawCenteredString(this.fontRenderer, this.fragment.title, this.reaction.area.mx(), 10, 0xffffff);
    }
}