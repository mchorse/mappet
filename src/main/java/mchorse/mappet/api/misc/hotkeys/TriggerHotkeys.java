package mchorse.mappet.api.misc.hotkeys;

import mchorse.mappet.api.utils.DataContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

public class TriggerHotkeys implements INBTSerializable<NBTTagCompound>
{
    public List<TriggerHotkey> hotkeys = new ArrayList<TriggerHotkey>();

    public void execute(EntityPlayer player, int keycode)
    {
        for (TriggerHotkey hotkey : this.hotkeys)
        {
            if (hotkey.keycode == keycode)
            {
                hotkey.execute(new DataContext(player).set("key", keycode));

                break;
            }
        }
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList hotkeys = new NBTTagList();

        for (TriggerHotkey hotkey : this.hotkeys)
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
                TriggerHotkey hotkey = new TriggerHotkey();

                hotkey.deserializeNBT(hotkeys.getCompoundTagAt(i));
                this.hotkeys.add(hotkey);
            }
        }
    }
}