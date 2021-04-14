package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.content.PacketContentData;
import mchorse.mappet.network.common.content.PacketContentRequestData;
import mchorse.mappet.network.common.content.PacketContentRequestNames;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiStringListElement;
import mchorse.mclib.client.gui.framework.elements.modals.GuiConfirmModal;
import mchorse.mclib.client.gui.framework.elements.modals.GuiModal;
import mchorse.mclib.client.gui.framework.elements.modals.GuiPromptModal;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDrawable;
import mchorse.mclib.client.gui.mclib.GuiDashboardPanel;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

public abstract class GuiMappetDashboardPanel <T extends INBTSerializable<NBTTagCompound>> extends GuiDashboardPanel<GuiMappetDashboard>
{
    public GuiIconElement toggleSidebar;
    public GuiElement sidebar;
    public GuiIconElement add;
    public GuiIconElement dupe;
    public GuiIconElement rename;
    public GuiIconElement remove;
    public GuiStringListElement names;

    public GuiScrollElement editor;

    protected boolean update;
    protected String id;
    protected T data;

    public GuiMappetDashboardPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.sidebar = new GuiElement(mc);
        this.sidebar.flex().relative(this).x(1F).w(200).h(1F).anchorX(1F);

        this.toggleSidebar = new GuiIconElement(mc, Icons.RIGHTLOAD, this::toggleSidebar);
        this.toggleSidebar.flex().relative(this.sidebar).x(-20);

        this.add = new GuiIconElement(mc, Icons.ADD, this::addNewData);
        this.dupe = new GuiIconElement(mc, Icons.DUPE, this::dupeData);
        this.rename = new GuiIconElement(mc, Icons.EDIT, this::renameData);
        this.remove = new GuiIconElement(mc, Icons.REMOVE, this::removeData);

        GuiDrawable drawable = new GuiDrawable((context) -> this.font.drawStringWithShadow("Data", this.names.area.x, this.area.y + 10, 0xffffff));

        this.names = new GuiStringListElement(mc, (list) -> this.pickData(list.get(0)));
        this.names.flex().relative(this.sidebar).xy(10, 25).w(1F, -20).h(1F, -35);
        this.sidebar.add(drawable, this.names);

        GuiElement buttons = new GuiElement(mc);

        buttons.flex().relative(this.names).x(1F).y(-20).anchorX(1F).row(0).resize();
        buttons.add(this.add, this.dupe, this.rename, this.remove);

        this.sidebar.add(buttons);

        this.editor = new GuiScrollElement(mc);
        this.editor.markContainer();
        this.editor.flex().relative(this).wTo(this.sidebar.area).h(1F).column(5).vertical().stretch().scroll();

        this.add(this.sidebar, this.editor, this.toggleSidebar);
    }

    private void toggleSidebar(GuiIconElement element)
    {
        this.sidebar.toggleVisible();
        this.toggleSidebar.both(this.sidebar.isVisible() ? Icons.RIGHTLOAD : Icons.LEFTLOAD);

        if (this.sidebar.isVisible())
        {
            this.toggleSidebar.flex().relative(this.sidebar).x(-20);
            this.editor.flex().wTo(this.sidebar.area);
        }
        else
        {
            this.editor.flex().w(1F);
            this.toggleSidebar.flex().relative(this).x(1F, -20);
        }

        this.resize();
    }

    /**
     * Get the content type of this panel
     */
    public abstract ContentType getType();

    protected void pickData(String id)
    {
        if (this.data != null && this.id != null)
        {
            Dispatcher.sendToServer(new PacketContentData(this.getType(), this.id, this.data.serializeNBT()));
        }

        Dispatcher.sendToServer(new PacketContentRequestData(this.getType(), id));
    }

    /* CRUD */

    protected void addNewData(GuiIconElement element)
    {
        GuiModal.addFullModal(this.sidebar, () -> new GuiPromptModal(this.mc, IKey.str("Type in name:"), this::addNewData));
    }

    protected void addNewData(String name)
    {
        if (!this.names.getList().contains(name))
        {
            Dispatcher.sendToServer(new PacketContentData(this.getType(), name, null));

            this.names.add(name);
            this.names.sort();
            this.names.setCurrentScroll(name);

            T data = (T) this.getType().getManager().create();

            this.fill(name, data);
        }
    }

    protected void dupeData(GuiIconElement element)
    {
        GuiModal.addFullModal(this.sidebar, () ->
        {
            GuiPromptModal promptModal = new GuiPromptModal(this.mc, IKey.str("Type in a new name for a duplicate:"), this::dupeData);

            return promptModal.setValue(this.id);
        });
    }

    protected void dupeData(String name)
    {
        if (this.data != null && !this.names.getList().contains(name))
        {
            Dispatcher.sendToServer(new PacketContentData(this.getType(), this.id, this.data.serializeNBT()));

            this.names.add(name);
            this.names.sort();
            this.names.setCurrentScroll(name);

            T data = (T) this.getType().getManager().create(this.data.serializeNBT());

            this.fill(name, data);
        }
    }

    protected void renameData(GuiIconElement element)
    {
        GuiModal.addFullModal(this.sidebar, () ->
        {
            GuiPromptModal promptModal = new GuiPromptModal(this.mc, IKey.str("Type in a new name:"), this::renameData);

            return promptModal.setValue(this.id);
        });
    }

    protected void renameData(String name)
    {
        if (this.id != null && !this.names.getList().contains(name))
        {
            Dispatcher.sendToServer(new PacketContentData(this.getType(), this.id).rename(name));

            this.names.remove(this.id);
            this.names.add(name);
            this.names.sort();
            this.names.setCurrentScroll(name);
        }
    }

    protected void removeData(GuiIconElement element)
    {
        GuiModal.addFullModal(this.sidebar, () -> new GuiConfirmModal(this.mc, IKey.str("Are you sure you want to remove this?"), this::removeData));
    }

    protected void removeData(boolean confirm)
    {
        if (this.id != null && confirm)
        {
            Dispatcher.sendToServer(new PacketContentData(this.getType(), this.id, null));

            this.names.remove(this.id);
            this.names.sort();
            this.names.setCurrentScroll("");
            this.fill("", null);
        }
    }

    /* Data population */

    public void fill(String id, T data)
    {
        this.id = id;
        this.data = data;
    }

    public void fillNames(List<String> names)
    {
        this.names.clear();
        this.names.add(names);
        this.names.sort();
    }

    @Override
    public void open()
    {
        super.open();

        this.update = true;
    }

    @Override
    public void appear()
    {
        super.appear();

        if (this.update)
        {
            this.update = false;

            Dispatcher.sendToServer(new PacketContentRequestNames(this.getType()));
        }
    }

    @Override
    public void close()
    {
        super.close();

        if (!this.update && this.data != null)
        {
            Dispatcher.sendToServer(new PacketContentData(this.getType(), this.id, this.data.serializeNBT()));
        }
    }

    @Override
    public void draw(GuiContext context)
    {
        if (this.sidebar.isVisible())
        {
            this.sidebar.area.draw(0xaa000000);
        }

        super.draw(context);
    }
}