package mchorse.mappet.api.data;

import mchorse.mappet.api.utils.manager.BaseManager;
import mchorse.mappet.utils.Utils;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.time.Instant;

public class DataManager extends BaseManager<Data>
{
    private File date;
    private Instant lastClear;
    private boolean lastInventory;

    public DataManager(File folder)
    {
        super(folder);

        if (folder != null)
        {
            this.date = new File(folder.getParentFile(), "date.txt");
        }
    }

    public Instant getLastClear()
    {
        if (this.lastClear == null)
        {
            try
            {
                String text = FileUtils.readFileToString(this.date, Utils.getCharset()).trim();
                String[] splits = text.split("\n");

                this.lastClear = Instant.parse(splits[0]);

                if (splits.length > 1)
                {
                    this.lastInventory = splits[1].trim().equals("1");
                }
            }
            catch (Exception e)
            {
                this.lastClear = Instant.EPOCH;
            }
        }

        return this.lastClear;
    }

    public boolean getLastInventory()
    {
        if (this.lastClear == null)
        {
            this.getLastClear();
        }

        return this.lastInventory;
    }

    public void updateLastClear(boolean inventory)
    {
        this.lastClear = Instant.now();

        try
        {
            FileUtils.writeStringToFile(this.date, this.lastClear.toString() + (inventory ? "\n1" : "\n0"), Utils.getCharset());
        }
        catch (Exception e)
        {}
    }

    @Override
    protected Data createData(String id, NBTTagCompound tag)
    {
        Data data = new Data();

        if (tag != null)
        {
            data.deserializeNBT(tag);
        }

        return data;
    }
}