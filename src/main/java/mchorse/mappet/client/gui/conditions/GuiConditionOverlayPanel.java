package mchorse.mappet.client.gui.conditions;

import mchorse.mappet.api.conditions.Condition;
import mchorse.mappet.api.conditions.blocks.AbstractBlock;
import mchorse.mappet.api.conditions.blocks.DialogueBlock;
import mchorse.mappet.api.conditions.blocks.FactionBlock;
import mchorse.mappet.api.conditions.blocks.QuestBlock;
import mchorse.mappet.api.conditions.blocks.StateBlock;
import mchorse.mappet.api.conditions.blocks.WorldTimeBlock;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlayPanel;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.list.GuiListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GuiConditionOverlayPanel extends GuiOverlayPanel
{
    public static final Map<
            Class<? extends AbstractBlock>,
            Class<? extends GuiAbstractBlockPanel<? extends AbstractBlock>>>
        PANELS = new HashMap<
            Class<? extends AbstractBlock>,
            Class<? extends GuiAbstractBlockPanel<? extends AbstractBlock>>>();

    public GuiAbstractBlockListElement list;

    public GuiScrollElement editor;

    private Condition condition;
    private AbstractBlock block;

    static
    {
        PANELS.put(QuestBlock.class, GuiQuestBlockPanel.class);
        PANELS.put(StateBlock.class, GuiStateBlockPanel.class);
        PANELS.put(DialogueBlock.class, GuiDialogueBlockPanel.class);
        PANELS.put(FactionBlock.class, GuiFactionBlockPanel.class);
        PANELS.put(WorldTimeBlock.class, GuiWorldTimeBlockPanel.class);
    }

    public GuiConditionOverlayPanel(Minecraft mc, Condition condition)
    {
        super(mc, IKey.lang("mappet.gui.conditions.title"));

        this.condition = condition;

        this.list = new GuiAbstractBlockListElement(mc, (l) -> this.pickBlock(l.get(0), false));
        this.list.sorting().setList(condition.blocks);
        this.list.context(() ->
        {
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(this.mc);

            menu.action(Icons.ADD, IKey.lang("mappet.gui.conditions.context.add"), () ->
            {
                GuiSimpleContextMenu adds = new GuiSimpleContextMenu(this.mc);

                for (String key : AbstractBlock.FACTORY.getKeys())
                {
                    adds.action(Icons.ADD, IKey.format("mappet.gui.conditions.context.add_condition", IKey.lang("mappet.gui.condition_types." + key)), () -> this.addBlock(key));
                }

                GuiBase.getCurrent().replaceContextMenu(adds);
            });
            menu.action(Icons.REMOVE, IKey.lang("mappet.gui.conditions.context.remove"), this::removeBlock);

            return menu;
        });

        this.editor = new GuiScrollElement(mc);

        this.list.flex().relative(this.content).w(120).h(1F);
        this.editor.flex().relative(this.content).x(120).w(1F, -120).h(1F).column(5).vertical().stretch().scroll().padding(10);

        this.content.add(this.editor, this.list);

        this.pickBlock(this.condition.blocks.isEmpty() ? null : this.condition.blocks.get(0), true);
    }

    private void addBlock(String type)
    {
        AbstractBlock block = AbstractBlock.FACTORY.create(type);

        this.condition.blocks.add(block);
        this.pickBlock(block, true);
        this.list.update();
    }

    private void removeBlock()
    {
        int index = this.list.getIndex();

        this.condition.blocks.remove(index);

        index = Math.max(index - 1, 0);

        this.pickBlock(this.condition.blocks.isEmpty() ? null :this.condition.blocks.get(index), true);
        this.list.update();
    }

    private void pickBlock(AbstractBlock block, boolean select)
    {
        this.block = block;

        this.editor.setVisible(block != null);

        if (block != null)
        {
            this.editor.removeAll();

            try
            {
                GuiAbstractBlockPanel panel = (GuiAbstractBlockPanel) PANELS.get(block.getClass()).getConstructors()[0].newInstance(this.mc, block);

                this.editor.add(panel);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            if (select)
            {
                this.list.setCurrentScroll(block);
            }
        }

        this.resize();
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
            int color = AbstractBlock.FACTORY.getColor(element);

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