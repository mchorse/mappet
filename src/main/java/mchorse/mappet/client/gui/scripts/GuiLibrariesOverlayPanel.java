package mchorse.mappet.client.gui.scripts;

import mchorse.mappet.ClientProxy;
import mchorse.mappet.api.scripts.Script;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mappet.client.gui.utils.overlays.GuiContentNamesOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mappet.client.gui.utils.overlays.GuiStringOverlayPanel;
import mchorse.mappet.utils.Colors;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class GuiLibrariesOverlayPanel extends GuiStringOverlayPanel
{
    private List<String> libraries;
    private String main;

    public GuiLibrariesOverlayPanel(Minecraft mc, Script script)
    {
        super(mc, IKey.lang("mappet.gui.scripts.libraries.title"), false, script.libraries, null);

        this.libraries = script.libraries;
        this.main = script.getId();

        this.strings.context(() ->
        {
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(mc);

            menu.action(Icons.ADD, IKey.lang("mappet.gui.scripts.libraries.context.add"), this::addLibrary);

            if (!this.strings.list.isDeselected())
            {
                menu.action(Icons.REMOVE, IKey.lang("mappet.gui.scripts.libraries.context.remove"), this::removeLibrary, Colors.NEGATIVE);
            }

            return menu.shadow();
        });
        this.strings.list.sorting();
    }

    /* Context menu callbacks */

    private void addLibrary()
    {
        ContentType type = ContentType.SCRIPTS;

        ClientProxy.requestNames(type, (names) ->
        {
            for (String string : this.strings.list.getList())
            {
                names.remove(string);
            }

            List<String> tempNames = new ArrayList<>(names);

            for (String string : tempNames)
            {
                /**
                 * Checks if library extension don't match script extension
                 */
                if(!string.endsWith(this.main.substring(this.main.lastIndexOf("."))))
                {
                    names.remove(string);
                }
            }

            names.remove(this.main);

            GuiContentNamesOverlayPanel overlay = new GuiContentNamesOverlayPanel(Minecraft.getMinecraft(), type.getPickLabel(), type, names, null)
            {
                @Override
                public void onClose()
                {
                    String library = this.getValue();

                    if (library != null && !library.isEmpty())
                    {
                        GuiLibrariesOverlayPanel.this.strings.list.add(library);
                        GuiLibrariesOverlayPanel.this.strings.list.setCurrentScroll(library);
                    }

                    super.onClose();
                }
            };

            GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.5F, 0.7F);
        });
    }

    private void removeLibrary()
    {
        int index = this.strings.list.getIndex();
        String key = this.strings.list.getCurrentFirst();

        this.strings.list.remove(key);
        this.strings.list.setIndex(Math.max(index - 1, 0));
    }

    @Override
    public void onClose()
    {
        this.libraries.clear();
        this.libraries.addAll(this.strings.list.getList());

        super.onClose();
    }

    @Override
    protected void drawBackground(GuiContext context)
    {
        super.drawBackground(context);

        if (this.strings.list.getList().size() <= 1)
        {
            GuiMappetUtils.drawRightClickHere(context, this.area);
        }
    }
}