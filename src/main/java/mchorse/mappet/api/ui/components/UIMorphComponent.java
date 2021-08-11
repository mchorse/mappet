package mchorse.mappet.api.ui.components;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.DiscardMethod;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.utils.GuiMorphRenderer;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.NBTUtils;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.MorphUtils;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.client.gui.creative.GuiNestedEdit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.text.DecimalFormat;

/**
 * Morph UI component.
 *
 * <p>This component allows to display a morph. Users can inspect it, but it can also be
 * edited, if configured. If you want disable users to turn around the morph by disabling
 * user input using {@link UIComponent#enabled(boolean)}.</p>
 *
 * <p>If this component is editable, then the value that gets written to UI context's data
 * (if ID is present) is an NBT compound tag that represents a morph.</p>
 *
 * <p>This component can be created using {@link IMappetUIBuilder#morph(AbstractMorph)} method.</p>
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        var ui = mappet.createUI().background();
 *        var layout = ui.layout();
 *
 *        layout.getCurrent().rx(0.5).ry(1).wh(300, 100).anchor(0.5, 1);
 *
 *        var steve = mappet.createMorph("{CustomPose:{Size:[0.6f,1.8f,0.6f],Poses:{right_arm:{P:[-6.0f,-2.0f,0.0f],R:[-83.0f,41.0f,0.0f]},left_leg:{P:[2.0f,-12.0f,0.0f]},right_armwear:{P:[0.0f,-4.0f,0.0f]},outer:{P:[0.0f,4.0f,0.0f]},left_legwear:{P:[0.0f,-6.0f,0.0f]},body:{P:[0.0f,8.0f,0.0f]},bodywear:{P:[0.0f,-6.0f,0.0f]},head:{P:[0.0f,8.0f,0.0f],R:[18.0f,0.0f,9.0f]},left_arm:{P:[6.0f,-2.0f,0.0f]},right_leg:{P:[-2.0f,-12.0f,0.0f]},right_legwear:{P:[0.0f,-6.0f,0.0f]},anchor:{P:[0.0f,16.0f,0.0f]},left_armwear:{P:[0.0f,-4.0f,0.0f]}}},Settings:{Hands:1b},Name:\"blockbuster.fred\"}");
 *        var morph = layout.morph(steve);
 *
 *        morph.position(-0.019, 1.5, 0).rotation(-11, 24).distance(1.6).fov(40);
 *        morph.enabled(false).wh(100, 100);
 *
 *        var label = layout.label("Steve").background(0xaa000000);
 *
 *        label.xy(0, 80).wh(100, 20).labelAnchor(0.5, 0.5);
 *
 *        var graphics = layout.graphics();
 *        var h = 54;
 *        var y = 30;
 *
 *        // Draw the background bubble
 *        graphics.xy(100, y).wh(200, 100);
 *        graphics.rect(4, 5, 192, h - 12, 0xff000000);
 *        graphics.rect(5, 4, 190, h - 10, 0xff000000);
 *        graphics.rect(5, 5, 190, h - 12, 0xffffffff);
 *        graphics.rect(4, 11, 1, 4, 0xffffffff);
 *        graphics.rect(3, 10, 1, 5, 0xff000000);
 *        graphics.rect(3, 11, 1, 3, 0xffffffff);
 *        graphics.rect(2, 10, 1, 4, 0xff000000);
 *        graphics.rect(2, 11, 1, 2, 0xffffffff);
 *        graphics.rect(1, 10, 1, 3, 0xff000000);
 *        graphics.rect(1, 11, 1, 1, 0xffffffff);
 *        graphics.rect(0, 10, 1, 2, 0xff000000);
 *        graphics.rect(-1, 10, 1, 1, 0xff000000);
 *
 *        var text = layout.text("Well, hello there! I expected you...\n\nMy name is Steve, and yours?");
 *
 *        text.color(0x000000, false).xy(110, y + 10).wh(180, 80);
 *
 *        c.getSubject().openUI(ui);
 *    }
 * }</pre>
 */
public class UIMorphComponent extends UIComponent
{
    public NBTTagCompound morph;
    public boolean editing;

    public Vector3f pos;
    public Vector2f rot;
    public float distance = 2F;
    public float fov = 70;

    /**
     * Set display morph.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *
     *    // Set Alex morph
     *    uiContext.get("morph").morph(mappet.createMorph('{Name:"blockbuster.alex"}'));
     * }</pre>
     */
    public UIMorphComponent morph(AbstractMorph morph)
    {
        this.change("Morph");

        this.morph = MorphUtils.toNBT(morph);

        return this;
    }

    /**
     * Enable an ability for players to pick or edit the morph.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *
     *    // Enable morph editing
     *    uiContext.get("morph").editing();
     * }</pre>
     */
    public UIMorphComponent editing()
    {
        return this.editing(true);
    }

    /**
     * Toggle an ability for players to pick or edit the morph.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *
     *    // Disable morph editing
     *    uiContext.get("morph").editing(false);
     * }</pre>
     */
    public UIMorphComponent editing(boolean editing)
    {
        this.change("Editing");

        this.editing = editing;

        return this;
    }

    /**
     * Change camera's orbit position in the morph component. The default camera position (<code>0</code>,
     * <code>1</code>, <code>0</code>).
     *
     * <p>ProTip: you can enable UI debug option in Ctrl + 0 &gt; Mappet, you can position the morph
     * after running the script, right click somewhere within its frame, and click Copy camera
     * information... context menu item. It will copy the configuration of camera, which you can
     * paste into the code.</p>
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *
     *    // Set camera position
     *    uiContext.get("morph").position(0, 1, 0.5);
     * }</pre>
     */
    public UIMorphComponent position(float x, float y, float z)
    {
        this.change("Position");

        this.pos = new Vector3f(x, y, z);

        return this;
    }

    /**
     * Change camera orbit rotation in the morph component. The default camera rotation (<code>0</code>, <code>0</code>).
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *
     *    // Set camera rotation
     *    uiContext.get("morph").rotation(15, 0);
     * }</pre>
     */
    public UIMorphComponent rotation(float pitch, float yaw)
    {
        this.change("Rotation");

        this.rot = new Vector2f(pitch, yaw);

        return this;
    }

    /**
     * Change camera distance from camera orbit position in the morph component. The default
     * camera distance is <code>2</code>.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *
     *    // Set camera distance
     *    uiContext.get("morph").distance(4);
     * }</pre>
     */
    public UIMorphComponent distance(float distance)
    {
        this.change("Distance");

        this.distance = distance;

        return this;
    }

    /**
     * Change camera Field of View in the morph component. The default FOV is <code>70</code>.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *
     *    // Set camera FOV
     *    uiContext.get("morph").fov(50);
     * }</pre>
     */
    public UIMorphComponent fov(float fov)
    {
        this.change("Fov");

        this.fov = fov;

        return this;
    }

    @Override
    @DiscardMethod
    protected int getDefaultUpdateDelay()
    {
        return UIComponent.DELAY;
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    protected void applyProperty(UIContext context, String key, GuiElement element)
    {
        super.applyProperty(context, key, element);

        GuiMorphRenderer renderer = (GuiMorphRenderer) element;

        if (key.equals("Morph"))
        {
            renderer.morph.set(MorphManager.INSTANCE.morphFromNBT(this.morph));
        }
        else if (key.equals("Editing"))
        {
            renderer.getChildren(GuiNestedEdit.class).get(0).setVisible(this.editing);
        }
        else if (key.equals("Position") && this.pos != null)
        {
            renderer.setPosition(this.pos.x, this.pos.y, this.pos.z);
        }
        else if (key.equals("Rotation") && this.rot != null)
        {
            renderer.setRotation(this.rot.y, this.rot.x);
        }
        else if (key.equals("Distance"))
        {
            renderer.scale = this.distance;
        }
        else if (key.equals("Fov"))
        {
            renderer.fov = this.fov;
        }
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiMorphRenderer renderer = new GuiMorphRenderer(mc);

        if (this.morph != null)
        {
            renderer.morph.set(MorphManager.INSTANCE.morphFromNBT(this.morph));
        }

        GuiNestedEdit edit = new GuiNestedEdit(mc, (editing) ->
        {
            GuiMappetDashboard.get(mc).openMorphMenu(renderer.getRoot(), editing, renderer.morph.copy(), (morph) ->
            {
                if (this.id.isEmpty())
                {
                    return;
                }

                AbstractMorph copy = MorphUtils.copy(morph);
                NBTTagCompound copyTag = MorphUtils.toNBT(copy);

                renderer.morph.setDirect(copy);
                context.data.setTag(this.id, copyTag == null ? new NBTTagCompound() : copyTag);
                context.dirty(this.id, this.updateDelay);
            });
        });

        edit.flex().relative(renderer).x(0.5F).y(1F, -30).wh(100, 20).anchorX(0.5F);
        edit.setVisible(this.editing);
        renderer.add(edit);

        if (Mappet.scriptUIDebug.get())
        {
            renderer.context(() -> new GuiSimpleContextMenu(mc).action(Icons.SEARCH, IKey.lang("mappet.gui.context.copy_camera"), () ->
            {
                StringBuilder builder = new StringBuilder();
                DecimalFormat formatter = GuiTrackpadElement.FORMAT;

                builder.append(".position(");
                builder.append(formatter.format(renderer.pos.x)).append(", ");
                builder.append(formatter.format(renderer.pos.y)).append(", ");
                builder.append(formatter.format(renderer.pos.z)).append(").rotation(");
                builder.append(formatter.format(renderer.pitch)).append(", ");
                builder.append(formatter.format(renderer.yaw)).append(").distance(");
                builder.append(formatter.format(renderer.scale)).append(").fov(");
                builder.append(formatter.format(renderer.fov)).append(")");

                GuiScreen.setClipboardString(builder.toString());
            }));
        }

        if (this.pos != null)
        {
            renderer.setPosition(this.pos.x, this.pos.y, this.pos.z);
        }

        if (this.rot != null)
        {
            renderer.setRotation(this.rot.y, this.rot.x);
        }

        renderer.scale = this.distance;
        renderer.fov = this.fov;

        return this.apply(renderer, context);
    }

    @Override
    @DiscardMethod
    public void populateData(NBTTagCompound tag)
    {
        super.populateData(tag);

        if (!this.id.isEmpty())
        {
            tag.setTag(this.id, this.morph.copy());
        }
    }

    @Override
    @DiscardMethod
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        if (this.morph != null)
        {
            tag.setTag("Morph", this.morph);
        }

        tag.setBoolean("Editing", this.editing);

        if (this.pos != null)
        {
            NBTTagList pos = new NBTTagList();

            NBTUtils.writeFloatList(pos, this.pos);
            tag.setTag("Position", pos);
        }

        if (this.rot != null)
        {
            NBTTagList rot = new NBTTagList();
            Vector3f rotation = new Vector3f(this.rot.x, this.rot.y, 0);

            NBTUtils.writeFloatList(rot, rotation);
            tag.setTag("Rotation", rot);
        }

        tag.setFloat("Distance", this.distance);
        tag.setFloat("Fov", this.fov);
    }

    @Override
    @DiscardMethod
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Morph"))
        {
            this.morph = tag.getCompoundTag("Morph");
        }

        if (tag.hasKey("Editing"))
        {
            this.editing = tag.getBoolean("Editing");
        }

        if (tag.hasKey("Position"))
        {
            Vector3f position = new Vector3f();

            NBTUtils.readFloatList(tag.getTagList("Position", Constants.NBT.TAG_FLOAT), position);

            this.pos = position;
        }

        if (tag.hasKey("Rotation"))
        {
            Vector3f rotation = new Vector3f();

            NBTUtils.readFloatList(tag.getTagList("Rotation", Constants.NBT.TAG_FLOAT), rotation);

            this.rot = new Vector2f(rotation.x, rotation.y);
        }

        if (tag.hasKey("Distance"))
        {
            this.distance = tag.getFloat("Distance");
        }

        if (tag.hasKey("Fov"))
        {
            this.fov = tag.getFloat("Fov");
        }
    }
}