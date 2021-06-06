package mchorse.mappet.api.misc;

import mchorse.mappet.api.utils.Trigger;
import mchorse.mappet.utils.NBTToJsonLike;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Global server settings
 */
public class ServerSettings implements INBTSerializable<NBTTagCompound>
{
    private File file;

    public Trigger chat;
    public Trigger breakBlock;
    public Trigger placeBlock;
    public Trigger damageEntity;
    public Trigger serverInit;
    public Trigger serverTick;

    public ServerSettings(File file)
    {
        this.file = file;
    }

    public void load()
    {
        if (this.file == null || !this.file.isFile())
        {
            return;
        }

        try
        {
            String json = FileUtils.readFileToString(this.file, Charset.defaultCharset());
            NBTTagCompound tag = NBTToJsonLike.fromJson(json);

            this.deserializeNBT(tag);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void save()
    {
        try
        {
            NBTTagCompound tag = this.serializeNBT();

            FileUtils.writeStringToFile(this.file, NBTToJsonLike.toJson(tag), Charset.defaultCharset());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        this.writeTrigger(tag, "Chat", this.chat);
        this.writeTrigger(tag, "BreakBlock", this.breakBlock);
        this.writeTrigger(tag, "PlaceBlock", this.placeBlock);
        this.writeTrigger(tag, "DamageEntity", this.damageEntity);
        this.writeTrigger(tag, "ServerInit", this.serverInit);
        this.writeTrigger(tag, "ServerTick", this.serverTick);

        return tag;
    }

    private void writeTrigger(NBTTagCompound tag, String key, Trigger trigger)
    {
        if (trigger != null)
        {
            NBTTagCompound triggerTag = trigger.serializeNBT();

            if (!triggerTag.hasNoTags())
            {
                tag.setTag(key, triggerTag);
            }
        }
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.chat = this.readTrigger(tag, "Chat");
        this.breakBlock = this.readTrigger(tag, "BreakBlock");
        this.placeBlock = this.readTrigger(tag, "PlaceBlock");
        this.damageEntity = this.readTrigger(tag, "DamageEntity");
        this.serverInit = this.readTrigger(tag, "ServerInit");
        this.serverTick = this.readTrigger(tag, "ServerTick");
    }

    private Trigger readTrigger(NBTTagCompound tag, String key)
    {
        if (tag.hasKey(key, Constants.NBT.TAG_COMPOUND))
        {
            Trigger trigger = new Trigger();
            NBTTagCompound triggerTag = tag.getCompoundTag(key);

            if (!triggerTag.hasNoTags())
            {
                trigger.deserializeNBT(triggerTag);

                return trigger;
            }
        }

        return null;
    }
}