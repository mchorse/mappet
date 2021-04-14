package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.crafting.CraftingRecipe;
import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.crafting.GuiCraftingRecipe;
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

    public GuiCraftingTablePanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.title = new GuiTextElement(mc, 1000, (text) -> this.data.title = text);
        this.title.flex().relative(this.editor).x(10).y(-30).w(1F, -20);

        this.editor.flex().y(55).h(1F, -55).column(5).margin = 30;
        this.editor.context(() -> new GuiSimpleContextMenu(this.mc)
            .action(Icons.ADD, IKey.str("Add a recipe"), this::addRecipe));

        this.inventory = new GuiInventoryElement(mc, (stack) ->
        {
            this.inventory.linked.acceptStack(stack);
            this.inventory.unlink();
        });
        this.inventory.flex().relative(this.editor).xy(0.5F, 0.5F).anchor(0.5F, 0.5F);
        this.inventory.setVisible(false);

        this.add(this.title, this.inventory);

        this.fill("", null);
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
        this.editor.removeAll();

        if (data != null)
        {
            this.title.setText(this.data.title);

            for (CraftingRecipe recipe : this.data.recipes)
            {
                this.editor.add(this.createRecipe(recipe));
            }

            this.resize();
        }
    }

    private void addRecipe()
    {
        CraftingRecipe recipe = new CraftingRecipe();

        this.data.recipes.add(recipe);
        this.editor.add(this.createRecipe(recipe));
        this.editor.resize();

        this.editor.scroll.scrollTo(this.editor.scroll.scrollSize);
    }

    private GuiCraftingRecipe createRecipe(CraftingRecipe recipe)
    {
        GuiCraftingRecipe element = new GuiCraftingRecipe(this.mc, recipe, () -> this.inventory);

        element.context(() -> new GuiSimpleContextMenu(this.mc)
            .action(Icons.ADD, IKey.str("Add a recipe"), this::addRecipe)
            .action(Icons.REMOVE, IKey.str("Remove recipe"), () -> this.removeRecipe(element)));

        return element;
    }

    private void removeRecipe(GuiCraftingRecipe recipe)
    {
        this.data.recipes.remove(recipe.recipe);
        recipe.removeFromParent();
        this.editor.resize();
    }

    @Override
    public void draw(GuiContext context)
    {
        this.editor.area.draw(0x66000000);

        super.draw(context);

        if (this.title.isVisible())
        {
            this.font.drawStringWithShadow("Crafting table's title", this.title.area.x, this.area.y + 10, 0xffffff);
        }
    }
}