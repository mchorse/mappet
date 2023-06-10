package mchorse.mappet.client.gui.scripts.scriptedItem;

import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.client.gui.triggers.GuiTriggerElement;
import mchorse.mappet.client.gui.utils.text.GuiText;
import mchorse.mappet.common.ScriptedItemProps;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.items.PacketScriptedItemInfo;
import mchorse.mappet.utils.NBTUtils;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiLabelListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Label;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class GuiScriptedItemScreen extends GuiBase
{
    public GuiScrollElement editor;

    public GuiLabelListElement<String> triggers;

    public GuiTriggerElement trigger;

    private String lastTrigger = "interact_with_air";

    private final ScriptedItemProps props;

    private final Minecraft mc;
    public GuiScriptedItemScreen(Minecraft mc, ItemStack stack)
    {
        this.mc = mc;

        GuiLabel triggersLabel = Elements.label(IKey.lang("mappet.gui.scripted_item.title")).anchor(0, 0.5F).background();

        triggersLabel.flex().relative(this.viewport).x(0.5F, 10).y(10).wh(120, 20);

        this.editor = new GuiScrollElement(mc);
        this.editor.flex().relative(this.viewport).x(0.5F).y(281).w(0.5F).h(1F, -311).column(5).scroll().stretch().padding(10);

        this.triggers = new GuiLabelListElement<>(mc, (l) -> this.fillTrigger(l.get(0), false));
        this.triggers.background().flex().relative(this.viewport).x(0.5F, 10).y(35).w(0.5F, -20).h(246);

        this.trigger = new GuiTriggerElement(mc).onClose(this::updateCurrentTrigger);
        this.trigger.flex().relative(this.viewport).x(1F, -10).y(1F, -10).wh(120, 20).anchor(1F, 1F);

        // Initialize properties
        this.props = NBTUtils.getScriptedItemProps(stack);

        this.fill();

        this.root.add(this.triggers, this.editor, this.trigger, triggersLabel);
    }

    private void fillTrigger(Label<String> trigger, boolean select)
    {
        this.editor.removeAll();
        this.editor.add(new GuiText(this.mc).text(IKey.lang("mappet.gui.scripted_item." + trigger.value)));
        this.editor.add(new GuiText(this.mc).text(IKey.lang("mappet.gui.scripted_item.descriptions." + trigger.value)));

        this.trigger.set(this.props.registered.get(trigger.value));

        if (select)
        {
            this.triggers.setCurrentScroll(trigger);
        }

        this.lastTrigger = trigger.value;

        this.editor.resize();
    }

    private void updateCurrentTrigger()
    {
        Trigger trigger = this.props.registered.get(this.lastTrigger);

        this.triggers.getCurrentFirst().title = this.createTooltip(this.lastTrigger, trigger);
    }

    public IKey createTooltip(String key, Trigger trigger)
    {
        IKey title = IKey.lang("mappet.gui.scripted_item." + key);

        if (trigger.blocks.isEmpty())
        {
            return title;
        }

        IKey count = IKey.str(" §7(§6" + trigger.blocks.size() + "§7)§r");

        return IKey.comp(title, count);
    }

    public void fill()
    {
        this.triggers.clear();

        for (String key : this.props.registered.keySet())
        {
            this.triggers.add(this.createTooltip(key, this.props.registered.get(key)), key);
        }

        this.triggers.sort();
        this.triggers.setCurrentValue(this.lastTrigger);

        this.fillTrigger(this.triggers.getCurrentFirst(), true);

        this.triggers.resize();
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

        Dispatcher.sendToServer(new PacketScriptedItemInfo(this.props.toNBT(), 0));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}