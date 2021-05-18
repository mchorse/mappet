package mchorse.mappet.client.gui.nodes.dialogues;

import mchorse.mappet.ClientProxy;
import mchorse.mappet.api.dialogues.nodes.QuestChainNode;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.nodes.GuiEventNodePanel;
import mchorse.mappet.client.gui.utils.overlays.GuiContentNamesOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiQuestChainNodePanel extends GuiEventNodePanel<QuestChainNode>
{
    public GuiButtonElement questChain;

    public GuiQuestChainNodePanel(Minecraft mc)
    {
        super(mc);

        this.questChain = new GuiButtonElement(mc, IKey.lang("mappet.gui.overlays.chain"), (b) -> this.openQuestChains());

        this.add(this.questChain);
    }

    private void openQuestChains()
    {
        ClientProxy.requestNames(ContentType.CHAINS, (names) ->
        {
            GuiContentNamesOverlayPanel overlay = new GuiContentNamesOverlayPanel(this.mc, IKey.lang("mappet.gui.overlays.chain"), ContentType.CHAINS, names, (name) -> this.node.chain = name);

            overlay.set(this.node.chain);
            GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.5F, 0.7F);
        });
    }
}