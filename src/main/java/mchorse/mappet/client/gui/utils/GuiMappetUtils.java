package mchorse.mappet.client.gui.utils;

import mchorse.mappet.ClientProxy;
import mchorse.mappet.api.utils.TargetMode;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.utils.overlays.GuiContentNamesOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mappet.client.gui.utils.overlays.GuiPromptOverlayPanel;
import mchorse.mclib.McLib;
import mchorse.mclib.client.InputRenderer;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Area;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.ColorUtils;
import mchorse.mclib.utils.Interpolation;
import mchorse.mclib.utils.Interpolations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.SoundEvent;

import java.util.function.Consumer;

public class GuiMappetUtils
{
    public static GuiTextElement fullWindowContext(GuiTextElement text, IKey title)
    {
        Minecraft mc = Minecraft.getMinecraft();

        text.context(() -> new GuiSimpleContextMenu(mc).action(Icons.EDIT, IKey.lang("mappet.gui.overlays.text_fullscreen"), () ->
        {
            GuiPromptOverlayPanel panel = new GuiPromptOverlayPanel(mc, title, text);
            GuiOverlay overlay = new GuiOverlay(mc, panel);

            panel.flex().w(1F, -30).h(54);
            GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay);
        }));

        return text;
    }

    public static void openPicker(ContentType type, String value, Consumer<String> callback)
    {
        ClientProxy.requestNames(type, (names) ->
        {
            GuiContentNamesOverlayPanel overlay = new GuiContentNamesOverlayPanel(Minecraft.getMinecraft(), type.getPickLabel(), type, names, callback);

            overlay.set(value);
            GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.5F, 0.7F);
        });
    }

    public static void drawRightClickHere(GuiContext context, Area area)
    {
        int primary = McLib.primaryColor.get();
        double ticks = (context.tick + context.partialTicks) % 80D;
        double factor = Math.abs(ticks / 80D * 2 - 1F);

        factor = Interpolation.EXP_INOUT.interpolate(0, 1, factor);

        double factor2 = Interpolations.envelope(ticks, 37, 40, 40, 43);

        factor2 = Interpolation.CUBIC_OUT.interpolate(0, 1, factor2);

        int offset = (int) (factor * 70 + factor2 * 2);

        GuiDraw.drawDropCircleShadow(area.mx(), area.my() + (int) (factor * 70), 16, 0, 16, ColorUtils.HALF_BLACK + primary, primary);
        InputRenderer.renderMouseButtons(area.mx() - 6, area.my() - 8 + offset, 0, false, factor2 > 0, false, false);

        String label = I18n.format("mappet.gui.right_click");
        int w = (int) (area.w / 1.1F);
        int color = ColorUtils.multiplyColor(0x444444, 1 - (float) factor);

        GuiDraw.drawMultiText(context.font, label, area.mx() - w / 2, area.my() - 20, color, w, 12, 0.5F, 1);

        GuiDraw.drawVerticalGradientRect(area.x, area.my() + 20, area.ex(), area.my() + 40, 0, 0xff000000);
        Gui.drawRect(area.x, area.my() + 40, area.ex(), area.my() + 90, 0xff000000);
    }

    public static GuiCirculateElement createTargetCirculate(Minecraft mc, TargetMode defaultTarget, Consumer<TargetMode> callback)
    {
        GuiCirculateElement button = new GuiCirculateElement(mc, (b) ->
        {
            if (callback != null)
            {
                callback.accept(TargetMode.values()[b.getValue()]);
            }
        });

        for (TargetMode target : TargetMode.values())
        {
            button.addLabel(IKey.lang("mappet.gui.conditions.targets." + target.name().toLowerCase()));
        }

        button.setValue(defaultTarget.ordinal());

        return button;
    }

    public static void playSound(SoundEvent event)
    {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(event, 1.0F));
    }
}