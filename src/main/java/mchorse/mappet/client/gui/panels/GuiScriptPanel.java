package mchorse.mappet.client.gui.panels;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.scripts.Script;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.scripts.GuiTextEditor;
import mchorse.mappet.client.gui.scripts.utils.GuiItemStackOverlayPanel;
import mchorse.mappet.client.gui.scripts.utils.GuiMorphOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.RayTracing;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.util.MMIcons;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.text.DecimalFormat;

public class GuiScriptPanel extends GuiMappetDashboardPanel<Script>
{
    public GuiTextEditor code;
    public GuiToggleElement unique;

    public GuiScriptPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.code = new GuiTextEditor(mc, null);
        this.code.background().flex().relative(this.editor).wh(1F, 1F);
        this.code.context(() ->
        {
            /* These GUI QoL features are getting out of hand... */
            return new GuiSimpleContextMenu(this.mc)
                .action(Icons.POSE, IKey.lang("mappet.gui.scripts.context.paste_morph"), this::openMorphPicker)
                .action(MMIcons.ITEM, IKey.lang("mappet.gui.scripts.context.paste_item"), this::openItemPicker)
                .action(Icons.BLOCK, IKey.lang("mappet.gui.scripts.context.paste_player_pos"), this::pastePlayerPosition)
                .action(Icons.VISIBLE, IKey.lang("mappet.gui.scripts.context.paste_block_pos"), this::pasteBlockPosition);
        });

        this.unique = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.meta.unique"), (b) -> this.data.unique = b.isToggled());
        this.unique.flex().relative(this.sidebar).x(10).y(1F, -10).w(1F, -20).anchorY(1F);

        this.names.flex().hTo(this.unique.area, -5);

        this.editor.add(this.code);
        this.sidebar.prepend(this.unique);

        this.fill(null);
    }

    private void openMorphPicker()
    {
        AbstractMorph morph = null;
        NBTTagCompound tag = this.readFromSelected();

        if (this.code.isSelected())
        {
            morph = MorphManager.INSTANCE.morphFromNBT(tag);
        }

        GuiOverlay.addOverlay(GuiBase.getCurrent(), new GuiMorphOverlayPanel(this.mc, IKey.lang("mappet.gui.scripts.overlay.title_morph"), this.code, morph), 240, 54);
    }

    private void openItemPicker()
    {
        ItemStack stack = ItemStack.EMPTY;
        NBTTagCompound tag = this.readFromSelected();

        if (tag != null)
        {
            stack = new ItemStack(tag);
        }

        GuiOverlay.addOverlay(GuiBase.getCurrent(), new GuiItemStackOverlayPanel(this.mc, IKey.lang("mappet.gui.scripts.overlay.title_item"), this.code, stack), 240, 54);
    }

    private NBTTagCompound readFromSelected()
    {
        if (this.code.isSelected())
        {
            NBTTagCompound tag = null;

            try
            {
                tag = JsonToNBT.getTagFromJson("{String:" + this.code.getSelectedText() + "}");
                tag = JsonToNBT.getTagFromJson(tag.getString("String"));
            }
            catch (Exception e)
            {}

            return tag;
        }

        return null;
    }

    private void pastePlayerPosition()
    {
        EntityPlayer player = this.mc.player;
        DecimalFormat format = GuiTrackpadElement.FORMAT;

        this.code.pasteText(format.format(player.posX) + ", " + format.format(player.posY) + ", " + format.format(player.posZ));
    }

    private void pasteBlockPosition()
    {
        EntityPlayer player = this.mc.player;
        DecimalFormat format = GuiTrackpadElement.FORMAT;
        RayTraceResult result = RayTracing.rayTrace(player, 128, 0F);

        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            BlockPos pos = result.getBlockPos();

            this.code.pasteText(format.format(pos.getX()) + ", " + format.format(pos.getY()) + ", " + format.format(pos.getZ()));
        }
    }

    @Override
    public ContentType getType()
    {
        return ContentType.SCRIPTS;
    }

    @Override
    public String getTitle()
    {
        return "mappet.gui.panels.scripts";
    }

    @Override
    protected void fillDefaultData(Script data)
    {
        super.fillDefaultData(data);

        data.code = "function main(c)\n{\n    // Code...\n    var s = c.getSubject();\n}";
    }

    @Override
    public void fill(Script data, boolean allowed)
    {
        super.fill(data, allowed);

        this.editor.setVisible(data != null);
        this.unique.setVisible(data != null && allowed);

        if (data != null)
        {
            this.code.getHighlighter().setStyle(Mappet.scriptEditorSyntaxStyle.get());

            if (!this.code.getText().equals(data.code))
            {
                this.code.setText(data.code);
            }
            else
            {
                this.code.resetHighlight();
            }

            this.unique.toggled(data.unique);
        }
    }

    @Override
    protected void preSave()
    {
        this.data.code = this.code.getText();
    }
}