package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.misc.ServerSettings;
import mchorse.mappet.api.utils.Trigger;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.utils.GuiTriggerElement;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.content.PacketRequestServerSettings;
import mchorse.mappet.network.common.content.PacketServerSettings;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.mclib.GuiDashboardPanel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

public class GuiServerSettingsPanel extends GuiDashboardPanel<GuiMappetDashboard>
{
    public GuiScrollElement editor;

    public GuiTriggerElement chat;
    public GuiTriggerElement placeBlock;
    public GuiTriggerElement breakBlock;
    public GuiTriggerElement damageEntity;
    public GuiTriggerElement serverInit;
    public GuiTriggerElement serverTick;

    private ServerSettings settings;

    public GuiServerSettingsPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.editor = new GuiScrollElement(mc);
        this.editor.flex().relative(this).wh(1F, 1F).column(5).scroll().width(180).padding(15);

        this.chat = new GuiTriggerElement(mc);
        this.placeBlock = new GuiTriggerElement(mc);
        this.breakBlock = new GuiTriggerElement(mc);
        this.damageEntity = new GuiTriggerElement(mc);
        this.serverInit = new GuiTriggerElement(mc);
        this.serverTick = new GuiTriggerElement(mc);

        this.add(this.editor);
        this.editor.add(Elements.label(IKey.lang("mappet.gui.settings.triggers.chat")).background(), this.chat);
        this.editor.add(Elements.label(IKey.lang("mappet.gui.settings.triggers.block_place")).background().marginTop(12), this.placeBlock);
        this.editor.add(Elements.label(IKey.lang("mappet.gui.settings.triggers.block_break")).background().marginTop(12), this.breakBlock);
        this.editor.add(Elements.label(IKey.lang("mappet.gui.settings.triggers.damage_entity")).background().marginTop(12), this.damageEntity);
        this.editor.add(Elements.label(IKey.lang("mappet.gui.settings.triggers.server_init")).background().marginTop(12), this.serverInit);
        this.editor.add(Elements.label(IKey.lang("mappet.gui.settings.triggers.server_tick")).background().marginTop(12), this.serverTick);
    }

    public void fill(NBTTagCompound tag)
    {
        this.settings = new ServerSettings(null);
        this.settings.deserializeNBT(tag);

        if (this.settings.chat == null) this.settings.chat = new Trigger();
        if (this.settings.placeBlock == null) this.settings.placeBlock = new Trigger();
        if (this.settings.breakBlock == null) this.settings.breakBlock = new Trigger();
        if (this.settings.damageEntity == null) this.settings.damageEntity = new Trigger();
        if (this.settings.serverInit == null) this.settings.serverInit = new Trigger();
        if (this.settings.serverTick == null) this.settings.serverTick = new Trigger();

        this.chat.set(this.settings.chat);
        this.placeBlock.set(this.settings.placeBlock);
        this.breakBlock.set(this.settings.breakBlock);
        this.damageEntity.set(this.settings.damageEntity);
        this.serverInit.set(this.settings.serverInit);
        this.serverTick.set(this.settings.serverTick);
    }

    public void save()
    {
        if (this.settings == null)
        {
            return;
        }

        Dispatcher.sendToServer(new PacketServerSettings(this.settings.serializeNBT()));
    }

    @Override
    public void appear()
    {
        super.appear();

        Dispatcher.sendToServer(new PacketRequestServerSettings());
    }

    @Override
    public void disappear()
    {
        super.disappear();

        this.save();
    }

    @Override
    public void close()
    {
        super.close();

        this.save();
    }
}