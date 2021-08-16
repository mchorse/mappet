package mchorse.mappet.client.gui.panels;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.scripts.Script;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.npc.utils.GuiNpcStatesOverlayPanel;
import mchorse.mappet.client.gui.scripts.GuiDocumentationOverlayPanel;
import mchorse.mappet.client.gui.scripts.GuiLibrariesOverlayPanel;
import mchorse.mappet.client.gui.scripts.GuiRepl;
import mchorse.mappet.client.gui.scripts.GuiTextEditor;
import mchorse.mappet.client.gui.scripts.utils.GuiItemStackOverlayPanel;
import mchorse.mappet.client.gui.scripts.utils.GuiMorphOverlayPanel;
import mchorse.mappet.client.gui.scripts.utils.GuiScriptSoundOverlayPanel;
import mchorse.mappet.client.gui.scripts.utils.SyntaxStyle;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mappet.client.gui.utils.overlays.GuiSoundOverlayPanel;
import mchorse.mappet.utils.MPIcons;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.Direction;
import mchorse.mclib.utils.RayTracing;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.util.MMIcons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.text.DecimalFormat;

public class GuiScriptPanel extends GuiMappetDashboardPanel<Script>
{
    public GuiIconElement toggleRepl;
    public GuiIconElement docs;
    public GuiIconElement libraries;
    public GuiIconElement run;
    public GuiTextEditor code;
    public GuiRepl repl;
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

    public static void openSoundPicker(GuiTextEditor editor)
    {
        GuiSoundOverlayPanel panel = new GuiScriptSoundOverlayPanel(Minecraft.getMinecraft(), editor);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), panel, 0.5F, 0.9F);
    }

    public GuiScriptPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.toggleRepl = new GuiIconElement(mc, MPIcons.REPL, (b) -> this.setRepl(!this.repl.isVisible()));
        this.toggleRepl.flex().relative(this.toggleSidebar).y(20);
        this.toggleRepl.tooltip(IKey.lang("mappet.gui.scripts.repl.title"), Direction.LEFT);
        this.docs = new GuiIconElement(mc, Icons.HELP, this::openDocumentation);
        this.docs.flex().relative(this.toggleRepl).y(20);
        this.docs.tooltip(IKey.lang("mappet.gui.scripts.documentation.title"), Direction.LEFT);
        this.libraries = new GuiIconElement(mc, Icons.MORE, this::openLibraries);
        this.libraries.flex().relative(this.docs).y(20);
        this.libraries.tooltip(IKey.lang("mappet.gui.scripts.libraries.tooltip"), Direction.LEFT);
        this.run = new GuiIconElement(mc, Icons.PLAY, this::runScript);
        this.run.flex().relative(this.libraries).y(20);
        this.run.tooltip(IKey.lang("mappet.gui.scripts.run"), Direction.LEFT);

        this.code = new GuiTextEditor(mc, null);
        this.code.background().context(() ->
        {
            /* These GUI QoL features are getting out of hand... */
            return new GuiSimpleContextMenu(this.mc)
                .action(Icons.POSE, IKey.lang("mappet.gui.scripts.context.paste_morph"), () -> openMorphPicker(this.code))
                .action(MMIcons.ITEM, IKey.lang("mappet.gui.scripts.context.paste_item"), () -> openItemPicker(this.code))
                .action(Icons.BLOCK, IKey.lang("mappet.gui.scripts.context.paste_player_pos"), () -> pastePlayerPosition(this.code))
                .action(Icons.VISIBLE, IKey.lang("mappet.gui.scripts.context.paste_block_pos"), () -> pasteBlockPosition(this.code))
                .action(Icons.SOUND, IKey.lang("mappet.gui.scripts.context.paste_sound"), () -> openSoundPicker(this.code));
        });

        this.repl = new GuiRepl(mc);

        this.unique = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.meta.unique"), (b) -> this.data.unique = b.isToggled());
        this.unique.flex().relative(this.sidebar).x(10).y(1F, -10).w(1F, -20).anchorY(1F);

        this.names.flex().hTo(this.unique.area, -5);

        this.code.flex().relative(this.editor).wh(1F, 1F);
        this.repl.flex().relative(this.editor).wh(1F, 1F);

        this.editor.add(this.code);
        this.sidebar.prepend(this.unique);

        this.toggleSidebar.removeFromParent();
        this.add(this.repl, this.toggleSidebar, this.toggleRepl, this.docs, this.libraries, this.run);

        this.fill(null);
    }

    private void openDocumentation(GuiIconElement element)
    {
        GuiDocumentationOverlayPanel panel = new GuiDocumentationOverlayPanel(this.mc);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), panel, 0.7F, 0.9F);
    }

    private void runScript(GuiIconElement element)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        this.save();
        this.save = false;
        
        player.sendChatMessage("/mp script exec " + player.getUniqueID().toString() + " " + this.data.getId());
    }

    private void openLibraries(GuiIconElement element)
    {
        GuiLibrariesOverlayPanel overlay = new GuiLibrariesOverlayPanel(this.mc, this.data);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.4F, 0.6F);
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
        this.updateButtons();

        if (data != null)
        {
            if (!this.code.getText().equals(data.code))
            {
                this.code.setText(data.code);
                this.setRepl(false);
            }

            this.unique.toggled(data.unique);
        }
    }

    private void updateButtons()
    {
        this.run.setVisible(this.data != null && this.allowed && this.code.isVisible());
        this.libraries.setVisible(this.data != null && this.allowed && this.code.isVisible());
    }

    private void setRepl(boolean showRepl)
    {
        this.repl.setVisible(showRepl);
        this.code.setVisible(!showRepl);
        this.updateButtons();
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

            this.repl.repl.getHighlighter().setStyle(style);
            this.repl.repl.resetHighlight();
        }
    }
}