package mchorse.mappet.client.gui.triggers;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.api.triggers.blocks.AbstractTriggerBlock;
import mchorse.mappet.api.triggers.blocks.CommandTriggerBlock;
import mchorse.mappet.api.triggers.blocks.DialogueTriggerBlock;
import mchorse.mappet.api.triggers.blocks.EventTriggerBlock;
import mchorse.mappet.api.triggers.blocks.ItemTriggerBlock;
import mchorse.mappet.api.triggers.blocks.ScriptTriggerBlock;
import mchorse.mappet.api.triggers.blocks.SoundTriggerBlock;
import mchorse.mappet.client.gui.triggers.panels.GuiAbstractTriggerBlockPanel;
import mchorse.mappet.client.gui.triggers.panels.GuiCommandTriggerBlockPanel;
import mchorse.mappet.client.gui.triggers.panels.GuiDialogueTriggerBlockPanel;
import mchorse.mappet.client.gui.triggers.panels.GuiEventTriggerBlockPanel;
import mchorse.mappet.client.gui.triggers.panels.GuiItemTriggerBlockPanel;
import mchorse.mappet.client.gui.triggers.panels.GuiScriptTriggerBlockPanel;
import mchorse.mappet.client.gui.triggers.panels.GuiSoundTriggerBlockPanel;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mappet.client.gui.utils.overlays.GuiEditorOverlayPanel;
import mchorse.mappet.utils.Colors;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.list.GuiListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GuiTriggerOverlayPanel extends GuiEditorOverlayPanel<AbstractTriggerBlock>
{
    public static final Map<
            Class<? extends AbstractTriggerBlock>,
            Class<? extends GuiAbstractTriggerBlockPanel<? extends AbstractTriggerBlock>>>
        PANELS = new HashMap<
            Class<? extends AbstractTriggerBlock>,
            Class<? extends GuiAbstractTriggerBlockPanel<? extends AbstractTriggerBlock>>>();

    private Trigger trigger;

    static
    {
        PANELS.put(CommandTriggerBlock.class, GuiCommandTriggerBlockPanel.class);
        PANELS.put(SoundTriggerBlock.class, GuiSoundTriggerBlockPanel.class);
        PANELS.put(EventTriggerBlock.class, GuiEventTriggerBlockPanel.class);
        PANELS.put(DialogueTriggerBlock.class, GuiDialogueTriggerBlockPanel.class);
        PANELS.put(ScriptTriggerBlock.class, GuiScriptTriggerBlockPanel.class);
        PANELS.put(ItemTriggerBlock.class, GuiItemTriggerBlockPanel.class);
    }

    public GuiTriggerOverlayPanel(Minecraft mc, Trigger trigger)
    {
        super(mc, IKey.lang("mappet.gui.triggers.title"));

        this.trigger = trigger;

        this.list.sorting().setList(trigger.blocks);
        this.list.context(() ->
        {
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(this.mc).shadow();

            menu.action(Icons.ADD, IKey.lang("mappet.gui.triggers.context.add"), () ->
            {
                GuiSimpleContextMenu adds = new GuiSimpleContextMenu(this.mc).shadow();

                for (String key : CommonProxy.getTriggerBlocks().getKeys())
                {
                    IKey label = IKey.format("mappet.gui.triggers.context.add_trigger", IKey.lang("mappet.gui.trigger_types." + key));
                    int color = CommonProxy.getTriggerBlocks().getColor(key);

                    adds.action(Icons.ADD, label, () -> this.addBlock(key), color);
                }

                GuiBase.getCurrent().replaceContextMenu(adds);
            });

            if (!this.list.isDeselected())
            {
                menu.action(Icons.COPY, IKey.lang("mappet.gui.triggers.context.copy"), this::copyTrigger);
            }

            try
            {
                NBTTagCompound tag = JsonToNBT.getTagFromJson(GuiScreen.getClipboardString());

                if (tag.hasKey("_TriggerType"))
                {
                    menu.action(Icons.PASTE, IKey.lang("mappet.gui.triggers.context.paste"), () -> this.pasteTrigger(tag));
                }
            }
            catch (Exception e)
            {}

            if (!this.list.isDeselected())
            {
                menu.action(Icons.REMOVE, IKey.lang("mappet.gui.triggers.context.remove"), this::removeItem, Colors.NEGATIVE);
            }

            return menu;
        });

        this.pickItem(this.trigger.blocks.isEmpty() ? null : this.trigger.blocks.get(0), true);
    }

    @Override
    protected GuiListElement<AbstractTriggerBlock> createList(Minecraft mc)
    {
        return new GuiAbstractBlockListElement(mc, (l) -> this.pickItem(l.get(0), false));
    }

    private void addBlock(String type)
    {
        AbstractTriggerBlock block = CommonProxy.getTriggerBlocks().create(type);

        this.trigger.blocks.add(block);
        this.pickItem(block, true);
        this.list.update();
    }

    private void copyTrigger()
    {
        AbstractTriggerBlock block = this.list.getCurrentFirst();
        NBTTagCompound tag = block.serializeNBT();

        tag.setString("_TriggerType", CommonProxy.getTriggerBlocks().getType(block));
        GuiScreen.setClipboardString(tag.toString());
    }

    private void pasteTrigger(NBTTagCompound tag)
    {
        AbstractTriggerBlock block = CommonProxy.getTriggerBlocks().create(tag.getString("_TriggerType"));

        block.deserializeNBT(tag);
        this.trigger.blocks.add(block);
        this.list.update();

        this.pickItem(block, true);
    }

    @Override
    protected void fillData(AbstractTriggerBlock block)
    {
        this.editor.removeAll();

        try
        {
            this.editor.add((GuiAbstractTriggerBlockPanel) PANELS.get(block.getClass()).getConstructors()[0].newInstance(this.mc, this, block));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(GuiContext context)
    {
        super.draw(context);

        if (this.trigger.blocks.isEmpty())
        {
            GuiMappetUtils.drawRightClickHere(context, this.list.area);
        }
    }

    public static class GuiAbstractBlockListElement extends GuiListElement<AbstractTriggerBlock>
    {
        public GuiAbstractBlockListElement(Minecraft mc, Consumer<List<AbstractTriggerBlock>> callback)
        {
            super(mc, callback);
        }

        @Override
        protected void drawElementPart(AbstractTriggerBlock element, int i, int x, int y, boolean hover, boolean selected)
        {
            int color = CommonProxy.getTriggerBlocks().getColor(element);

            Gui.drawRect(x, y, x + 4, y + this.scroll.scrollItemSize, 0xff000000 + color);
            GuiDraw.drawHorizontalGradientRect(x + 4, y, x + 24, y + this.scroll.scrollItemSize, 0x44000000 + color, color);

            super.drawElementPart(element, i, x + 4, y, hover, selected);
        }

        @Override
        protected String elementToString(AbstractTriggerBlock element)
        {
            return element.stringify();
        }
    }
}