package mchorse.mappet.client.gui.crafting;

import mchorse.mappet.api.crafting.CraftingRecipe;
import mchorse.mappet.client.gui.conditions.GuiCheckerElement;
import mchorse.mappet.client.gui.triggers.GuiTriggerElement;
import mchorse.mappet.client.gui.utils.GuiItemsElement;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiKeybindElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class GuiCraftingRecipeEditor extends GuiElement
{
    public GuiTextElement title;
    public GuiTextElement description;
    public GuiItemsElement input;
    public GuiItemsElement output;
    public GuiCheckerElement checker;
    public GuiTriggerElement trigger;
    public GuiKeybindElement hotkey;
    public GuiToggleElement ignoreNBT;

    public CraftingRecipe recipe;

    public GuiCraftingRecipeEditor(Minecraft mc)
    {
        super(mc);

        this.title = new GuiTextElement(mc, 1000, (text) -> this.recipe.title = text);
        this.description = new GuiTextElement(mc, 1000, (text) -> this.recipe.description = text);
        this.input = new GuiItemsElement(mc, IKey.lang("mappet.gui.crafting.input"), null);
        this.input.marginTop(12);
        this.ignoreNBT = new GuiToggleElement(mc, IKey.lang("mappet.gui.crafting.ignoreNBT"), (b) -> this.recipe.ignoreNBT = b.isToggled());
        this.ignoreNBT.flex().h(30);
        this.output = new GuiItemsElement(mc, IKey.lang("mappet.gui.crafting.output"), null);
        this.checker = new GuiCheckerElement(mc);
        this.trigger = new GuiTriggerElement(mc);
        this.hotkey = new GuiKeybindElement(mc, (key) ->
        {
            if (key == Keyboard.KEY_ESCAPE)
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

        this.add(Elements.label(IKey.lang("mappet.gui.crafting.recipe.title")), this.title);
        this.add(Elements.label(IKey.lang("mappet.gui.crafting.recipe.description")).marginTop(12), this.description, this.input, this.ignoreNBT, this.output);
        this.add(Elements.row(mc, 5,
            Elements.column(mc, 4, Elements.label(IKey.lang("mappet.gui.crafting.recipe.visible")), this.checker),
            Elements.column(mc, 4, Elements.label(IKey.lang("mappet.gui.crafting.recipe.hotkey")), this.hotkey)
        ).marginTop(12));
        this.add(Elements.label(IKey.lang("mappet.gui.crafting.recipe.trigger")).background().marginTop(12).marginBottom(5), this.trigger);
    }

    public void set(CraftingRecipe recipe)
    {
        this.recipe = recipe;

        this.title.setText(recipe.title);
        this.description.setText(recipe.description);
        this.input.set(recipe.input);
        this.ignoreNBT.toggled(recipe.ignoreNBT);
        this.output.set(recipe.output);
        this.checker.set(recipe.visible);
        this.trigger.set(recipe.trigger);
        this.hotkey.setKeybind(recipe.hotkey);
    }
}