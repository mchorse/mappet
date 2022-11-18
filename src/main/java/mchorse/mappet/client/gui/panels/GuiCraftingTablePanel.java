package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.crafting.CraftingRecipe;
import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.crafting.GuiCraftingRecipeEditor;
import mchorse.mappet.client.gui.crafting.GuiCraftingRecipeList;
import mchorse.mappet.utils.Colors;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.metamorph.util.MMIcons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class GuiCraftingTablePanel extends GuiMappetRunPanel<CraftingTable>
{
    public static final IKey ACTION = IKey.lang("mappet.gui.crafting.action");
    public static final IKey TITLE = IKey.lang("mappet.gui.crafting.title");
    public static final IKey EMPTY_TABLE = IKey.lang("mappet.gui.crafting.info.empty");
    public static final IKey EMPTY_RECIPE = IKey.lang("mappet.gui.crafting.info.empty_recipe");

    public GuiTextElement title;
    public GuiTextElement action;

    public GuiCraftingRecipeEditor recipe;
    public GuiCraftingRecipeList recipes;

    public GuiCraftingTablePanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.namesList.setFileIcon(Icons.WRENCH);

        this.title = new GuiTextElement(mc, 1000, (text) -> this.data.title = text);
        this.action = new GuiTextElement(mc, 1000, (text) -> this.data.action = text);

        this.recipes = new GuiCraftingRecipeList(mc, (list) -> this.pickRecipe(list.get(0), false));
        this.recipes.sorting().context(() ->
        {
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(this.mc)
                .action(Icons.ADD, IKey.lang("mappet.gui.crafting.context.add"), this::addRecipe);

            if (!this.recipes.isDeselected())
            {
                menu.action(Icons.REMOVE, IKey.lang("mappet.gui.crafting.context.remove"), this::removeRecipe, Colors.NEGATIVE);
            }

            return menu;
        });

        GuiScrollElement scrollEditor = this.createScrollEditor();

        this.recipe = new GuiCraftingRecipeEditor(mc);

        int y = 52;

        this.title.flex().relative(this.editor).x(10).y(22).wTo(this.action.area, -5);
        this.action.flex().relative(this.editor).x(0.65F).y(22).wTo(this.editor.area, 1F, -10);
        this.recipes.flex().relative(this.editor).y(y).w(120).h(1F, -y);
        scrollEditor.flex().x(120).y(y).w(1F, -120).h(1F, -y).column(0).padding(0);

        this.editor.add(this.action, this.title, this.recipes, scrollEditor);
        scrollEditor.add(this.recipe);

        this.fill(null);
    }

    private void pickRecipe(CraftingRecipe recipe, boolean select)
    {
        this.recipe.setVisible(recipe != null);

        if (recipe != null)
        {
            this.recipe.set(recipe);
            this.editor.resize();

            if (select)
            {
                this.recipes.setCurrentScroll(recipe);
            }
        }
    }

    private void addRecipe()
    {
        CraftingRecipe recipe = new CraftingRecipe();

        this.data.recipes.add(recipe);
        this.pickRecipe(recipe, true);
        this.editor.resize();
        this.recipes.update();
    }

    private void removeRecipe()
    {
        int index = this.recipes.getIndex();

        this.data.recipes.remove(this.recipes.getCurrentFirst());

        if (index > 0)
        {
            index -= 1;
        }

        this.pickRecipe(this.data.recipes.isEmpty() ? null : this.data.recipes.get(index), true);
        this.recipes.update();
    }

    @Override
    protected void run(EntityPlayerSP player)
    {
        this.save();
        this.save = false;

        player.sendChatMessage("/mp crafting open " + player.getUniqueID().toString() + " " + this.data.getId());
    }

    @Override
    public ContentType getType()
    {
        return ContentType.CRAFTING_TABLE;
    }

    @Override
    public String getTitle()
    {
        return "mappet.gui.panels.crafting";
    }

    @Override
    public void fill(CraftingTable data, boolean allowed)
    {
        super.fill(data, allowed);

        this.title.setVisible(data != null);
        this.action.setVisible(data != null);
        this.editor.setVisible(data != null);
        this.recipes.setVisible(data != null);

        if (data != null)
        {
            this.title.setText(this.data.title);
            this.action.setText(this.data.action);

            this.recipes.setList(this.data.recipes);
            this.pickRecipe(this.data.recipes.isEmpty() ? null : this.data.recipes.get(0), true);

            this.resize();
        }
    }

    @Override
    public void draw(GuiContext context)
    {
        if (this.title.isVisible())
        {
            this.font.drawStringWithShadow(TITLE.get(), this.title.area.x, this.title.area.y - 12, 0xffffff);
            this.font.drawStringWithShadow(ACTION.get(), this.action.area.x, this.action.area.y - 12, 0xffffff);
        }

        if (!this.editor.isVisible())
        {
            int w = (this.editor.area.ex() - this.area.x) / 2;
            int x = (this.area.x + this.editor.area.ex()) / 2 - w / 2;

            GuiDraw.drawMultiText(this.font, EMPTY_TABLE.get(), x, this.area.my(), 0xffffff, w, 12, 0.5F, 0.5F);
        }

        if (this.editor.isVisible() && !this.recipe.isVisible())
        {
            int w = this.editor.area.w / 2;
            int x = this.editor.area.mx(w);

            GuiDraw.drawMultiText(this.font, EMPTY_RECIPE.get(), x, this.editor.area.my(), 0xffffff, w, 12, 0.5F, 0.5F);
        }

        super.draw(context);
    }
}