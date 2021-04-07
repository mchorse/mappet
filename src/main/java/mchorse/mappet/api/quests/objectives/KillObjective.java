package mchorse.mappet.api.quests.objectives;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class KillObjective implements IObjective
{
    public ResourceLocation entity = new ResourceLocation("");
    public int count;
    public int killed;

    public KillObjective()
    {}

    public KillObjective(ResourceLocation entity, int quantity)
    {
        this.entity = entity;
        this.count = quantity;
    }

    public void playerKilled(EntityPlayer player, Entity mob)
    {
        if (this.entity.equals(EntityList.getKey(mob)))
        {
            this.killed += 1;
        }
    }

    @Override
    public boolean isComplete(EntityPlayer player)
    {
        return this.killed >= this.count;
    }

    @Override
    public void complete(EntityPlayer player)
    {}

    @Override
    public IObjective copy()
    {
        return new KillObjective(this.entity, this.count);
    }

    @Override
    public String stringify(EntityPlayer player)
    {
        return "Kill " + this.entity.toString() + " (" + Math.min(this.killed, this.count) + "/" + this.count + ")";
    }

    @Override
    public String getType()
    {
        return "kill";
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setString("Entity", this.entity.toString());
        tag.setInteger("Count", this.count);
        tag.setInteger("Killed", this.killed);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.entity = new ResourceLocation(tag.getString("Entity"));
        this.count = tag.getInteger("Count");
        this.killed = tag.getInteger("Killed");
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entity = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
        this.count = buf.readInt();
        this.killed = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.entity.toString());
        buf.writeInt(this.count);
        buf.writeInt(this.killed);
    }
}