package mchorse.mappet.client.gui;

import mchorse.mappet.client.gui.triggers.GuiTriggerElement;
import mchorse.mappet.client.gui.utils.text.GuiText;
import mchorse.mappet.common.ScriptedItemProps;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.items.PacketScriptedItemInfo;
import mchorse.mappet.utils.NBTUtils;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.ScrollDirection;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiScriptedItemScreen extends GuiBase {
    private ScriptedItemProps props;

    private List<GuiTriggerElement> triggerElements;
    private List<String> triggerLabels;
    private List<String> contextLabels;

    public GuiScriptedItemScreen(ItemStack stack) {
        super();

        // Initialize properties and store initial state
        this.props = NBTUtils.getScriptedItemProps(stack);
        this.props.storeInitialState();

        // Initialize the scrolling element
        Minecraft mc = Minecraft.getMinecraft();
        GuiScrollElement scroll = new GuiScrollElement(mc, ScrollDirection.VERTICAL);
        scroll.flex().relative(this.root).w(1F).h(1F);
        scroll.scroll.scrollSpeed *= 2;
        scroll.flex().column(5).scroll().width(180).padding(15);

        // Initialize context labels
        this.contextLabels = Arrays.asList(
                "mappet.gui.scripted_item.descriptions.interact_with_air",
                "mappet.gui.scripted_item.descriptions.interact_with_entity",
                "mappet.gui.scripted_item.descriptions.interact_with_block",
                "mappet.gui.scripted_item.descriptions.attack_entity",
                "mappet.gui.scripted_item.descriptions.break_block",
                "mappet.gui.scripted_item.descriptions.place_block",
                "mappet.gui.scripted_item.descriptions.hit_block",
                "mappet.gui.scripted_item.descriptions.on_holder_tick",
                "mappet.gui.scripted_item.descriptions.pickup"
        );

        // Initialize triggers
        this.triggerElements = Arrays.asList(
                new GuiTriggerElement(mc, props.interactWithAir),
                new GuiTriggerElement(mc, props.interactWithEntity),
                new GuiTriggerElement(mc, props.interactWithBlock),
                new GuiTriggerElement(mc, props.attackEntity),
                new GuiTriggerElement(mc, props.breakBlock),
                new GuiTriggerElement(mc, props.placeBlock),
                new GuiTriggerElement(mc, props.hitBlock),
                new GuiTriggerElement(mc, props.onHolderTick),
                new GuiTriggerElement(mc, props.pickup)
        );

        // Initialize trigger labels
        this.triggerLabels = Arrays.asList(
                "mappet.gui.scripted_item.interact_with_air",
                "mappet.gui.scripted_item.interact_with_entity",
                "mappet.gui.scripted_item.interact_with_block",
                "mappet.gui.scripted_item.attack_entity",
                "mappet.gui.scripted_item.break_block",
                "mappet.gui.scripted_item.place_block",
                "mappet.gui.scripted_item.hit_block",
                "mappet.gui.scripted_item.on_holder_tick",
                "mappet.gui.scripted_item.pickup"
        );

        for (int i = 0; i < triggerElements.size(); i++) {
            GuiTriggerElement triggerElement = triggerElements.get(i);
            String triggerLabel = triggerLabels.get(i);
            String contextLabel = contextLabels.get(i);

            // Create label and text elements
            GuiElement triggerLabelElement = Elements.label(IKey.lang(triggerLabel)).background().marginBottom(6).marginTop(24);
            GuiText contextTextElement = new GuiText(mc).text(IKey.lang(contextLabel));

            // Add the trigger, trigger label, and context data text to the scroll panel
            scroll.add(triggerLabelElement, triggerElement, contextTextElement);
        }

        // Add the scroll element to the root
        this.root.add(scroll);
    }


    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    protected void closeScreen()
    {
        super.closeScreen();
        if (this.props.hasChanged()) {
            Dispatcher.sendToServer(new PacketScriptedItemInfo(this.props.toNBT(), 0));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        String title = I18n.format("mappet.gui.scripted_item.title");

        GuiDraw.drawTextBackground(this.fontRenderer, title, this.viewport.x + 15, this.viewport.y + 10, 0xffffff, ColorUtils.HALF_BLACK);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}