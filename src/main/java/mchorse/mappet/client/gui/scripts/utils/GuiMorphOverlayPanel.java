package mchorse.mappet.client.gui.scripts.utils;

import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.scripts.GuiTextEditor;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlayPanel;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagString;

public class GuiMorphOverlayPanel extends GuiOverlayPanel
{
    public GuiButtonElement pick;
    public GuiButtonElement insert;

    private GuiTextEditor editor;
    private AbstractMorph morph;

    public GuiMorphOverlayPanel(Minecraft mc, IKey title, GuiTextEditor editor, AbstractMorph morph)
    {
        super(mc, title);

        this.editor = editor;
        this.morph = morph;

        this.pick = new GuiButtonElement(mc, IKey.lang("mappet.gui.scripts.overlay.pick_morph"), this::pickMorph);
        this.insert = new GuiButtonElement(mc, IKey.lang("mappet.gui.scripts.overlay.insert"), this::insert);

        GuiElement row = Elements.row(mc, 5, this.pick, this.insert);

        row.flex().relative(this.content).y(1F, -30).w(1F).h(20);
        this.content.add(row);
    }

    private void pickMorph(GuiButtonElement b)
    {
        GuiMappetDashboard.get(this.mc).openMorphMenu(this.getParent(), false, this.morph, this::setMorph);
    }

    private void setMorph(AbstractMorph morph)
    {
        this.morph = morph;
    }

    private void insert(GuiButtonElement b)
    {
        this.close();

        if (this.morph != null)
        {
            String nbt = this.morph.toNBT().toString();

            this.editor.pasteText(NBTTagString.quoteAndEscape(nbt));
        }
    }
}