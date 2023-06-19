package mchorse.mappet.client.gui;

import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.client.gui.triggers.GuiTriggerElement;
import mchorse.mappet.client.gui.utils.GuiVecPosElement;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.blocks.PacketEditTrigger;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiCollapseSection;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class GuiTriggerBlockScreen extends GuiBase
{
    public GuiTriggerElement left;
    public GuiTriggerElement right;
    public GuiToggleElement collidable;
    public GuiVecPosElement boundingBoxPos1;
    public GuiVecPosElement boundingBoxPos2;

    private BlockPos pos;

    public GuiTriggerBlockScreen(BlockPos pos, Trigger left, Trigger right, boolean collidable, Vec3d pos1, Vec3d pos2)
    {
        super();

        this.pos = pos;

        Minecraft mc = Minecraft.getMinecraft();
        GuiElement element = new GuiElement(mc);

        element.flex().relative(this.viewport).xy(0.5F, 0.5F).w(0.5F).anchor(0.5F, 0.5F).column(5).vertical().stretch();

        this.left = new GuiTriggerElement(mc);
        this.left.set(left);

        this.right = new GuiTriggerElement(mc);
        this.right.set(right);

        this.collidable = new GuiToggleElement(mc, IKey.lang("mappet.gui.trigger_block.collidable"), null);
        this.collidable.toggled(collidable);

        this.boundingBoxPos1 = new GuiVecPosElement(mc, null);
        this.boundingBoxPos1.set(pos1);
        this.sanitizeTrackpads(this.boundingBoxPos1);

        this.boundingBoxPos2 = new GuiVecPosElement(mc, null);
        this.boundingBoxPos2.set(pos2);
        this.sanitizeTrackpads(this.boundingBoxPos2);

        GuiCollapseSection collapseSection = new GuiCollapseSection(mc, IKey.lang("mappet.gui.trigger_block.bounding_box.title"), null,true);

        collapseSection.addFields(Elements.row(
                mc,
                2,
                Elements.label(IKey.lang("mappet.gui.trigger_block.bounding_box.pos1")).background().marginTop(6),
                this.boundingBoxPos1));
        collapseSection.addFields(Elements.row(
                mc,
                2,
                Elements.label(IKey.lang("mappet.gui.trigger_block.bounding_box.pos2")).background().marginTop(6),
                this.boundingBoxPos2));

        collapseSection.context(() ->
        {
            GuiSimpleContextMenu menu =  new GuiSimpleContextMenu(mc)
                    .action(Icons.MINIMIZE, IKey.lang("mappet.gui.trigger_block.bounding_box.center"), () ->
            {
                double sizeX = (this.boundingBoxPos2.x.value - this.boundingBoxPos1.x.value);
                double sizeY = (this.boundingBoxPos2.y.value - this.boundingBoxPos1.y.value);
                double sizeZ = (this.boundingBoxPos2.z.value - this.boundingBoxPos1.z.value);

                this.boundingBoxPos1.x.setValue(0.5 - sizeX / 2);
                this.boundingBoxPos1.y.setValue(0.5 - sizeY / 2);
                this.boundingBoxPos1.z.setValue(0.5 - sizeZ / 2);

                this.boundingBoxPos2.x.setValue(0.5 + sizeX / 2);
                this.boundingBoxPos2.y.setValue(0.5 + sizeY / 2);
                this.boundingBoxPos2.z.setValue(0.5 + sizeZ / 2);
            })
                    .action(Icons.FULLSCREEN, IKey.lang("mappet.gui.trigger_block.bounding_box.reset"), () ->
                    {
                        this.boundingBoxPos1.set(Vec3d.ZERO);
                        this.boundingBoxPos2.set(new Vec3d(1, 1, 1));
                    });

            return menu;
        });


        element.add(Elements.label(IKey.lang("mappet.gui.trigger_block.left")).background().marginBottom(5), this.left);
        element.add(Elements.label(IKey.lang("mappet.gui.trigger_block.right")).background().marginTop(12).marginBottom(5), this.right, this.collidable.marginTop(6));
        element.add(collapseSection.marginTop(12));

        this.root.add(element);
    }

    public void sanitizeTrackpads(GuiVecPosElement guiVecPosElement)
    {
        guiVecPosElement.clamp(Vec3d.ZERO, new Vec3d(1, 1, 1));
        guiVecPosElement.x.normal = 0.05;
        guiVecPosElement.x.increment = 0.05;
        guiVecPosElement.y.normal = 0.05;
        guiVecPosElement.y.increment = 0.05;
        guiVecPosElement.z.normal = 0.05;
        guiVecPosElement.z.increment = 0.05;
        guiVecPosElement.context(null);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    protected void closeScreen()
    {
        super.closeScreen();

        Dispatcher.sendToServer(new PacketEditTrigger(
                this.pos,
                this.left.get().serializeNBT(),
                this.right.get().serializeNBT(),
                this.collidable.isToggled(),
                this.boundingBoxPos1.get(),
                this.boundingBoxPos2.get()
        ));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}