package mchorse.mappet.client.gui;

import mchorse.mappet.client.gui.triggers.GuiTriggerElement;
import mchorse.mappet.common.ScriptedItemProps;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.items.PacketScriptedItemInfo;
import mchorse.mappet.utils.NBTUtils;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiScriptedItemScreen extends GuiBase {
    public ScriptedItemProps props;
    GuiTriggerElement interactWithAir;
    GuiTriggerElement interactWithEntity;
    GuiTriggerElement interactWithBlock;
    GuiTriggerElement attackEntity;
    GuiTriggerElement breakBlock;
    GuiTriggerElement placeBlock;
    GuiTriggerElement hitBlock;
    GuiTriggerElement onHolderTick;
    GuiTriggerElement pickup;

    public GuiScriptedItemScreen(ItemStack stack)
    {
        super();
        this.props = NBTUtils.getScriptedItemProps(stack);
        this.props.storeInitialState();

        Minecraft mc = Minecraft.getMinecraft();
        GuiScrollElement scroll = new GuiScrollElement(mc);
        scroll.flex().relative(this.viewport).xy(0.5F, 0.5F).w(0.5F).h(1F).anchor(0.5F, 0.5F).column(5).vertical().stretch().scroll().padding(10);
        this.interactWithAir = new GuiTriggerElement(mc, props.interactWithAir);
        this.interactWithEntity = new GuiTriggerElement(mc, props.interactWithEntity);
        this.interactWithBlock = new GuiTriggerElement(mc, props.interactWithBlock);
        this.attackEntity = new GuiTriggerElement(mc, props.attackEntity);
        this.breakBlock = new GuiTriggerElement(mc, props.breakBlock);
        this.placeBlock = new GuiTriggerElement(mc, props.placeBlock);
        this.hitBlock = new GuiTriggerElement(mc, props.hitBlock);
        this.onHolderTick = new GuiTriggerElement(mc, props.onHolderTick);
        this.pickup = new GuiTriggerElement(mc, props.pickup);

        scroll.add(Elements.label(IKey.lang("mappet.gui.scripted_item.interact_with_air")).background().marginTop(12).marginBottom(5), this.interactWithAir.marginTop(6));
        scroll.add(Elements.label(IKey.lang("mappet.gui.scripted_item.interact_with_entity")).background().marginTop(12).marginBottom(5), this.interactWithEntity.marginTop(6));
        scroll.add(Elements.label(IKey.lang("mappet.gui.scripted_item.interact_with_block")).background().marginTop(12).marginBottom(5), this.interactWithBlock.marginTop(6));
        scroll.add(Elements.label(IKey.lang("mappet.gui.scripted_item.attack_entity")).background().marginTop(12).marginBottom(5), this.attackEntity.marginTop(6));
        scroll.add(Elements.label(IKey.lang("mappet.gui.scripted_item.break_block")).background().marginTop(12).marginBottom(5), this.breakBlock.marginTop(6));
        scroll.add(Elements.label(IKey.lang("mappet.gui.scripted_item.place_block")).background().marginTop(12).marginBottom(5), this.placeBlock.marginTop(6));
        scroll.add(Elements.label(IKey.lang("mappet.gui.scripted_item.hit_block")).background().marginTop(12).marginBottom(5), this.hitBlock.marginTop(6));
        scroll.add(Elements.label(IKey.lang("mappet.gui.scripted_item.on_holder_tick")).background().marginTop(12).marginBottom(5), this.onHolderTick.marginTop(6));
        scroll.add(Elements.label(IKey.lang("mappet.gui.scripted_item.pickup")).background().marginTop(12).marginBottom(5), this.pickup.marginTop(6));
        this.root.add(scroll);
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
        if (this.props.hasChanged()) {
            Dispatcher.sendToServer(new PacketScriptedItemInfo(this.props.toNBT(), 0));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        String title = I18n.format("mappet.gui.scripted_item.title");

        GuiDraw.drawTextBackground(this.fontRenderer, title, this.viewport.mx(this.fontRenderer.getStringWidth(title)), this.viewport.y + 20, 0xffffff, ColorUtils.HALF_BLACK);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}