package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.DiscardMethod;
import mchorse.mappet.client.gui.utils.text.GuiText;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Text UI component.
 *
 * <p>This component allows you to input lots of text. This text can have multiple lines
 * and Minecraft's formatting using "[" symbol instead of section field. Beside that
 * this text component is resizable inside of column and row elements so it should
 * work perfectly with layouts.</p>
 *
 * <p>This component can be created using {@link IMappetUIBuilder#text(String)} method.</p>
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        var ui = mappet.createUI().background();
 *        var scroll = ui.column(5, 10);
 *
 *        scroll.getCurrent().scroll().rxy(0.5, 0.5).w(200).rh(0.8).anchor(0.5);
 *
 *        var text = scroll.text("Lorem ipsum dolor sit amet,\n\n" +
 *            "consectetur adipiscing elit. Nullam sit amet luctus tellus. Sed posuere, orci quis vehicula mattis, orci nulla malesuada nunc, in mattis mi urna a quam. Mauris malesuada tempus molestie. Pellentesque in est quam. Sed iaculis dictum bibendum. Cras eleifend varius ligula, id luctus arcu ultricies a. Nam tincidunt mauris eu ligula sodales faucibus sed ut eros. Phasellus consectetur nec magna quis fermentum. Donec quis mauris tristique neque suscipit placerat. Etiam id laoreet ante. Maecenas finibus nec augue vitae convallis.\n\n" +
 *            "Donec at tortor nibh. Nunc quis nulla justo. Vestibulum lacinia quis sapien at euismod. Curabitur sed maximus sapien. Fusce sed dui at lectus venenatis volutpat ac ac sapien. Cras at tortor pellentesque, finibus nulla vitae, tristique ligula. Etiam porta elementum justo. Cras facilisis rutrum mauris ac consectetur. Aliquam ipsum dolor, accumsan et lacus malesuada, volutpat pretium odio. Donec sed purus vulputate, auctor nulla in, sagittis ipsum. Nam dolor tortor, consequat sit amet eleifend at, imperdiet at ligula. Aenean blandit sem sit amet ex vehicula consequat. Etiam feugiat condimentum sem, eget imperdiet augue mattis quis.");
 *
 *        c.getSubject().openUI(ui);
 *    }
 * }</pre>
 */
public class UITextComponent extends UILabelBaseComponent
{
    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        return this.apply(new GuiText(mc).text(this.getLabel()), context);
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    protected void applyProperty(UIContext context, String key, GuiElement element)
    {
        super.applyProperty(context, key, element);

        if (key.equals("Label"))
        {
            ((GuiText) element).text(this.getLabel());
        }
    }
}