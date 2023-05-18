package mchorse.mappet.common;

import mchorse.mappet.api.triggers.Trigger;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;

/**
 * Scripted item properties
 * <p>
 * This class is responsible for storing properties of scripted items.
 * Including Triggers and display morphs.
 * <p>
 */
public class ScriptedItemProps {
    private HashMap<Trigger, String> initialState = new HashMap<>();
    public Trigger interactWithAir = new Trigger();
    public Trigger interactWithEntity = new Trigger();
    public Trigger interactWithBlock = new Trigger();
    public Trigger attackEntity = new Trigger();
    public Trigger breakBlock = new Trigger();
    public Trigger placeBlock = new Trigger();
    public Trigger hitBlock = new Trigger();
    public Trigger onHolderTick = new Trigger();
    public Trigger pickup = new Trigger();

    public ScriptedItemProps() {
        reset();
    }

    public ScriptedItemProps(NBTTagCompound tag) {
        reset();
        this.fromNBT(tag);
    }

    /**
     * Reset properties to default values
     */
    public void reset() {
        this.interactWithAir = new Trigger();
        this.interactWithEntity = new Trigger();
        this.interactWithBlock = new Trigger();
        this.attackEntity = new Trigger();
        this.breakBlock = new Trigger();
        this.placeBlock = new Trigger();
        this.hitBlock = new Trigger();
        this.onHolderTick = new Trigger();
        this.pickup = new Trigger();
    }

    public void fromNBT(NBTTagCompound tag) {
        this.reset();

        if (tag.hasKey("InteractWithAir")) {
            this.interactWithAir.deserializeNBT(tag.getCompoundTag("InteractWithAir"));
        }
        if (tag.hasKey("InteractWithEntity")) {
            this.interactWithEntity.deserializeNBT(tag.getCompoundTag("InteractWithEntity"));
        }
        if (tag.hasKey("InteractWithBlock")) {
            this.interactWithBlock.deserializeNBT(tag.getCompoundTag("InteractWithBlock"));
        }
        if (tag.hasKey("AttackEntity")) {
            this.attackEntity.deserializeNBT(tag.getCompoundTag("AttackEntity"));
        }
        if (tag.hasKey("BreakBlock")) {
            this.breakBlock.deserializeNBT(tag.getCompoundTag("BreakBlock"));
        }
        if (tag.hasKey("PlaceBlock")) {
            this.placeBlock.deserializeNBT(tag.getCompoundTag("PlaceBlock"));
        }
        if (tag.hasKey("HitBlock")) {
            this.hitBlock.deserializeNBT(tag.getCompoundTag("HitBlock"));
        }
        if (tag.hasKey("OnHolderTick")) {
            this.onHolderTick.deserializeNBT(tag.getCompoundTag("OnHolderTick"));
        }
        if (tag.hasKey("Pickup")) {
            this.pickup.deserializeNBT(tag.getCompoundTag("Pickup"));
        }
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("InteractWithAir", this.interactWithAir.serializeNBT());
        tag.setTag("InteractWithEntity", this.interactWithEntity.serializeNBT());
        tag.setTag("InteractWithBlock", this.interactWithBlock.serializeNBT());
        tag.setTag("AttackEntity", this.attackEntity.serializeNBT());
        tag.setTag("BreakBlock", this.breakBlock.serializeNBT());
        tag.setTag("PlaceBlock", this.placeBlock.serializeNBT());
        tag.setTag("HitBlock", this.hitBlock.serializeNBT());
        tag.setTag("OnHolderTick", this.onHolderTick.serializeNBT());
        tag.setTag("Pickup", this.pickup.serializeNBT());

        return tag;
    }

    /* These methods are responsable for doing nothing if the properties has not been changed when the gui closes. */
    public void storeInitialState() {
        initialState.clear();

        for (Trigger trigger : getTriggers()) {
            initialState.put(trigger, trigger.serializeNBT().toString());
        }
    }

    public boolean hasChanged() {
        for (Trigger trigger : getTriggers()) {
            String initial = initialState.get(trigger);
            String current = trigger.serializeNBT().toString();
            if (initial == null) {
                return true;
            }
            if (!initial.equals(current)) {
                return true;
            }
        }
        return false;
    }

    public Trigger[] getTriggers() {
        return new Trigger[] {
            this.interactWithAir,
            this.interactWithEntity,
            this.interactWithBlock,
            this.attackEntity,
            this.breakBlock,
            this.placeBlock,
            this.hitBlock,
            this.onHolderTick,
            this.pickup
        };
    }
}