package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.DiscardMethod;
import mchorse.mappet.api.ui.utils.GuiClick;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Click area component.
 *
 * <p>This component doesn't display anything but rather acts as a special
 * user input field. When an ID is assigned and users clicks on the component's
 * bounds, this component will be sending a NBT list containing 5 floats:</p>
 *
 * <ul>
 *     <li>Index <code>0</code> = X coordinate relative to click area's frame.</li>
 *     <li>Index <code>1</code> = Y coordinate relative to click area's frame.</li>
 *     <li>Index <code>2</code> = X factor (0..1) how far into the click area's width (<code>0</code> being left edge, <code>1</code> being the right edge).</li>
 *     <li>Index <code>3</code> = Y factor (0..1) how far into the click area's height (<code>0</code> being top edge, <code>1</code> being the bottom edge).</li>
 *     <li>Index <code>4</code> = Mouse button (<code>0</code> is left button, <code>1</code> is right button, <code>2</code> is middle button, etc.).</li>
 * </ul>
 *
 * <p>This component can be created using {@link IMappetUIBuilder#click()} method.</p>
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        let ui = mappet.createUI(handler).background();
 *        let click = ui.click().id("click");
 *        let backdrop = ui.graphics().id("backdrop");
 *
 *        backdrop.rxy(0.5, 0.5).wh(300, 150).anchor(0.5);
 *        backdrop.rect(0, 0, 300, 150, 0x88000000n);
 *        click.rxy(0.5, 0.5).wh(300, 150).anchor(0.5);
 *        c.getSubject().openUI(ui);
 *    }
 *
 *    function handler(c)
 *    {
 *        let uiContext = c.getSubject().getUIContext();
 *        let data = uiContext.getData();
 *
 *        if (uiContext.getLast() === "click")
 *        {
 *            // Math.floor just in case there are precision issues
 *            let list = data.getList("click");
 *            let x = Math.floor(list.getFloat(0));
 *            let y = Math.floor(list.getFloat(1));
 *            let fx = list.getFloat(2);
 *            let fy = list.getFloat(3);
 *            let button = Math.floor(list.getFloat(4));
 *
 *            // Draw random rectangle on the back drop
 *            if (button === 0)
 *            {
 *                let backdrop = uiContext.get("backdrop");
 *
 *                backdrop.rect(x - 10, y - 10, 20, 20, 0xff000000n + BigInt(Math.floor(Math.random() * 0xffffff)));
 *            }
 *        }
 *    }
 * }</pre>
 */
public class UIClickComponent extends UIComponent
{
    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        return this.apply(new GuiClick(mc, this, context), context);
    }

    @Override
    @DiscardMethod
    public void populateData(NBTTagCompound tag)
    {
        super.populateData(tag);

        if (!this.id.isEmpty())
        {
            NBTTagList list = new NBTTagList();

            list.appendTag(new NBTTagFloat(0));
            list.appendTag(new NBTTagFloat(0));
            list.appendTag(new NBTTagFloat(0));
            list.appendTag(new NBTTagFloat(0));
            list.appendTag(new NBTTagFloat(0));

            tag.setTag(this.id, list);
        }
    }
}
