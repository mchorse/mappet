package mchorse.mappet.api.utils.manager;

import mchorse.mappet.api.utils.AbstractData;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Folder based manager
 */
public abstract class FolderManager <T extends AbstractData> implements IManager<T>
{
    private File folder;

    public FolderManager(File folder)
    {
        if (folder != null)
        {
            this.folder = folder;
            this.folder.mkdirs();
        }
    }

    @Override
    public boolean exists(String name)
    {
        return this.getFile(name).exists();
    }

    @Override
    public boolean rename(String id, String newId)
    {
        File file = this.getFile(id);

        if (file != null && file.exists())
        {
            return file.renameTo(this.getFile(newId));
        }

        return false;
    }

    @Override
    public boolean delete(String name)
    {
        File file = this.getFile(name);

        return file != null && file.delete();
    }

    @Override
    public Collection<String> getKeys()
    {
        List<String> list = new ArrayList<String>();

        if (this.folder == null)
        {
            return list;
        }

        for (File file : this.folder.listFiles())
        {
            String name = file.getName();

            if (file.isFile() && name.endsWith(this.getExtension()))
            {
                list.add(name.substring(0, name.lastIndexOf(".")));
            }
        }

        return list;
    }

    public File getFile(String name)
    {
        if (this.folder == null)
        {
            return null;
        }

        return new File(this.folder, name + this.getExtension());
    }

    protected String getExtension()
    {
        return ".json";
    }
}