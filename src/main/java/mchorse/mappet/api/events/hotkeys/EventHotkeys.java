package mchorse.mappet.api.events.hotkeys;

import mchorse.mappet.utils.NBTToJsonLike;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class EventHotkeys implements INBTSerializable<NBTTagCompound>
{
    public List<EventHotkey> hotkeys = new ArrayList<EventHotkey>();

    private File file;

    public void execute(EntityPlayer player, int keycode)
    {
        for (EventHotkey hotkey : this.hotkeys)
        {
            if (hotkey.keycode == keycode)
            {
                hotkey.execute(player);

                break;
            }
        }
    }

    public void load(File file)
    {
        this.file = file;

        try
        {
            this.deserializeNBT(NBTToJsonLike.fromJson(FileUtils.readFileToString(file, Charset.defaultCharset())));
        }
        catch (Exception e)
        {}
    }

    public void save()
    {
        try
        {
            FileUtils.writeStringToFile(this.file, NBTToJsonLike.toJson(this.serializeNBT()), Charset.defaultCharset());
        }
        catch (Exception e)
        {}
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList hotkeys = new NBTTagList();

        for (EventHotkey hotkey : this.hotkeys)
        {
            hotkeys.appendTag(hotkey.serializeNBT());
        }

        tag.setTag("Hotkeys", hotkeys);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.hotkeys.clear();

        if (tag.hasKey("Hotkeys", Constants.NBT.TAG_LIST))
        {
            NBTTagList hotkeys = tag.getTagList("Hotkeys", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < hotkeys.tagCount(); i++)
            {
                EventHotkey hotkey = new EventHotkey();

                hotkey.deserializeNBT(hotkeys.getCompoundTagAt(i));
                this.hotkeys.add(hotkey);
            }
        }
    }
}