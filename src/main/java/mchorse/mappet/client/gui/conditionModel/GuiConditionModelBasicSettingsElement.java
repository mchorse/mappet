package mchorse.mappet.client.gui.conditionModel;

import mchorse.mappet.tile.TileConditionModel;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiConditionModelBasicSettingsElement extends GuiElement
{
    public GuiTrackpadElement frequency;
    public GuiToggleElement isGlobal;
    public GuiToggleElement isShadow;

    public TileConditionModel tile;

    public GuiConditionModelBasicSettingsElement(Minecraft mc)
    {
        super(mc);

        this.isGlobal = new GuiToggleElement(mc, IKey.lang("mappet.gui.conditionModel.global"), (b) -> this.tile.isGlobal = b.isToggled());
        this.isShadow = new GuiToggleElement(mc, IKey.lang("mappet.gui.conditionModel.shadow"), (b) -> this.tile.isShadow = b.isToggled());
        this.frequency = new GuiTrackpadElement(mc, (value) -> this.tile.frequency = value.intValue()).limit(1).integer();

        this.add(this.isGlobal, this.isShadow);
        this.add(Elements.label(IKey.lang("mappet.gui.conditionModel.frequency")).marginTop(6), this.frequency);
        this.flex().column(5).vertical().stretch();
    }

    public void set(TileConditionModel tile)
    {
        this.tile = tile;

        if (tile != null)
        {
            this.isGlobal.toggled(tile.isGlobal);
            this.isShadow.toggled(tile.isShadow);
            this.frequency.setValue(tile.frequency);
        }
    }
}
