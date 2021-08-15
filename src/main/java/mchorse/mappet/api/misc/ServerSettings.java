package mchorse.mappet.api.misc;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.misc.hotkeys.TriggerHotkeys;
import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.events.RegisterServerTriggerEvent;
import mchorse.mappet.utils.NBTToJsonLike;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Global server settings
 */
public class ServerSettings implements INBTSerializable<NBTTagCompound>
{
    private File file;

    public final Map<String, Trigger> registered = new LinkedHashMap<String, Trigger>();
    public final TriggerHotkeys hotkeys = new TriggerHotkeys();

    public final Trigger chat;
    public final Trigger breakBlock;
    public final Trigger placeBlock;
    public final Trigger damageEntity;
    public final Trigger serverLoad;
    public final Trigger serverTick;

    /* Player triggers */
    public final Trigger playerLogIn;
    public final Trigger playerLeftClick;
    public final Trigger playerRightClick;
    public final Trigger playerDeath;

    public Trigger register(String key, Trigger trigger)
    {
        if (this.registered.containsKey(key))
        {
            throw new IllegalStateException("Server trigger '" + key + "' is already registered!");
        }

        this.registered.put(key, trigger);

        return trigger;
    }

    public ServerSettings(File file)
    {
        this.file = file;

        this.chat = this.register("chat", new Trigger());
        this.breakBlock = this.register("break_block", new Trigger());
        this.placeBlock = this.register("place_block", new Trigger());
        this.damageEntity = this.register("damage_entity", new Trigger());
        this.serverLoad = this.register("server_load", new Trigger());
        this.serverTick = this.register("server_tick", new Trigger());

        this.playerLogIn = this.register("player_login", new Trigger());
        this.playerLeftClick = this.register("player_lmb", new Trigger());
        this.playerRightClick = this.register("player_rmb", new Trigger());
        this.playerDeath = this.register("player_death", new Trigger());

        Mappet.EVENT_BUS.post(new RegisterServerTriggerEvent(this));
    }

    /* Deserialization / Serialization */

    public void load()
    {
        if (this.file == null || !this.file.isFile())
        {
            return;
        }

        try
        {
            NBTTagCompound tag = NBTToJsonLike.read(this.file);

            if (!tag.hasKey("Hotkeys"))
            {
                /* Backward compatibility with beta */
                File hotkeys = new File(this.file.getParentFile(), "hotkeys.json");

                if (hotkeys.isFile())
                {
                    try
                    {
                        NBTTagCompound hotkeysTag = NBTToJsonLike.read(hotkeys);

                        tag.setTag("Hotkeys", hotkeysTag);
                        hotkeys.delete();
                    }
                    catch (Exception e)
                    {}
                }
            }

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
            NBTToJsonLike.write(this.file, this.serializeNBT());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /* NBT */

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound triggers = new NBTTagCompound();

        for (Map.Entry<String, Trigger> entry : this.registered.entrySet())
        {
            this.writeTrigger(triggers, entry.getKey(), entry.getValue());
        }

        if (!triggers.hasNoTags())
        {
            tag.setTag("Triggers", triggers);
        }

        tag.setTag("Hotkeys", this.hotkeys.serializeNBT());

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
        if (tag.hasKey("Triggers"))
        {
            NBTTagCompound triggers = tag.getCompoundTag("Triggers");

            for (Map.Entry<String, Trigger> entry : this.registered.entrySet())
            {
                this.readTrigger(triggers, entry.getKey(), entry.getValue());
            }
        }

        if (tag.hasKey("Hotkeys"))
        {
            this.hotkeys.deserializeNBT(tag.getCompoundTag("Hotkeys"));
        }
    }

    private void readTrigger(NBTTagCompound tag, String key, Trigger trigger)
    {
        if (tag.hasKey(key, Constants.NBT.TAG_COMPOUND))
        {
            NBTTagCompound triggerTag = tag.getCompoundTag(key);

            if (!triggerTag.hasNoTags())
            {
                trigger.deserializeNBT(triggerTag);
            }
        }
    }
}