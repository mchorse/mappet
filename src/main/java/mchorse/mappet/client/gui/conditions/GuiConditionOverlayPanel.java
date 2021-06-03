package mchorse.mappet.client.gui.conditions;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.api.conditions.Condition;
import mchorse.mappet.api.conditions.blocks.AbstractBlock;
import mchorse.mappet.api.conditions.blocks.ConditionBlock;
import mchorse.mappet.api.conditions.blocks.DialogueBlock;
import mchorse.mappet.api.conditions.blocks.FactionBlock;
import mchorse.mappet.api.conditions.blocks.ItemBlock;
import mchorse.mappet.api.conditions.blocks.QuestBlock;
import mchorse.mappet.api.conditions.blocks.StateBlock;
import mchorse.mappet.api.conditions.blocks.WorldTimeBlock;
import mchorse.mappet.client.gui.conditions.blocks.GuiAbstractBlockPanel;
import mchorse.mappet.client.gui.conditions.blocks.GuiConditionBlockPanel;
import mchorse.mappet.client.gui.conditions.blocks.GuiDialogueBlockPanel;
import mchorse.mappet.client.gui.conditions.blocks.GuiFactionBlockPanel;
import mchorse.mappet.client.gui.conditions.blocks.GuiItemBlockPanel;
import mchorse.mappet.client.gui.conditions.blocks.GuiQuestBlockPanel;
import mchorse.mappet.client.gui.conditions.blocks.GuiStateBlockPanel;
import mchorse.mappet.client.gui.conditions.blocks.GuiWorldTimeBlockPanel;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mappet.client.gui.utils.overlays.GuiEditorOverlayPanel;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.list.GuiListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.framework.elements.utils.GuiInventoryElement;
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

public class GuiConditionOverlayPanel extends GuiEditorOverlayPanel<AbstractBlock>
{
    public static final Map<
            Class<? extends AbstractBlock>,
            Class<? extends GuiAbstractBlockPanel<? extends AbstractBlock>>>
        PANELS = new HashMap<
            Class<? extends AbstractBlock>,
            Class<? extends GuiAbstractBlockPanel<? extends AbstractBlock>>>();

    public GuiInventoryElement inventory;

    private Condition condition;

    static
    {
        PANELS.put(QuestBlock.class, GuiQuestBlockPanel.class);
        PANELS.put(StateBlock.class, GuiStateBlockPanel.class);
        PANELS.put(DialogueBlock.class, GuiDialogueBlockPanel.class);
        PANELS.put(FactionBlock.class, GuiFactionBlockPanel.class);
        PANELS.put(ItemBlock.class, GuiItemBlockPanel.class);
        PANELS.put(WorldTimeBlock.class, GuiWorldTimeBlockPanel.class);
        PANELS.put(ConditionBlock.class, GuiConditionBlockPanel.class);
    }

    public GuiConditionOverlayPanel(Minecraft mc, Condition condition)
    {
        super(mc, IKey.lang("mappet.gui.conditions.title"));

        this.condition = condition;

        this.list.setList(condition.blocks);
        this.list.context(() ->
        {
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(this.mc).shadow();

            menu.action(Icons.ADD, IKey.lang("mappet.gui.conditions.context.add"), () ->
            {
                GuiSimpleContextMenu adds = new GuiSimpleContextMenu(this.mc).shadow();

                for (String key : CommonProxy.getConditionBlocks().getKeys())
                {
                    IKey label = IKey.format("mappet.gui.conditions.context.add_condition", IKey.lang("mappet.gui.condition_types." + key));
                    int color = CommonProxy.getConditionBlocks().getColor(key);

                    adds.action(Icons.ADD, label, () -> this.addBlock(key), color);
                }

                GuiBase.getCurrent().replaceContextMenu(adds);
            });

            if (!this.list.isDeselected())
            {
                menu.action(Icons.COPY, IKey.lang("mappet.gui.conditions.context.copy"), this::copyCondition);
            }

            try
            {
                NBTTagCompound tag = JsonToNBT.getTagFromJson(GuiScreen.getClipboardString());

                menu.action(Icons.PASTE, IKey.lang("mappet.gui.conditions.context.paste"), () -> this.pasteCondition(tag));
            }
            catch (Exception e)
            {}

            if (!this.list.isDeselected())
            {
                menu.action(Icons.REMOVE, IKey.lang("mappet.gui.conditions.context.remove"), this::removeItem, 0xff0022);
            }

            return menu;
        });
        this.inventory = new GuiInventoryElement(mc, (stack) ->
        {
            this.inventory.linked.acceptStack(stack);
            this.inventory.unlink();
        });
        this.inventory.flex().relative(this).xy(0.5F, 0.5F).anchor(0.5F, 0.5F);
        this.inventory.setVisible(false);

        this.content.add(this.inventory);

        this.pickItem(this.condition.blocks.isEmpty() ? null : this.condition.blocks.get(0), true);
    }

    @Override
    protected GuiListElement<AbstractBlock> createList(Minecraft mc)
    {
        return new GuiAbstractBlockListElement(mc, (l) -> this.pickItem(l.get(0), false));
    }

    private void addBlock(String type)
    {
        AbstractBlock block = CommonProxy.getConditionBlocks().create(type);

        this.condition.blocks.add(block);
        this.pickItem(block, true);
        this.list.update();
    }

    private void copyCondition()
    {
        AbstractBlock block = this.list.getCurrentFirst();
        NBTTagCompound tag = block.serializeNBT();

        tag.setString("Type", CommonProxy.getConditionBlocks().getType(block));
        GuiScreen.setClipboardString(tag.toString());
    }

    private void pasteCondition(NBTTagCompound tag)
    {
        AbstractBlock block = CommonProxy.getConditionBlocks().create(tag.getString("Type"));

        block.deserializeNBT(tag);
        this.condition.blocks.add(block);
        this.list.update();

        this.pickItem(block, true);
    }

    @Override
    protected void fillData(AbstractBlock block)
    {
        this.editor.removeAll();

        try
        {
            this.editor.add((GuiAbstractBlockPanel) PANELS.get(block.getClass()).getConstructors()[0].newInstance(this.mc, this, block));
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

        if (this.condition.blocks.isEmpty())
        {
            GuiMappetUtils.drawRightClickHere(context, this.list.area);
        }
    }

    public static class GuiAbstractBlockListElement extends GuiListElement<AbstractBlock>
    {
        public GuiAbstractBlockListElement(Minecraft mc, Consumer<List<AbstractBlock>> callback)
        {
            super(mc, callback);
        }

        @Override
        protected void drawElementPart(AbstractBlock element, int i, int x, int y, boolean hover, boolean selected)
        {
            int color = CommonProxy.getConditionBlocks().getColor(element);

            Gui.drawRect(x, y, x + 4, y + this.scroll.scrollItemSize, 0xff000000 + color);
            GuiDraw.drawHorizontalGradientRect(x + 4, y, x + 24, y + this.scroll.scrollItemSize, 0x44000000 + color, color);

            super.drawElementPart(element, i, x + 4, y, hover, selected);
        }

        @Override
        protected String elementToString(AbstractBlock element)
        {
            return element.stringify();
        }
    }
}