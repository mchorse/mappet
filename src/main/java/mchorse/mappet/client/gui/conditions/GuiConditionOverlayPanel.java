package mchorse.mappet.client.gui.conditions;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.api.conditions.Condition;
import mchorse.mappet.api.conditions.blocks.AbstractConditionBlock;
import mchorse.mappet.api.conditions.blocks.ConditionConditionBlock;
import mchorse.mappet.api.conditions.blocks.DialogueConditionBlock;
import mchorse.mappet.api.conditions.blocks.EntityConditionBlock;
import mchorse.mappet.api.conditions.blocks.FactionConditionBlock;
import mchorse.mappet.api.conditions.blocks.ItemConditionBlock;
import mchorse.mappet.api.conditions.blocks.MorphConditionBlock;
import mchorse.mappet.api.conditions.blocks.QuestConditionBlock;
import mchorse.mappet.api.conditions.blocks.StateConditionBlock;
import mchorse.mappet.api.conditions.blocks.WorldTimeConditionBlock;
import mchorse.mappet.client.gui.conditions.blocks.GuiAbstractConditionBlockPanel;
import mchorse.mappet.client.gui.conditions.blocks.GuiConditionConditionBlockPanel;
import mchorse.mappet.client.gui.conditions.blocks.GuiDialogueConditionBlockPanel;
import mchorse.mappet.client.gui.conditions.blocks.GuiEntityConditionBlockPanel;
import mchorse.mappet.client.gui.conditions.blocks.GuiFactionConditionBlockPanel;
import mchorse.mappet.client.gui.conditions.blocks.GuiItemConditionBlockPanel;
import mchorse.mappet.client.gui.conditions.blocks.GuiMorphConditionBlockPanel;
import mchorse.mappet.client.gui.conditions.blocks.GuiQuestConditionBlockPanel;
import mchorse.mappet.client.gui.conditions.blocks.GuiStateConditionBlockPanel;
import mchorse.mappet.client.gui.conditions.blocks.GuiWorldTimeConditionBlockPanel;
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
import mchorse.mclib.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GuiConditionOverlayPanel extends GuiEditorOverlayPanel<AbstractConditionBlock>
{
    public static final Map<
            Class<? extends AbstractConditionBlock>,
            Class<? extends GuiAbstractConditionBlockPanel<? extends AbstractConditionBlock>>>
        PANELS = new HashMap<
            Class<? extends AbstractConditionBlock>,
            Class<? extends GuiAbstractConditionBlockPanel<? extends AbstractConditionBlock>>>();

    private Condition condition;

    static
    {
        PANELS.put(QuestConditionBlock.class, GuiQuestConditionBlockPanel.class);
        PANELS.put(StateConditionBlock.class, GuiStateConditionBlockPanel.class);
        PANELS.put(DialogueConditionBlock.class, GuiDialogueConditionBlockPanel.class);
        PANELS.put(FactionConditionBlock.class, GuiFactionConditionBlockPanel.class);
        PANELS.put(ItemConditionBlock.class, GuiItemConditionBlockPanel.class);
        PANELS.put(WorldTimeConditionBlock.class, GuiWorldTimeConditionBlockPanel.class);
        PANELS.put(EntityConditionBlock.class, GuiEntityConditionBlockPanel.class);
        PANELS.put(ConditionConditionBlock.class, GuiConditionConditionBlockPanel.class);
        PANELS.put(MorphConditionBlock.class, GuiMorphConditionBlockPanel.class);
    }

    public GuiConditionOverlayPanel(Minecraft mc, Condition condition)
    {
        super(mc, IKey.lang("mappet.gui.conditions.title"));

        this.condition = condition;

        this.list.sorting().setList(condition.blocks);
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

                if (tag.getBoolean("_ConditionCopy"))
                {
                    menu.action(Icons.PASTE, IKey.lang("mappet.gui.conditions.context.paste"), () -> this.pasteCondition(tag));
                }
            }
            catch (Exception e)
            {}

            if (!this.list.isDeselected())
            {
                menu.action(Icons.REMOVE, IKey.lang("mappet.gui.conditions.context.remove"), this::removeItem, Colors.NEGATIVE);
            }

            return menu;
        });

        this.pickItem(this.condition.blocks.isEmpty() ? null : this.condition.blocks.get(0), true);
    }

    @Override
    protected GuiListElement<AbstractConditionBlock> createList(Minecraft mc)
    {
        return new GuiAbstractBlockListElement(mc, (l) -> this.pickItem(l.get(0), false));
    }

    private void addBlock(String type)
    {
        AbstractConditionBlock block = CommonProxy.getConditionBlocks().create(type);

        this.condition.blocks.add(block);
        this.pickItem(block, true);
        this.list.update();
    }

    private void copyCondition()
    {
        AbstractConditionBlock block = this.list.getCurrentFirst();
        NBTTagCompound tag = block.serializeNBT();

        tag.setBoolean("_ConditionCopy", true);
        tag.setString("Type", CommonProxy.getConditionBlocks().getType(block));
        GuiScreen.setClipboardString(tag.toString());
    }

    private void pasteCondition(NBTTagCompound tag)
    {
        AbstractConditionBlock block = CommonProxy.getConditionBlocks().create(tag.getString("Type"));

        block.deserializeNBT(tag);
        this.condition.blocks.add(block);
        this.list.update();

        this.pickItem(block, true);
    }

    @Override
    protected void fillData(AbstractConditionBlock block)
    {
        this.editor.removeAll();

        try
        {
            this.editor.add((GuiAbstractConditionBlockPanel) PANELS.get(block.getClass()).getConstructors()[0].newInstance(this.mc, this, block));
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

    public static class GuiAbstractBlockListElement extends GuiListElement<AbstractConditionBlock>
    {
        public GuiAbstractBlockListElement(Minecraft mc, Consumer<List<AbstractConditionBlock>> callback)
        {
            super(mc, callback);

            this.postDraw = true;
            this.scroll.scrollItemSize = 24;
        }

        @Override
        public void drawPostListElement(AbstractConditionBlock element, int i, int x, int y, boolean hover, boolean selected)
        {
            if (i > 0)
            {
                String label = I18n.format(element.or ? "mappet.gui.conditions.label_or" : "mappet.gui.conditions.label_and");

                y -= 4;
                int w = this.font.getStringWidth(label);

                GuiDraw.drawTextBackground(this.font, label, this.scroll.mx(w), y,0xffffff, ColorUtils.HALF_BLACK, 2);
            }
        }

        @Override
        protected void drawElementPart(AbstractConditionBlock element, int i, int x, int y, boolean hover, boolean selected)
        {
            int color = CommonProxy.getConditionBlocks().getColor(element);

            Gui.drawRect(x, y, x + 4, y + this.scroll.scrollItemSize, 0xff000000 + color);
            GuiDraw.drawHorizontalGradientRect(x + 4, y, x + 24, y + this.scroll.scrollItemSize, 0x44000000 + color, color);

            if (element.not)
            {
                GuiDraw.drawTextBackground(this.font, "!", x + 6, y + this.scroll.scrollItemSize / 2 - this.font.FONT_HEIGHT / 2,0xffffff, ColorUtils.HALF_BLACK, 2);
            }

            super.drawElementPart(element, i, x + 4, y, hover, selected);
        }

        @Override
        protected String elementToString(AbstractConditionBlock element)
        {
            return element.stringify();
        }
    }
}