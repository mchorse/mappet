package mchorse.mappet.utils;

import mchorse.mappet.client.gui.utils.text.GuiText;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.GuiUtils;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.config.gui.GuiConfigPanel;
import mchorse.mclib.config.values.ValueGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class ValueButtons extends ValueGUI
{
    public ValueButtons(String id)
    {
        super(id);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<GuiElement> getFields(Minecraft mc, GuiConfigPanel config)
    {
        return Arrays.asList(
            Elements.column(mc, 5,
                new GuiText(mc).text(IKey.lang("mappet.translation.credit")),
                Elements.row(mc, 5,
                    new GuiButtonElement(mc, IKey.lang("mappet.translation.wiki"), (b) -> GuiUtils.openWebLink(I18n.format("mappet.translation.wiki_url"))),
                    new GuiButtonElement(mc, IKey.lang("mappet.translation.community"), (b) -> GuiUtils.openWebLink(I18n.format("mappet.translation.community_url")))
                )
            ).marginTop(6)
        );
    }
}