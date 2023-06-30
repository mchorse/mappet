package mchorse.mappet.client.gui.scripts.scriptedItem;

import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.client.gui.triggers.GuiTriggerElement;
import mchorse.mappet.client.gui.utils.text.GuiText;
import mchorse.mappet.common.ScriptedItemProps;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.items.PacketScriptedItemInfo;
import mchorse.mappet.utils.NBTUtils;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.list.GuiLabelListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.Label;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

public class GuiScriptedItemScreen extends GuiBase
{
    public GuiFormattedTextElement title;
    public GuiElement lore;
    public GuiIconElement addLore;

    public GuiScrollElement editor;

    public GuiLabelListElement<String> triggers;

    public GuiTriggerElement trigger;

    private String lastTrigger = "interact_with_air";

    private final ItemStack stack;
    private final ScriptedItemProps props;

    public GuiScriptedItemScreen(Minecraft mc, ItemStack stack)
    {
        this.stack = stack;
        this.props = NBTUtils.getScriptedItemProps(stack);

        /* Labels */
        GuiLabel triggersLabel = Elements.label(IKey.lang("mappet.gui.scripted_item.title")).anchor(0, 0.5F).background();
        GuiLabel titleLabel = Elements.label(IKey.lang("mappet.gui.scripted_item.item_title")).anchor(0, 0.5F).background();
        GuiLabel loreLabel = Elements.label(IKey.lang("mappet.gui.scripted_item.item_lore")).anchor(0, 0.5F).background();

        /* UI components */
        this.title = new GuiFormattedTextElement(mc, stack::setStackDisplayName);
        this.title.flex().relative(this.viewport).x(10).w(0.5F, -20).y(35).h(32);

        this.lore = Elements.column(mc, 5);
        this.lore.flex().relative(loreLabel).y(1F, 8).w(1F).hTo(this.viewport, 1F, -10);

        this.addLore = new GuiIconElement(mc, Icons.ADD, (b) -> this.addLore(mc, ""));
        this.addLore.flex().relative(loreLabel).x(1F, 3).y(-3).anchorX(1F);
        this.addLore.tooltip(IKey.lang("mappet.gui.scripted_item.item_lore_add"));

        this.editor = new GuiScrollElement(mc);
        this.editor.flex().relative(this.viewport).x(0.5F).y(281).w(0.5F).h(1F, -311).column(5).scroll().stretch().padding(10);

        this.triggers = new GuiLabelListElement<>(mc, (l) -> this.fillTrigger(mc, l.get(0), false));
        this.triggers.background().flex().relative(this.viewport).x(0.5F, 10).y(35).w(0.5F, -20).h(246);

        this.trigger = new GuiTriggerElement(mc).onClose(this::updateCurrentTrigger);
        this.trigger.flex().relative(this.viewport).x(1F, -10).y(1F, -10).wh(120, 20).anchor(1F, 1F);

        triggersLabel.flex().relative(this.viewport).x(0.5F, 10).y(10).wh(120, 20);
        titleLabel.flex().relative(this.viewport).x(10).y(10).wh(120, 20);
        loreLabel.flex().relative(this.title).y(1F, 25).w(1F).h( 20);

        this.fill(mc);

        this.root.add(this.title);
        this.root.add(this.triggers, this.editor, this.trigger, triggersLabel, titleLabel, loreLabel, this.lore, this.addLore);
    }

    private void addLore(Minecraft mc, String lore)
    {
        GuiFormattedTextElement textElement = new GuiFormattedTextElement(mc, null);

        textElement.text.setText(lore);
        textElement.context(() ->
        {
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(this.mc);

            menu.action(Icons.REMOVE, IKey.lang("mappet.gui.scripted_item.item_lore_remove"), () ->
            {
                textElement.removeFromParent();
                this.lore.resize();
            });

            return menu;
        });

        textElement.flex().h(32);

        this.lore.add(textElement);
        this.lore.resize();
    }

    private void fillTrigger(Minecraft mc, Label<String> trigger, boolean select)
    {
        this.editor.removeAll();
        this.editor.add(new GuiText(mc).text(IKey.lang("mappet.gui.scripted_item." + trigger.value)));
        this.editor.add(new GuiText(mc).text(IKey.lang("mappet.gui.scripted_item.descriptions." + trigger.value)));

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

    public void fill(Minecraft mc)
    {
        this.triggers.clear();

        for (String key : this.props.registered.keySet())
        {
            this.triggers.add(this.createTooltip(key, this.props.registered.get(key)), key);
        }

        this.triggers.sort();
        this.triggers.setCurrentValue(this.lastTrigger);

        this.fillTrigger(mc, this.triggers.getCurrentFirst(), true);

        this.triggers.resize();

        /* Fill display name and lore */
        this.title.text.setText(this.stack.getDisplayName());

        NBTTagCompound tag = this.stack.getTagCompound();

        if (tag != null && tag.hasKey("display", Constants.NBT.TAG_COMPOUND) && tag.getCompoundTag("display").hasKey("Lore", Constants.NBT.TAG_LIST))
        {
            NBTTagList lore = tag.getCompoundTag("display").getTagList("Lore", Constants.NBT.TAG_STRING);

            for (int i = 0; i < lore.tagCount(); i++)
            {
                this.addLore(mc, lore.getStringTagAt(i));
            }
        }
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

        // Reset 'pickedUp' property to false each time properties are edited
        this.props.pickedUp = false;

        NBTTagList lore = new NBTTagList();

        for (GuiFormattedTextElement element : this.lore.getChildren(GuiFormattedTextElement.class))
        {
            String text = element.text.getText();

            if (!text.isEmpty())
            {
                lore.appendTag(new NBTTagString(text));
            }
        }

        this.stack.getOrCreateSubCompound("display").setTag("Lore", lore);

        if (this.title.text.getText().trim().isEmpty())
        {
            this.stack.getOrCreateSubCompound("display").removeTag("Name");
        }

        Dispatcher.sendToServer(new PacketScriptedItemInfo(this.props.toNBT(), this.stack.getTagCompound(), 0));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}