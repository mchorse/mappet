package mchorse.mappet.client.gui.crafting;

import mchorse.mappet.api.crafting.CraftingRecipe;
import mchorse.mappet.client.gui.utils.GuiItemsElement;
import mchorse.mappet.client.gui.utils.GuiTriggerElement;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiKeybindElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiInventoryElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.util.function.Supplier;

public class GuiCraftingRecipe extends GuiElement
{
    public GuiTextElement title;
    public GuiItemsElement input;
    public GuiItemsElement output;
    public GuiTextElement condition;
    public GuiTriggerElement trigger;
    public GuiKeybindElement hotkey;

    public CraftingRecipe recipe;

    public GuiCraftingRecipe(Minecraft mc, Supplier<GuiInventoryElement> inventory)
    {
        super(mc);

        this.title = new GuiTextElement(mc, 1000, (text) -> this.recipe.title = text);
        this.input = new GuiItemsElement(mc, IKey.str("Ingredients"), null, inventory);
        this.output = new GuiItemsElement(mc, IKey.str("Result"), null, inventory);

        this.condition = new GuiTextElement(mc, 1000, (text) -> this.recipe.condition = text);

        this.trigger = new GuiTriggerElement(mc);
        this.trigger.flex().h(40);

        this.hotkey = new GuiKeybindElement(mc, (key) ->
        {
            if (key == 1)
            {
                this.recipe.hotkey = 0;
                this.hotkey.setKeybind(0);
            }
            else
            {
                this.recipe.hotkey = key;
            }
        });

        this.flex().column(5).vertical().stretch().padding(10);

        this.add(Elements.label(IKey.str("Recipe's title")), this.title, this.input, this.output);
        this.add(Elements.label(IKey.str("Expression condition"), 20).anchor(0, 1F), this.condition);
        this.add(Elements.label(IKey.str("Crafting hotkey"), 20).anchor(0, 1F), this.hotkey, this.trigger);
    }

    public void set(CraftingRecipe recipe)
    {
        this.recipe = recipe;

        this.title.setText(recipe.title);
        this.input.set(recipe.input);
        this.output.set(recipe.output);
        this.condition.setText(recipe.condition);
        this.trigger.set(recipe.trigger);
        this.hotkey.setKeybind(recipe.hotkey);
    }
}