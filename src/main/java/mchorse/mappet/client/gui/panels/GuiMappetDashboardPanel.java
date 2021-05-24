package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.utils.AbstractData;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.content.PacketContentData;
import mchorse.mappet.network.common.content.PacketContentRequestData;
import mchorse.mappet.network.common.content.PacketContentRequestNames;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.list.GuiStringSearchListElement;
import mchorse.mclib.client.gui.framework.elements.modals.GuiConfirmModal;
import mchorse.mclib.client.gui.framework.elements.modals.GuiModal;
import mchorse.mclib.client.gui.framework.elements.modals.GuiPromptModal;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDrawable;
import mchorse.mclib.client.gui.framework.elements.utils.GuiInventoryElement;
import mchorse.mclib.client.gui.mclib.GuiDashboardPanel;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Keyboard;

import java.util.List;

public abstract class GuiMappetDashboardPanel <T extends AbstractData> extends GuiDashboardPanel<GuiMappetDashboard>
{
    public static final IKey KEYS_CATEGORY = IKey.lang("mappet.gui.panels.keys.category");

    public GuiIconElement toggleSidebar;
    public GuiElement sidebar;

    public GuiElement buttons;
    public GuiIconElement add;
    public GuiIconElement dupe;
    public GuiIconElement rename;
    public GuiIconElement remove;
    public GuiStringSearchListElement names;

    public GuiScrollElement editor;
    public GuiInventoryElement inventory;

    protected boolean update;
    protected T data;

    public GuiMappetDashboardPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.buttons = new GuiElement(mc);

        this.sidebar = new GuiElement(mc);
        this.sidebar.flex().relative(this).x(1F).w(200).h(1F).anchorX(1F);

        this.toggleSidebar = new GuiIconElement(mc, Icons.RIGHTLOAD, (element) -> this.toggleSidebar());
        this.toggleSidebar.flex().relative(this.sidebar).x(-20);

        this.add = new GuiIconElement(mc, Icons.ADD, this::addNewData);
        this.dupe = new GuiIconElement(mc, Icons.DUPE, this::dupeData);
        this.rename = new GuiIconElement(mc, Icons.EDIT, this::renameData);
        this.remove = new GuiIconElement(mc, Icons.REMOVE, this::removeData);

        GuiDrawable drawable = new GuiDrawable((context) -> this.font.drawStringWithShadow(I18n.format(this.getTitle()), this.names.area.x, this.area.y + 10, 0xffffff));

        this.names = new GuiStringSearchListElement(mc, (list) -> this.pickData(list.get(0)));
        this.names.label(IKey.lang("mappet.gui.search"));
        this.names.flex().relative(this.sidebar).xy(10, 25).w(1F, -20).h(1F, -35);
        this.names.list.context(() ->
        {
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(mc);

            if (this.data != null)
            {
                menu.action(Icons.COPY, IKey.lang("mappet.gui.panels.context.copy"), this::copy);
            }

            try
            {
                NBTTagCompound tag = JsonToNBT.getTagFromJson(GuiScreen.getClipboardString());

                if (tag.getInteger("ContentType") == this.getType().ordinal())
                {
                    menu.action(Icons.PASTE, IKey.lang("mappet.gui.panels.context.paste"), () -> this.paste(tag));
                }
            }
            catch (Exception e)
            {}

            return menu.actions.getList().isEmpty() ? null : menu.shadow();
        });
        this.sidebar.add(drawable, this.names, this.buttons);

        this.editor = new GuiScrollElement(mc);
        this.editor.markContainer();
        this.editor.flex().relative(this).wTo(this.sidebar.area).h(1F).column(5).vertical().stretch().scroll();

        this.inventory = new GuiInventoryElement(mc, (stack) ->
        {
            this.inventory.linked.acceptStack(stack);
            this.inventory.unlink();
        });
        this.inventory.flex().relative(this.editor).xy(0.5F, 0.5F).anchor(0.5F, 0.5F);
        this.inventory.setVisible(false);

        this.buttons.flex().relative(this.names).x(1F).y(-20).anchorX(1F).row(0).resize();
        this.buttons.add(this.add, this.dupe, this.rename, this.remove);

        this.markContainer();
        this.add(this.sidebar, this.editor, this.toggleSidebar);

        this.keys().register(IKey.lang("mappet.gui.panels.keys.toggle_sidebar"), Keyboard.KEY_N, () -> this.toggleSidebar.clickItself(GuiBase.getCurrent())).category(KEYS_CATEGORY);
    }

    private void copy()
    {
        NBTTagCompound tag = this.data.serializeNBT();

        tag.setInteger("ContentType", this.getType().ordinal());
        GuiScreen.setClipboardString(tag.toString());
    }

    private void paste(NBTTagCompound tag)
    {
        T data = (T) this.getType().getManager().create("", tag);

        this.addNewData(this.add, data);
    }

    private void toggleSidebar()
    {
        this.sidebar.toggleVisible();
        this.toggleSidebar.both(this.sidebar.isVisible() ? Icons.RIGHTLOAD : Icons.LEFTLOAD);

        if (this.sidebar.isVisible())
        {
            this.toggleWithSidebar();
            this.toggleSidebar.flex().relative(this.sidebar).x(-20);
        }
        else
        {
            this.toggleFull();
            this.toggleSidebar.flex().relative(this).x(1F, -20);
        }

        this.resize();
    }

    protected void toggleWithSidebar()
    {
        this.editor.flex().wTo(this.sidebar.area);
    }

    protected void toggleFull()
    {
        this.editor.flex().w(1F);
    }

    /**
     * Get the content type of this panel
     */
    public abstract ContentType getType();

    public abstract String getTitle();

    public void pickData(String id)
    {
        this.save();

        Dispatcher.sendToServer(new PacketContentRequestData(this.getType(), id));
    }

    /* CRUD */

    protected void addNewData(GuiIconElement element)
    {
        this.addNewData(element, null);
    }

    protected void addNewData(GuiIconElement element, T data)
    {
        GuiModal.addFullModal(this.sidebar, () -> new GuiPromptModal(this.mc, IKey.lang("mappet.gui.panels.modals.add"), (name) -> this.addNewData(name, data)).filename());
    }

    protected void addNewData(String name, T data)
    {
        if (!this.names.list.getList().contains(name))
        {
            Dispatcher.sendToServer(new PacketContentData(this.getType(), name, data == null ? null : data.serializeNBT()));

            this.names.list.add(name);
            this.names.list.sort();
            this.names.list.setCurrentScroll(name);

            if (data == null)
            {
                data = (T) this.getType().getManager().create(name);
            }
            else
            {
                data.setId(name);
            }

            this.fill(data);
        }
    }

    protected void dupeData(GuiIconElement element)
    {
        GuiModal.addFullModal(this.sidebar, () ->
        {
            GuiPromptModal promptModal = new GuiPromptModal(this.mc, IKey.lang("mappet.gui.panels.modals.dupe"), this::dupeData);

            return promptModal.setValue(this.data.getId()).filename();
        });
    }

    protected void dupeData(String name)
    {
        if (this.data != null && !this.names.list.getList().contains(name))
        {
            Dispatcher.sendToServer(new PacketContentData(this.getType(), this.data.getId(), this.data.serializeNBT()));

            this.names.list.add(name);
            this.names.list.sort();
            this.names.list.setCurrentScroll(name);

            T data = (T) this.getType().getManager().create(name, this.data.serializeNBT());

            this.fill(data);
        }
    }

    protected void renameData(GuiIconElement element)
    {
        GuiModal.addFullModal(this.sidebar, () ->
        {
            GuiPromptModal promptModal = new GuiPromptModal(this.mc, IKey.lang("mappet.gui.panels.modals.rename"), this::renameData);

            return promptModal.setValue(this.data.getId()).filename();
        });
    }

    protected void renameData(String name)
    {
        if (this.data != null && !this.names.list.getList().contains(name))
        {
            Dispatcher.sendToServer(new PacketContentData(this.getType(), this.data.getId()).rename(name));

            this.names.list.remove(this.data.getId());
            this.names.list.add(name);
            this.names.list.sort();
            this.names.list.setCurrentScroll(name);

            this.data.setId(name);
        }
    }

    protected void removeData(GuiIconElement element)
    {
        GuiModal.addFullModal(this.sidebar, () -> new GuiConfirmModal(this.mc, IKey.lang("mappet.gui.panels.modals.remove"), this::removeData));
    }

    protected void removeData(boolean confirm)
    {
        if (this.data != null && confirm)
        {
            Dispatcher.sendToServer(new PacketContentData(this.getType(), this.data.getId(), null));

            this.names.list.remove(this.data.getId());
            this.names.list.sort();
            this.names.list.setCurrentScroll("");
            this.fill(null);
        }
    }

    /* Data population */

    public void fill(T data)
    {
        this.data = data;
    }

    public void fillNames(List<String> names)
    {
        String value = this.names.list.getCurrentFirst();

        this.names.list.clear();
        this.names.list.add(names);
        this.names.list.sort();
        this.names.list.setCurrentScroll(value);
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

            this.requestDataNames();
        }
    }

    public void requestDataNames()
    {
        Dispatcher.sendToServer(new PacketContentRequestNames(this.getType()));
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

    public void save()
    {
        if (!this.update && this.data != null)
        {
            Dispatcher.sendToServer(new PacketContentData(this.getType(), this.data.getId(), this.data.serializeNBT()));
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