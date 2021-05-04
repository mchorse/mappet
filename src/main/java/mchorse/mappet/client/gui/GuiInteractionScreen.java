package mchorse.mappet.client.gui;

import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.api.dialogues.DialogueFragment;
import mchorse.mappet.client.gui.crafting.GuiCrafting;
import mchorse.mappet.client.gui.crafting.ICraftingScreen;
import mchorse.mappet.client.gui.utils.GuiClickableText;
import mchorse.mappet.client.gui.utils.GuiMorphRenderer;
import mchorse.mappet.client.gui.utils.GuiText;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.crafting.PacketCraftingTable;
import mchorse.mappet.network.common.dialogue.PacketDialogueFragment;
import mchorse.mappet.network.common.dialogue.PacketPickReply;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDrawable;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.metamorph.api.MorphManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;

public class GuiInteractionScreen extends GuiBase implements ICraftingScreen
{
    public GuiElement area;

    public GuiMorphRenderer morph;

    /* Dialogue */
    public GuiScrollElement reaction;
    public GuiText reactionText;
    public GuiScrollElement replies;

    private PacketDialogueFragment fragment;

    /* Crafting */
    public GuiCrafting crafting;
    public GuiButtonElement back;

    private CraftingTable table;

    public GuiInteractionScreen(PacketDialogueFragment fragment)
    {
        super();

        Minecraft mc = Minecraft.getMinecraft();

        /* General */
        this.morph = new GuiMorphRenderer(mc);
        this.morph.flex().relative(this.viewport).x(0.4F).w(0.6F).h(1F);

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

        this.back = new GuiButtonElement(mc, IKey.str("Back"), (b) -> Dispatcher.sendToServer(new PacketPickReply(-1)));
        this.back.flex().relative(this.crafting).x(10).y(1F, -10).wh(80, 20).anchorY(1F);

        this.crafting.add(this.back);

        GuiDrawable drawable = new GuiDrawable((context) ->
        {
            Gui.drawRect(0, 0, this.area.area.x(0.65F), this.area.area.ey(), 0xaa000000);
            GuiDraw.drawHorizontalGradientRect(this.area.area.x(0.65F), 0, this.area.area.x(1.125F), this.area.area.ey(), 0xaa000000, 0);
        });

        this.area.add(this.crafting, this.replies, this.reaction);
        this.root.add(this.morph, drawable, this.area);
        this.reaction.add(this.reactionText);

        this.setFragment(fragment);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void updateVisibility()
    {
        if (this.table != null)
        {
            this.reaction.flex().hTo(this.crafting.area);

            this.crafting.setVisible(true);
            this.replies.setVisible(false);
        }
        else
        {
            this.reaction.flex().hTo(this.replies.area);

            this.crafting.setVisible(false);
            this.replies.setVisible(true);
        }
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

        this.reactionText.text(fragment.reaction.text.replace("\\n", "\n"));
        this.replies.removeAll();

        for (DialogueFragment reply : fragment.replies)
        {
            GuiClickableText replyElement = new GuiClickableText(Minecraft.getMinecraft());

            replyElement.callback(this::pickReply);
            this.replies.add(replyElement.text("> " + reply.text.replace("\\n", "\n")).hoverColor(0xffffa0));
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
        if (fragment.table != null)
        {
            this.setFragment(fragment);
            this.set(fragment.table);
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

    public void set(CraftingTable table)
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

        int y = this.table != null ? this.crafting.area.y - 1 : this.replies.area.y - 1;
        GuiDraw.drawHorizontalGradientRect(this.replies.area.x - 20, y, this.replies.area.mx(), y + 1, 0, 0x88ffffff);
        GuiDraw.drawHorizontalGradientRect(this.replies.area.mx(), y, this.replies.area.ex() + 20, y + 1, 0x88ffffff, 0);

        this.drawCenteredString(this.fontRenderer, this.fragment.title, this.reaction.area.mx(), 10, 0xffffff);
    }
}