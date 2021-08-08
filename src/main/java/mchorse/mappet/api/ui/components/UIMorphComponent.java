package mchorse.mappet.api.ui.components;

import mchorse.mappet.Mappet;
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

public class UIMorphComponent extends UIComponent
{
    public NBTTagCompound morph;
    public boolean editing;

    public Vector3f pos;
    public Vector2f rot;
    public float scale = 2F;
    public float fov = 70;

    public UIMorphComponent morph(AbstractMorph morph)
    {
        this.change("Morph");

        this.morph = MorphUtils.toNBT(morph);

        return this;
    }

    public UIMorphComponent editing()
    {
        this.change("Editing");

        this.editing = true;

        return this;
    }

    public UIMorphComponent position(float x, float y, float z)
    {
        this.change("Position");

        this.pos = new Vector3f(x, y, z);

        return this;
    }

    public UIMorphComponent rotation(float pitch, float yaw)
    {
        this.change("Rotation");

        this.rot = new Vector2f(pitch, yaw);

        return this;
    }

    public UIMorphComponent scale(float scale)
    {
        this.change("Scale");

        this.scale = scale;

        return this;
    }

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
        else if (key.equals("Scale"))
        {
            renderer.scale = this.scale;
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
                builder.append(formatter.format(renderer.yaw)).append(").scale(");
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

        renderer.scale = this.scale;
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

        tag.setFloat("Scale", this.scale);
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

        if (tag.hasKey("Scale"))
        {
            this.scale = tag.getFloat("Scale");
        }

        if (tag.hasKey("Fov"))
        {
            this.fov = tag.getFloat("Fov");
        }
    }
}