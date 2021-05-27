package mchorse.mappet.api.data;

import mchorse.mappet.api.utils.manager.BaseManager;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.time.Instant;

public class DataManager extends BaseManager<Data>
{
    private File date;
    private Instant lastClear;

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
                this.lastClear = Instant.parse(FileUtils.readFileToString(this.date, Charset.defaultCharset()).trim());
            }
            catch (Exception e)
            {
                this.lastClear = Instant.EPOCH;
            }
        }

        return this.lastClear;
    }

    public void updateLastClear()
    {
        this.lastClear = Instant.now();

        try
        {
            FileUtils.writeStringToFile(this.date, this.lastClear.toString(), Charset.defaultCharset());
        }
        catch (Exception e)
        {}
    }

    @Override
    protected Data createData(NBTTagCompound tag)
    {
        Data data = new Data();

        if (tag != null)
        {
            data.deserializeNBT(tag);
        }

        return data;
    }
}