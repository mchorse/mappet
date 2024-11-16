package mchorse.mappet.client.gui.crafting;

import mchorse.mappet.api.crafting.CraftingRecipe;
import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.crafting.PacketCraft;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.ColorUtils;
import mchorse.mclib.utils.TextUtils;
import net.minecraft.client.Minecraft;

public class GuiCrafting extends GuiElement implements ICraftingScreen
{
    public static final IKey CRAFT_LABEL = IKey.lang("mappet.gui.crafting.craft");

    public GuiCraftingRecipes recipes;
    public GuiButtonElement craft;

    private CraftingTable table;

    public GuiCrafting(Minecraft mc)
    {
        super(mc);

        this.craft = new GuiButtonElement(mc, CRAFT_LABEL, this::craft);
        this.craft.flex().relative(this.area).x(1F, -10).y(1F, -10).wh(80, 20).anchor(1F, 1F);

        this.recipes = new GuiCraftingRecipes(mc, (element) -> this.pickRecipe(element.getRecipe()));
        this.recipes.flex().relative(this.area).x(10).y(10).w(1F, -20).hTo(this.craft.area, -5);

        this.add(this.craft, this.recipes);
    }

    public CraftingTable get()
    {
        return this.table;
    }

    public void set(CraftingTable table)
    {
        this.table = table;
        this.craft.label = table.action.trim().isEmpty() ? CRAFT_LABEL : IKey.str(TextUtils.processColoredText(table.action));

        this.recipes.setTable(this.table);
        this.pickRecipe(this.table.recipes.get(0));
        this.recipes.setRecipe(this.table.recipes.get(0));

        this.keys().keybinds.clear();

        for (CraftingRecipe recipe : this.table.recipes)
        {
            if (recipe.hotkey > 0)
            {
                this.keys().register(IKey.format("mappet.gui.crafting.keys.craft", recipe.title), recipe.hotkey, () ->
                {
                    this.pickRecipe(recipe);
                    this.recipes.setRecipe(recipe);
                    this.craft(this.craft);
                });
            }
        }
    }

    @Override
    public void refresh()
    {
        this.pickRecipe(this.recipes.getCurrent().getRecipe());
    }

    private void craft(GuiButtonElement button)
    {
        Dispatcher.sendToServer(new PacketCraft(this.recipes.getChildren().indexOf(this.recipes.getCurrent())));
    }

    private void pickRecipe(CraftingRecipe recipe)
    {
        this.craft.setEnabled(recipe.isPlayerHasAllItems(Minecraft.getMinecraft().player));
    }

    @Override
    public void draw(GuiContext context)
    {
        super.draw(context);

        if (this.mc.player.isCreative() && this.table != null)
        {
            int w = this.font.getStringWidth(this.table.getId());
            
            GuiDraw.drawTextBackground(this.font, this.table.getId(), this.area.mx(w), this.craft.area.my(this.font.FONT_HEIGHT - 2), 0xffffff, ColorUtils.HALF_BLACK);
        }
    }

    @Override
    public boolean mouseClicked(GuiContext context)
    {
        if (this.craft.area.isInside(context) && context.mouseButton == 0)
        {
            this.craft(this.craft);

            return true;
        }

        if (super.mouseClicked(context))
        {
            return true;
        }

        return false;
    }
}