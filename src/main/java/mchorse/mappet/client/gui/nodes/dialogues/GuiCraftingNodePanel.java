package mchorse.mappet.client.gui.nodes.dialogues;

import mchorse.mappet.ClientProxy;
import mchorse.mappet.api.dialogues.nodes.CraftingNode;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.nodes.GuiEventBaseNodePanel;
import mchorse.mappet.client.gui.panels.GuiMappetDashboardPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiContentNamesOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiCraftingNodePanel extends GuiEventBaseNodePanel<CraftingNode>
{
    public GuiButtonElement crafting;

    public GuiCraftingNodePanel(Minecraft mc, GuiMappetDashboardPanel parentPanel)
    {
        super(mc, parentPanel);

        this.crafting = new GuiButtonElement(mc, IKey.lang("mappet.gui.overlays.crafting"), (b) -> this.openCraftingTables());

        this.add(this.crafting);
    }

    private void openCraftingTables()
    {
        ClientProxy.requestNames(ContentType.CRAFTING_TABLE, (names) ->
        {
            GuiContentNamesOverlayPanel overlay = new GuiContentNamesOverlayPanel(this.mc, IKey.lang("mappet.gui.overlays.crafting"), ContentType.CRAFTING_TABLE, names, (name) -> this.node.table = name);

            overlay.set(this.node.table);
            GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.5F, 0.7F);
        });
    }
}