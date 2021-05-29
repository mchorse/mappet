package mchorse.mappet.client.gui.utils;

import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.mclib.GuiDashboardPanel;
import mchorse.mclib.utils.ColorUtils;
import net.minecraft.client.Minecraft;

public class GuiDebugPanel extends GuiDashboardPanel<GuiMappetDashboard>
{
    public GuiMultiTextElement text;

    public GuiDebugPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.text = new GuiMultiTextElement(mc, null);
        this.text.flex().relative(this).wh(0.8F, 0.5F);

        this.text.setText(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit.\n" +
            "Suspendisse lectus enim, vehicula non laoreet tincidunt, sagittis nec mi.\n" +
            "Suspendisse posuere, sem a congue sagittis, diam tortor ultricies dolor, consectetur vehicula sapien elit eu ante. Donec a placerat lorem.\n" +
            "Nulla eu consequat quam. Pellentesque interdum elementum imperdiet. Vivamus a ante id lacus imperdiet accumsan vitae ut sapien. Mauris tempus blandit metus, at faucibus turpis pretium in.\n" +
            "Vestibulum augue enim, euismod id purus quis, congue auctor nisl. \n" +
            "Nam elementum orci vel ligula rhoncus, at rhoncus felis tincidunt. Nulla facilisis maximus ipsum ac pulvinar.\n" +
            "\n" +
            "Duis vulputate a risus in laoreet.\n" +
            "Integer id ex molestie, blandit quam nec, tincidunt nibh.\n" +
            "Morbi luctus at felis ut porttitor. Nulla a mauris tortor.\n" +
            "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Fusce imperdiet erat non lectus egestas, vitae lobortis leo faucibus.\n" +
            "Nulla ut mi sit amet risus tempor faucibus. Phasellus in eleifend nisi, sed tristique risus.\n" +
            "Nunc vel nibh a ante ultrices laoreet eget quis magna. Pellentesque fermentum purus vel pulvinar tristique.\n" +
            "Sed sit amet vehicula ante, a malesuada dolor. Nullam ultrices nisi quis posuere ornare.\n" +
            "Etiam eget neque lacinia, consectetur augue sit amet, facilisis ex. Nulla gravida fermentum sapien, aliquam aliquet risus finibus non."
        );

        GuiTextElement element = new GuiTextElement(mc, 10000, null);

        element.flex().relative(this).x(10).y(1F, -30).w(100);

        this.add(this.text, element);
    }

    @Override
    public void draw(GuiContext context)
    {
        this.text.area.draw(ColorUtils.HALF_BLACK);

        super.draw(context);
    }
}