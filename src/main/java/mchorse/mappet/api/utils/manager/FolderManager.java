package mchorse.mappet.api.utils.manager;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.utils.AbstractData;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Folder based manager
 */
public abstract class FolderManager <T extends AbstractData> implements IManager<T>
{
    protected Map<String, ManagerCache> cache = new HashMap<String, ManagerCache>();
    protected File folder;
    protected long lastCheck;

    public FolderManager(File folder)
    {
        if (folder != null)
        {
            this.folder = folder;
            this.folder.mkdirs();
        }
    }

    protected void doExpirationCheck()
    {
        final int threshold = 1000 * 30;
        long current = System.currentTimeMillis();

        /* Check every 30 seconds all cached entries and remove those that weren't used in
         * last 30 seconds */
        if (current - this.lastCheck > threshold)
        {
            this.cache.values().removeIf((cache) -> current - cache.lastUsed > threshold);

            this.lastCheck = current;
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
            if (file.renameTo(this.getFile(newId)))
            {
                if (Mappet.generalDataCaching.get())
                {
                    this.cache.put(newId, this.cache.remove(id));
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean delete(String name)
    {
        File file = this.getFile(name);

        if (file != null && file.delete())
        {
            this.cache.remove(name);

            return true;
        }

        return false;
    }

    @Override
    public Collection<String> getKeys()
    {
        Set<String> set = new HashSet<String>();

        if (this.folder == null)
        {
            return set;
        }

        this.recursiveFind(set, this.folder, "");

        return set;
    }

    protected void recursiveFind(Set<String> set, File folder, String prefix)
    {
        for (File file : folder.listFiles())
        {
            String name = file.getName();

            if (file.isFile() && name.endsWith(".json"))
            {
                set.add(prefix + name.replace(".json", ""));
            }
            else if (file.isDirectory())
            {
                if (file.listFiles().length > 0)
                {
                    this.recursiveFind(set, file, prefix + name + "/");
                }
                else
                {
                    set.add(prefix + name + "/");
                }
            }
        }
    }

    protected boolean isData(File file)
    {
        return file.getName().endsWith(this.getExtension());
    }

    public File getFile(String name)
    {
        if (this.folder == null)
        {
            return null;
        }

        return new File(this.folder, name + this.getExtension());
    }

    public File getFolder()
    {
        return this.folder;
    }

    protected String getExtension()
    {
        return ".json";
    }
}