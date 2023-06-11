package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.utils.AbstractData;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.utils.GuiStringFolderList;
import mchorse.mappet.client.gui.utils.GuiStringFolderSearchListElement;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.content.PacketContentData;
import mchorse.mappet.network.common.content.PacketContentFolder;
import mchorse.mappet.network.common.content.PacketContentRequestData;
import mchorse.mappet.network.common.content.PacketContentRequestNames;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.modals.GuiConfirmModal;
import mchorse.mclib.client.gui.framework.elements.modals.GuiModal;
import mchorse.mclib.client.gui.framework.elements.modals.GuiPromptModal;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDrawable;
import mchorse.mclib.client.gui.mclib.GuiDashboardPanel;
import mchorse.mclib.client.gui.utils.GuiUtils;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.io.FilenameUtils;
import org.lwjgl.input.Keyboard;

import java.util.List;

public abstract class GuiMappetDashboardPanel <T extends AbstractData> extends GuiDashboardPanel<GuiMappetDashboard>
{
    public static final IKey KEYS_CATEGORY = IKey.lang("mappet.gui.panels.keys.category");

    public GuiElement iconBar;
    public GuiIconElement toggleSidebar;
    public GuiElement sidebar;

    public GuiElement buttons;
    public GuiIconElement add;
    public GuiIconElement dupe;
    public GuiIconElement rename;
    public GuiIconElement remove;
    public GuiStringFolderSearchListElement names;
    public GuiStringFolderList namesList;

    public GuiElement editor;
    protected boolean update;
    protected T data;
    protected boolean allowed;
    protected boolean save;

    public GuiMappetDashboardPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.buttons = new GuiElement(mc);

        this.sidebar = new GuiElement(mc);
        this.sidebar.flex().relative(this).x(1F).w(200).h(1F).anchorX(1F);
        this.iconBar = new GuiElement(mc);
        this.iconBar.flex().relative(this.sidebar).x(-20).w(20).h(1F).column(0).stretch();

        this.toggleSidebar = new GuiIconElement(mc, Icons.RIGHTLOAD, (element) -> this.toggleSidebar());
        this.iconBar.add(this.toggleSidebar);

        this.add = new GuiIconElement(mc, Icons.ADD, this::addNewData);
        this.add.context(() ->
        {
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(mc);

            menu.action(Icons.ADD, IKey.lang("mappet.gui.panels.context.add_folder"), this::addFolder);

            return menu.shadow();
        });
        this.dupe = new GuiIconElement(mc, Icons.DUPE, this::dupeData);
        this.rename = new GuiIconElement(mc, Icons.EDIT, this::renameData);
        this.rename.context(() ->
        {
            if (this.namesList.getPath().isEmpty())
            {
                return null;
            }

            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(mc);

            menu.action(Icons.EDIT, IKey.lang("mappet.gui.panels.context.rename_folder"), this::renameFolder);

            return menu.shadow();
        });
        this.remove = new GuiIconElement(mc, Icons.REMOVE, this::removeData);
        this.remove.context(() ->
        {
            if (this.namesList.getPath().isEmpty())
            {
                return null;
            }

            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(mc);

            menu.action(Icons.REMOVE, IKey.lang("mappet.gui.panels.context.remove_folder"), this::removeFolder);

            return menu.shadow();
        });

        GuiDrawable drawable = new GuiDrawable((context) -> this.font.drawStringWithShadow(I18n.format(this.getTitle()), this.names.area.x, this.area.y + 10, 0xffffff));

        this.names = new GuiStringFolderSearchListElement(mc, (list) -> this.pickData(list.get(0)));
        this.namesList = (GuiStringFolderList) this.names.list;
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

                if (tag.getInteger("_ContentType") == this.getType().ordinal())
                {
                    menu.action(Icons.PASTE, IKey.lang("mappet.gui.panels.context.paste"), () -> this.paste(tag));
                }
            }
            catch (Exception e)
            {}

            if (mc.isSingleplayer())
            {
                menu.action(Icons.FOLDER, IKey.lang("mappet.gui.panels.context.open_folder"), () ->
                {
                    String path = this.getType().getManager().getFolder().getAbsolutePath() + "/" + this.namesList.getPath();

                    GuiUtils.openFolder(path);
                });
            }

            return menu.actions.getList().isEmpty() ? null : menu.shadow();
        });
        this.sidebar.add(drawable, this.names, this.buttons);

        this.editor = new GuiElement(mc);
        this.editor.flex().relative(this).wTo(this.iconBar.area).h(1F);

        this.buttons.flex().relative(this.names).x(1F).y(-20).anchorX(1F).row(0).resize();
        this.buttons.add(this.add, this.dupe, this.rename, this.remove);

        this.markContainer();
        this.add(this.sidebar, this.iconBar, this.editor);

        this.keys().register(IKey.lang("mappet.gui.panels.keys.toggle_sidebar"), Keyboard.KEY_N, () -> this.toggleSidebar.clickItself(GuiBase.getCurrent())).category(KEYS_CATEGORY);
    }

    private void copy()
    {
        NBTTagCompound tag = this.data.serializeNBT();

        tag.setInteger("_ContentType", this.getType().ordinal());
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
            this.iconBar.flex().relative(this.sidebar).x(-20);
        }
        else
        {
            this.toggleFull();
            this.iconBar.flex().relative(this).x(1F, -20);
        }

        this.resize();
    }

    protected void toggleWithSidebar()
    {
        this.editor.flex().wTo(this.iconBar.area);
    }

    protected void toggleFull()
    {
        this.editor.flex().wTo(this.iconBar.area);
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
        GuiModal.addFullModal(this.sidebar, () -> new GuiPromptModal(this.mc, IKey.lang("mappet.gui.panels.modals.add"), (name) -> this.addNewData(this.namesList.getPath(name), data)).filename());
    }

    protected void addNewData(String name, T data)
    {
        if (!this.namesList.hasInHierarchy(name))
        {
            this.save();

            Dispatcher.sendToServer(new PacketContentData(this.getType(), name, data == null ? new NBTTagCompound() : data.serializeNBT()));

            this.namesList.addFile(name);

            if (data == null)
            {
                data = (T) this.getType().getManager().create(name);
                this.fillDefaultData(data);
                this.getType().getManager().create(data.getId(), data.serializeNBT());
            }
            else
            {
                data.setId(name);
            }

            this.fill(data);
        }
    }

    private void addFolder()
    {
        GuiModal.addFullModal(this.sidebar, () -> new GuiPromptModal(this.mc, IKey.lang("mappet.gui.panels.modals.add_folder"), this::addFolder).filename());
    }

    private void addFolder(String name)
    {
        Dispatcher.sendToServer(new PacketContentFolder(this.getType(), name, this.namesList.getPath("")));
    }

    private void renameFolder()
    {
        if (this.namesList.getPath().isEmpty())
        {
            return;
        }

        String name = FilenameUtils.getBaseName(this.namesList.getPath());

        GuiModal.addFullModal(this.sidebar, () -> new GuiPromptModal(this.mc, IKey.lang("mappet.gui.panels.modals.rename_folder"), this::renameFolder).filename().setValue(name));
    }

    private void renameFolder(String name)
    {
        String path = this.namesList.getPath("");

        Dispatcher.sendToServer(new PacketContentFolder(this.getType(), "", path.substring(0, path.length() - 1)).rename(name));
        this.fill(null);
    }

    private void removeFolder()
    {
        if (!this.namesList.getPath().isEmpty())
        {
            return;
        }

        GuiModal.addFullModal(this.sidebar, () -> new GuiConfirmModal(this.mc, IKey.lang("mappet.gui.panels.modals.remove_folder"), this::removeFolder));
    }

    private void removeFolder(Boolean isDelete)
    {
        if (isDelete)
        {
            String path = this.namesList.getPath("");
            Dispatcher.sendToServer(new PacketContentFolder(this.getType(), "", path.substring(0, path.length() - 1)).delete());
        }
    }

    protected void fillDefaultData(T data)
    {}

    protected void dupeData(GuiIconElement element)
    {
        if (this.data == null)
        {
            return;
        }

        GuiModal.addFullModal(this.sidebar, () ->
        {
            GuiPromptModal promptModal = new GuiPromptModal(this.mc, IKey.lang("mappet.gui.panels.modals.dupe"), this::dupeData);

            return promptModal.setValue(this.data.getId()).filename();
        });
    }

    protected void dupeData(String name)
    {
        if (!this.namesList.hasInHierarchy(name))
        {
            this.save();

            Dispatcher.sendToServer(new PacketContentData(this.getType(), name, this.data.serializeNBT()));

            this.namesList.addFile(name);

            T data = (T) this.getType().getManager().create(name, this.data.serializeNBT());

            this.fill(data);
        }
    }

    protected void renameData(GuiIconElement element)
    {
        if (this.data == null)
        {
            return;
        }

        GuiModal.addFullModal(this.sidebar, () ->
        {
            GuiPromptModal promptModal = new GuiPromptModal(this.mc, IKey.lang("mappet.gui.panels.modals.rename"), this::renameData);

            return promptModal.setValue(this.data.getId().substring(this.data.getId().lastIndexOf('/') + 1)).filename();
        });
    }

    protected void renameData(String name)
    {
        if (!this.namesList.hasInHierarchy(name))
        {
            String path = this.getDataPath();
            Dispatcher.sendToServer(new PacketContentData(this.getType(), this.data.getId(), this.data.serializeNBT()).rename(path + name));

            this.namesList.removeFile(this.data.getId());
            this.namesList.addFile(path + name);

            this.data.setId(path + name);
        }
    }

    protected String getDataPath()
    {
        String output = "";
        int index = this.data.getId().lastIndexOf('/');

        if (index != -1)
        {
            output = this.data.getId().substring(0, index + 1);
        }

        return output;
    }

    protected void removeData(GuiIconElement element)
    {
        if (this.data == null)
        {
            return;
        }

        GuiModal.addFullModal(this.sidebar, () -> new GuiConfirmModal(this.mc, IKey.lang("mappet.gui.panels.modals.remove"), this::removeData));
    }

    protected void removeData(boolean confirm)
    {
        if (this.data != null && confirm)
        {
            Dispatcher.sendToServer(new PacketContentData(this.getType(), this.data.getId(), null));

            this.namesList.removeFile(this.data.getId());
            this.fill(null);
        }
    }

    /* Data population */

    public final void fill(T data)
    {
        this.fill(data, true);
    }

    public void fill(T data, boolean allowed)
    {
        this.data = data;
        this.allowed = allowed;
        this.editor.setEnabled(allowed);
        this.remove.setEnabled(allowed);
        this.rename.setEnabled(allowed);
    }

    public void fillNames(List<String> names)
    {
        String value = this.data == null ? null : this.data.getId();

        this.namesList.fill(names);
        this.namesList.sort();
        this.namesList.setCurrentFile(value);
    }

    protected GuiScrollElement createScrollEditor()
    {
        GuiScrollElement scrollEditor = new GuiScrollElement(mc);

        scrollEditor.flex().relative(this.editor).wh(1F, 1F).column(5).stretch().vertical().scroll().padding(10);

        return scrollEditor;
    }

    @Override
    public void open()
    {
        super.open();

        this.update = true;
        this.save = true;
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

        if (this.data != null)
        {
            Dispatcher.sendToServer(new PacketContentRequestData(this.getType(), this.data.getId()));
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

        if (this.save)
        {
            this.save();
        }
    }

    @Override
    public void close()
    {
        super.close();

        if (this.save)
        {
            this.save();
        }
    }

    public void save()
    {
        if (!this.update && this.data != null && this.editor.isEnabled())
        {
            this.preSave();
            Dispatcher.sendToServer(new PacketContentData(this.getType(), this.data.getId(), this.data.serializeNBT()));
        }
    }

    protected void preSave()
    {}

    @Override
    public void draw(GuiContext context)
    {
        this.iconBar.area.draw(0x77000000);
        GuiDraw.drawHorizontalGradientRect(this.iconBar.area.x - 6, this.iconBar.area.y, this.iconBar.area.x, this.iconBar.area.ey(), 0, 0x29000000);

        if (this.sidebar.isVisible())
        {
            this.sidebar.area.draw(0xaa000000);
        }

        super.draw(context);

        if (!this.editor.isEnabled() && this.data != null)
        {
            GuiDraw.drawLockedArea(this.editor);
        }
    }
}