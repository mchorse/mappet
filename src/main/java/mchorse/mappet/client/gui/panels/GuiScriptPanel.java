package mchorse.mappet.client.gui.panels;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.scripts.Script;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.scripts.GuiReplPanel;
import mchorse.mappet.client.gui.scripts.GuiTextEditor;
import mchorse.mappet.client.gui.scripts.utils.GuiItemStackOverlayPanel;
import mchorse.mappet.client.gui.scripts.utils.GuiMorphOverlayPanel;
import mchorse.mappet.client.gui.scripts.utils.SyntaxStyle;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mappet.utils.MPIcons;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiPanelBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.RayTracing;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.util.MMIcons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.text.DecimalFormat;

public class GuiScriptPanel extends GuiMappetDashboardPanel<Script>
{
    public GuiPanelBase<GuiElement> panels;
    public GuiTextEditor code;
    public GuiReplPanel replBlock;
    public GuiToggleElement unique;

    public static void openMorphPicker(GuiTextEditor editor)
    {
        AbstractMorph morph = null;
        NBTTagCompound tag = readFromSelected(editor);

        if (editor.isSelected())
        {
            morph = MorphManager.INSTANCE.morphFromNBT(tag);
        }

        GuiOverlay.addOverlay(GuiBase.getCurrent(), new GuiMorphOverlayPanel(Minecraft.getMinecraft(), IKey.lang("mappet.gui.scripts.overlay.title_morph"), editor, morph), 240, 54);
    }

    public static void openItemPicker(GuiTextEditor editor)
    {
        ItemStack stack = ItemStack.EMPTY;
        NBTTagCompound tag = readFromSelected(editor);

        if (tag != null)
        {
            stack = new ItemStack(tag);
        }

        GuiOverlay.addOverlay(GuiBase.getCurrent(), new GuiItemStackOverlayPanel(Minecraft.getMinecraft(), IKey.lang("mappet.gui.scripts.overlay.title_item"), editor, stack), 240, 54);
    }

    public static NBTTagCompound readFromSelected(GuiTextEditor editor)
    {
        if (editor.isSelected())
        {
            NBTTagCompound tag = null;

            try
            {
                tag = JsonToNBT.getTagFromJson("{String:" + editor.getSelectedText() + "}");
                tag = JsonToNBT.getTagFromJson(tag.getString("String"));
            }
            catch (Exception e)
            {}

            return tag;
        }

        return null;
    }

    public static void pastePlayerPosition(GuiTextEditor editor)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        DecimalFormat format = GuiTrackpadElement.FORMAT;

        editor.pasteText(format.format(player.posX) + ", " + format.format(player.posY) + ", " + format.format(player.posZ));
    }

    public static void pasteBlockPosition(GuiTextEditor editor)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        DecimalFormat format = GuiTrackpadElement.FORMAT;
        RayTraceResult result = RayTracing.rayTrace(player, 128, 0F);

        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            BlockPos pos = result.getBlockPos();

            editor.pasteText(format.format(pos.getX()) + ", " + format.format(pos.getY()) + ", " + format.format(pos.getZ()));
        }
    }

    public GuiScriptPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.code = new GuiTextEditor(mc, null);
        this.code.background().context(() ->
        {
            /* These GUI QoL features are getting out of hand... */
            return new GuiSimpleContextMenu(this.mc)
                .action(Icons.POSE, IKey.lang("mappet.gui.scripts.context.paste_morph"), () -> openMorphPicker(this.code))
                .action(MMIcons.ITEM, IKey.lang("mappet.gui.scripts.context.paste_item"), () -> openItemPicker(this.code))
                .action(Icons.BLOCK, IKey.lang("mappet.gui.scripts.context.paste_player_pos"), () -> pastePlayerPosition(this.code))
                .action(Icons.VISIBLE, IKey.lang("mappet.gui.scripts.context.paste_block_pos"), () -> pasteBlockPosition(this.code));
        });

        this.replBlock = new GuiReplPanel(mc);

        this.unique = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.meta.unique"), (b) -> this.data.unique = b.isToggled());
        this.unique.flex().relative(this.sidebar).x(10).y(1F, -10).w(1F, -20).anchorY(1F);

        this.names.flex().hTo(this.unique.area, -5);

        this.panels = new GuiPanelBase<GuiElement>(mc)
        {
            @Override
            protected void drawBackground(GuiContext context, int x, int y, int w, int h)
            {
                Gui.drawRect(x, y, x + w, y + h, 0xff080808);
            }
        };
        this.panels.registerPanel(this.code, IKey.lang("mappet.gui.scripts.panels.script"), MMIcons.PROPERTIES);
        this.panels.registerPanel(this.replBlock, IKey.lang("mappet.gui.scripts.panels.repl"), MPIcons.REPL);
        this.panels.flex().relative(this.editor).wh(1F, 1F);

        this.editor.add(this.panels);
        this.sidebar.prepend(this.unique);

        this.fill(null);
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
            if (!this.code.getText().equals(data.code))
            {
                this.code.setText(data.code);
                this.panels.setPanel(this.code);
            }

            this.unique.toggled(data.unique);
        }
    }

    @Override
    protected void preSave()
    {
        this.data.code = this.code.getText();
    }

    @Override
    public void open()
    {
        super.open();

        SyntaxStyle style = Mappet.scriptEditorSyntaxStyle.get();

        if (this.code.getHighlighter().getStyle() != style)
        {
            this.code.getHighlighter().setStyle(style);
            this.code.resetHighlight();

            this.replBlock.repl.getHighlighter().setStyle(style);
            this.replBlock.repl.resetHighlight();
        }
    }
}