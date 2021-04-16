package mchorse.mappet.client.gui;

import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.dialogue.PacketDialogueFragment;
import mchorse.mappet.network.common.dialogue.PacketPickReply;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiStringListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.util.List;
import java.util.stream.Collectors;

public class GuiDialogueScreen extends GuiBase
{
    public GuiElement dialogue;
    public GuiStringListElement replies;

    private PacketDialogueFragment fragment;

    public GuiDialogueScreen(PacketDialogueFragment fragment)
    {
        super();

        Minecraft mc = Minecraft.getMinecraft();

        this.dialogue = new GuiElement(mc);
        this.replies = new GuiStringListElement(mc, (list) -> this.pickReply());

        this.dialogue.flex().relative(this.root).wh(0.5F, 1F);
        this.replies.flex().relative(this.dialogue).x(30).y(0.4F).w(1F, -60).h(0.6F, -30);

        this.root.add(this.dialogue, this.replies);

        this.setFragment(fragment);
    }

    private void setFragment(PacketDialogueFragment fragment)
    {
        List<String> replies = fragment.replies.stream()
            .map((reply) -> reply.text)
            .collect(Collectors.toList());

        this.fragment = fragment;
        this.replies.setList(replies);
        this.replies.setCurrent("");
        this.replies.scroll.scrollTo(0);
    }

    private void pickReply()
    {
        Dispatcher.sendToServer(new PacketPickReply(this.replies.getIndex()));
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
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        Gui.drawRect(0, 0, this.dialogue.area.mx(), this.dialogue.area.ey(), 0xaa000000);
        GuiDraw.drawHorizontalGradientRect(this.dialogue.area.mx(), 0, this.dialogue.area.ex(), this.dialogue.area.ey(), 0xaa000000, 0);

        super.drawScreen(mouseX, mouseY, partialTicks);

        this.drawCenteredString(this.fontRenderer, this.fragment.title, this.replies.area.mx(), 10, 0xffffff);
        GuiDraw.drawMultiText(this.fontRenderer, this.fragment.reaction.text, this.replies.area.x, 45, 0xffffff, this.replies.area.w, 12, 0.5F, 0);
    }
}