package mchorse.mappet.api.events.nodes;

import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class DataNode extends EventBaseNode
{
    public String dataId = "";
    public String customData = "";

    @Override
    @SideOnly(Side.CLIENT)
    protected String getDisplayTitle()
    {
        return this.dataId;
    }

    public DataContext apply(EventContext event)
    {
        DataContext context = event.data.copy();

        context.parse(event.data.process(this.customData));

        return context;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        tag.setString("DataId", this.dataId);
        tag.setString("CustomData", this.customData);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.dataId = tag.getString("DataId");
        this.customData = tag.getString("CustomData");
    }
}