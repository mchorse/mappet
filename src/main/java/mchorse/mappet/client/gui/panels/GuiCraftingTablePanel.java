package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.crafting.CraftingRecipe;
import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.crafting.GuiCraftingRecipe;
import mchorse.mappet.client.gui.crafting.GuiCraftingRecipeList;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiInventoryElement;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

/* TODO: extract strings */
public class GuiCraftingTablePanel extends GuiMappetDashboardPanel<CraftingTable>
{
    public GuiTextElement title;
    public GuiInventoryElement inventory;

    public GuiCraftingRecipe recipe;
    public GuiCraftingRecipeList recipes;

    public GuiCraftingTablePanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.title = new GuiTextElement(mc, 1000, (text) -> this.data.title = text);

        this.inventory = new GuiInventoryElement(mc, (stack) ->
        {
            this.inventory.linked.acceptStack(stack);
            this.inventory.unlink();
        });
        this.inventory.flex().relative(this.editor).xy(0.5F, 0.5F).anchor(0.5F, 0.5F);
        this.inventory.setVisible(false);

        this.recipes = new GuiCraftingRecipeList(mc, (list) -> this.pickRecipe(list.get(0), false));
        this.recipes.sorting().context(() ->
        {
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(this.mc)
                .action(Icons.ADD, IKey.str("Add a crafting recipe"), this::addRecipe);

            if (!this.recipes.isDeselected())
            {
                menu.action(Icons.REMOVE, IKey.str("Remove current recipe"), this::removeRecipe);
            }

            return menu;
        });

        this.recipe = new GuiCraftingRecipe(mc, () -> this.inventory);

        this.title.flex().relative(this.recipes).x(10).y(-30).wTo(this.editor.area, 1F, -10);
        this.recipes.flex().relative(this).y(55).w(120).h(1F, -55);
        this.editor.flex().x(120).y(55).h(1F, -55).column(0);

        this.add(this.title, this.recipes, this.inventory);
        this.editor.add(this.recipe);

        this.fill("", null);
    }

    private void pickRecipe(CraftingRecipe recipe, boolean select)
    {
        this.recipe.set(recipe);
        this.editor.resize();

        if (select)
        {
            this.recipes.setCurrentScroll(recipe);
        }
    }

    private void addRecipe()
    {
        CraftingRecipe recipe = new CraftingRecipe();

        this.data.recipes.add(recipe);
        this.pickRecipe(recipe, true);
        this.editor.resize();

        this.editor.scroll.scrollTo(this.editor.scroll.scrollSize);
    }

    private void removeRecipe()
    {
        this.data.recipes.remove(this.recipes.getCurrentFirst());
        this.recipes.update();
    }

    @Override
    public ContentType getType()
    {
        return ContentType.CRAFTING_TABLE;
    }

    @Override
    public void fill(String id, CraftingTable data)
    {
        super.fill(id, data);

        this.title.setVisible(data != null);
        this.editor.setVisible(data != null);

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

        super.draw(context);

        if (this.title.isVisible())
        {
            this.font.drawStringWithShadow("Crafting table's title", this.title.area.x, this.area.y + 10, 0xffffff);
        }
    }
}