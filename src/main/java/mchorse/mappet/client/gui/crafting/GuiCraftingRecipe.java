package mchorse.mappet.client.gui.crafting;

import mchorse.mappet.api.crafting.CraftingRecipe;
import mchorse.mappet.client.gui.utils.GuiItemsElement;
import mchorse.mappet.client.gui.utils.GuiTriggerElement;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiInventoryElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.util.function.Supplier;

public class GuiCraftingRecipe extends GuiElement
{
    public GuiTextElement title;
    public GuiItemsElement input;
    public GuiItemsElement output;
    public GuiTextElement condition;
    public GuiTriggerElement trigger;

    public CraftingRecipe recipe;

    public GuiCraftingRecipe(Minecraft mc, CraftingRecipe recipe, Supplier<GuiInventoryElement> inventory)
    {
        super(mc);

        this.recipe = recipe;

        this.title = new GuiTextElement(mc, 1000, (text) -> this.recipe.title = text);
        this.title.setText(recipe.title);

        this.input = new GuiItemsElement(mc, IKey.str("Ingredients"), recipe.input, inventory);
        this.output = new GuiItemsElement(mc, IKey.str("Result"), recipe.output, inventory);

        this.condition = new GuiTextElement(mc, 1000, (text) -> this.recipe.condition = text);
        this.condition.setText(recipe.condition);

        this.trigger = new GuiTriggerElement(mc, recipe.trigger);
        this.trigger.flex().h(40);

        this.flex().column(5).vertical().stretch().padding(10);

        this.add(Elements.label(IKey.str("Recipe's title")), this.title, this.input, this.output);
        this.add(Elements.label(IKey.str("Expression condition"), 20).anchor(0, 1F), this.condition, this.trigger);
    }

    @Override
    public void draw(GuiContext context)
    {
        Gui.drawRect(this.area.x, this.area.y, this.area.ex(), this.area.y + 1, 0x88000000);

        super.draw(context);
    }
}