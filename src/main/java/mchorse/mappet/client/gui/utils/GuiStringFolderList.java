package mchorse.mappet.client.gui.utils;

import mchorse.mclib.client.gui.framework.elements.list.GuiStringListElement;
import mchorse.mclib.client.gui.utils.Icon;
import mchorse.mclib.client.gui.utils.Icons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class GuiStringFolderList extends GuiStringListElement
{
    public GuiStringFolderList(Minecraft mc, Consumer<List<String>> callback)
    {
        super(mc, callback);

        this.callback = (l) -> this.fileCallback(callback, l);
    }

    /**
     * A list of paths.
     */
    private List<String> hierarchy = new ArrayList<String>();

    /**
     * Path in which current list is located. It's expected to be
     * something like "abc/def/ghi" (i.e. without trailing slash).
     */
    private String path = "";

    /**
     * Icon that is used to render "files."
     */
    private Icon fileIcon = Icons.FILE;

    private void fileCallback(Consumer<List<String>> callback, List<String> strings)
    {
        String path = strings.get(0);

        if (path.endsWith("/"))
        {
            String newPath;

            if (path.equals("../"))
            {
                int index = this.path.lastIndexOf('/');

                newPath = index == -1 ? "" : this.path.substring(0, index);
            }
            else
            {
                path = path.substring(0, path.length() - 1);
                newPath = this.getPath(path);
            }

            this.goTo(newPath);
        }
        else
        {
            strings.clear();
            strings.add(this.getPath(path));

            callback.accept(strings);
        }
    }

    public void setFileIcon(Icon icon)
    {
        this.fileIcon = icon;
    }

    public String getPath()
    {
        return this.path;
    }

    public String getPath(String name)
    {
        if (this.path.isEmpty())
        {
            return name;
        }

        return this.path + "/" + name;
    }

    public void fill(Collection<String> hierarchy)
    {
        this.hierarchy.clear();
        this.hierarchy.addAll(hierarchy);

        this.goTo("");
    }

    private void goTo(String path)
    {
        this.path = path;

        this.filter("");
        this.setIndex(-1);
        this.updateStrings();
    }

    private void updateStrings()
    {
        Set<String> files = new HashSet<String>();

        if (!this.path.isEmpty())
        {
            files.add("../");
        }

        for (String path : this.hierarchy)
        {
            if (this.path.isEmpty())
            {
                int i = path.indexOf('/');

                if (i < 0)
                {
                    files.add(path);
                }
                else
                {
                    files.add(path.substring(0, i + 1));
                }
            }
            else if (path.startsWith(this.path + '/') && path.indexOf('/') > 0)
            {
                String newPath = path.substring(this.path.length() + 1);
                int index = newPath.indexOf('/');

                if (index >= 0)
                {
                    newPath = newPath.substring(0, index + 1);
                }

                if (newPath.equals(""))
                {
                    continue;
                }

                files.add(newPath);
            }
        }

        this.list.clear();
        this.list.addAll(files);

        this.sort();
        this.update();
    }

    public boolean hasInHierarchy(String path)
    {
        return this.hierarchy.contains(path);
    }

    /**
     * Add file path to this hierarchy.
     */
    public void addFile(String path)
    {
        String filename = this.getFilename(path);

        if (filename != null)
        {
            this.hierarchy.add(path);

            this.add(filename);
            this.sort();
            this.setCurrentFile(path);
        }
    }

    /**
     * Removes given path from the hierarchy and currently displayed list.
     */
    public void removeFile(String path)
    {
        String filename = this.getFilename(path);

        if (filename != null && this.hasInHierarchy(path))
        {
            this.hierarchy.remove(path);

            this.remove(filename);
            this.setIndex(-1);
        }
    }

    /**
     * Get the filename of the path. It returns filename only if
     * given path matches the current path in the hierarchy, otherwise
     * it will return {@code null}.
     */
    private String getFilename(String path)
    {
        int lastIndex = path.lastIndexOf('/');

        if (this.path.isEmpty() && lastIndex < 0)
        {
            return path;
        }
        else if (path.startsWith(this.path))
        {
            String filename = path.substring(this.path.length() + 1);

            if (filename.indexOf('/') < 0)
            {
                return filename;
            }
        }

        return null;
    }

    /**
     * It's like {@link #setCurrentScroll(Object)} but using file paths
     * listed in {@link #hierarchy}.
     */
    public void setCurrentFile(String path)
    {
        if (path == null)
        {
            return;
        }

        int lastIndex = path.lastIndexOf('/');

        if (lastIndex < 0)
        {
            this.goTo("");
            this.setCurrentScroll(path);
        }
        else
        {
            this.goTo(path.substring(0, lastIndex));
            this.setCurrentScroll(path.substring(lastIndex + 1));
        }
    }

    @Override
    protected void drawElementPart(String element, int i, int x, int y, boolean hover, boolean selected)
    {
        GlStateManager.color(1, 1, 1, 1);
        (element.endsWith("/") ? Icons.FOLDER : this.fileIcon).render(x, y);

        super.drawElementPart(element, i, x + 12, y, hover, selected);
    }

    @Override
    protected boolean sortElements()
    {
        this.list.sort((p1, p2) ->
        {
            int isP1Folder = p1.equals("../") ? Integer.MAX_VALUE : p1.endsWith("/") ? 1 : 0;
            int isP2Folder = p2.equals("../") ? Integer.MAX_VALUE : p2.endsWith("/") ? 1 : 0;

            if (isP1Folder == isP2Folder)
            {
                return p1.compareTo(p2);
            }

            return (isP2Folder) - (isP1Folder);
        });

        return true;
    }
}
