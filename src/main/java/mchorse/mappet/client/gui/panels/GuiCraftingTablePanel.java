package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.crafting.CraftingRecipe;
import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.crafting.GuiCraftingRecipe;
import mchorse.mappet.client.gui.crafting.GuiCraftingRecipeList;
import mchorse.mappet.utils.Colors;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiCraftingTablePanel extends GuiMappetDashboardPanel<CraftingTable>
{
    public GuiTextElement title;

    public GuiCraftingRecipe recipe;
    public GuiCraftingRecipeList recipes;

    public GuiCraftingTablePanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.title = new GuiTextElement(mc, 1000, (text) -> this.data.title = text);

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

        this.recipe = new GuiCraftingRecipe(mc, () -> this.inventory);

        this.title.flex().relative(this.editor).x(10).y(22).w(1F, -20);
        this.recipes.flex().relative(this.editor).y(52).w(120).h(1F, -55);
        scrollEditor.flex().x(120).y(52).w(1F, -120).h(1F, -55).column(0).padding(0);

        this.editor.add(this.title, this.recipes, scrollEditor, this.inventory);
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
        this.editor.setVisible(data != null);
        this.recipes.setVisible(data != null);

        if (data != null)
        {
            this.title.setText(this.data.title);

            this.recipes.setList(this.data.recipes);
            this.pickRecipe(this.data.recipes.isEmpty() ? null : this.data.recipes.get(0), true);

            this.resize();
        }
    }

    @Override
    protected void toggleWithSidebar()
    {
        this.editor.flex().wTo(this.sidebar.area, 0);
    }

    @Override
    protected void toggleFull()
    {
        this.editor.flex().w(1F, -120);
    }

    @Override
    public void draw(GuiContext context)
    {
        if (this.editor.isVisible())
        {
            this.editor.area.draw(0x66000000);
        }

        if (this.title.isVisible())
        {
            this.font.drawStringWithShadow(I18n.format("mappet.gui.crafting.title"), this.title.area.x, this.title.area.y - 12, 0xffffff);
        }

        if (!this.editor.isVisible())
        {
            int w = (this.editor.area.ex() - this.area.x) / 2;
            int x = (this.area.x + this.editor.area.ex()) / 2 - w / 2;

            GuiDraw.drawMultiText(this.font, I18n.format("mappet.gui.crafting.info.empty"), x, this.area.my(), 0xffffff, w, 12, 0.5F, 0.5F);
        }

        if (this.editor.isVisible() && !this.recipe.isVisible())
        {
            int w = this.editor.area.w / 2;
            int x = this.editor.area.mx(w);

            GuiDraw.drawMultiText(this.font, I18n.format("mappet.gui.crafting.info.empty_recipe"), x, this.editor.area.my(), 0xffffff, w, 12, 0.5F, 0.5F);
        }

        super.draw(context);
    }
}