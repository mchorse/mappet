package mchorse.mappet.tile;

import mchorse.blockbuster.Blockbuster;
import mchorse.blockbuster.common.entity.EntityActor;
import mchorse.blockbuster.common.tileentity.TileEntityModelSettings;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.conditions.Checker;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.blocks.PacketEditConditionModel;
import mchorse.mappet.utils.ConditionModel;
import mchorse.mclib.math.Constant;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TileConditionModel extends TileEntity implements ITickable
{
    public EntityActor entity;
    public int frequency;
    public boolean isGlobal = true;
    public boolean isShadow = false;
    private TileEntityModelSettings settings;
    public List<ConditionModel> list = new ArrayList<>();

    private int tick;

    public TileConditionModel()
    {
        this.frequency = 1;
        this.settings = new TileEntityModelSettings();
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        float range = Blockbuster.actorRenderingRange.get();

        return range * range;
    }

    @Override
    public void update()
    {
        if (this.world.isRemote)
        {
            updateMorph();
            return;
        }

        int frequency = Math.max(this.frequency, 1);

        Constant constantFalse = new Constant(0);

        if (this.tick % frequency == 0)
        {
            for (EntityPlayer playerEntity : this.world.playerEntities)
            {
                AbstractMorph morph = null;

                for (ConditionModel conditionModel : this.list)
                {
                    boolean result = false;

                    DataContext context = new DataContext(playerEntity);
                    Checker checker = conditionModel.checker;

                    if (checker.mode == Checker.Mode.CONDITION)
                    {
                        result = checker.condition.execute(context);
                    }
                    else
                    {
                        String expression = checker.expression;
                        result = Mappet.expressions.set(context).parse(expression, constantFalse).booleanValue();
                    }

                    if (result && !conditionModel.morph.equals(morph))
                    {
                        morph = conditionModel.morph;
                    }
                }
                NBTTagCompound tag = new NBTTagCompound();
                NBTTagCompound tagMorph = new NBTTagCompound();
                if (morph != null)
                {
                    morph.toNBT(tagMorph);
                }
                NBTTagCompound settings = new NBTTagCompound();
                this.settings.toNBT(settings);
                tag.setTag("settings", settings);
                tag.setTag("morph", tagMorph);
                tag.setBoolean("shadow", this.isShadow);
                tag.setBoolean("global", this.isGlobal);
                Dispatcher.sendTo(new PacketEditConditionModel(this.getPos(), tag).setIsEdit(false), (EntityPlayerMP) playerEntity);
            }
        }

        this.tick += 1;
    }

    @SideOnly(Side.CLIENT)
    public void updateMorph()
    {
        if (this.entity == null)
        {
            this.createEntity(this.world);
        }

        if (this.entity.morph.get() != null)
        {
            this.entity.morph.get().update(this.entity);
        }

        ++entity.ticksExisted;
    }

    public TileEntityModelSettings getSettings()
    {
        return this.settings;
    }

    public void createEntity(World world)
    {
        if (world != null)
        {
            this.entity = new EntityActor(world);
            this.entity.onGround = true;
        }
    }

    public void fill(NBTTagCompound tag)
    {
        this.list.clear();

        NBTTagList list = tag.getTagList("list", 10);

        for (int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound element = list.getCompoundTagAt(i);

            ConditionModel conditionModel = new ConditionModel();
            conditionModel.deserializeNBT(element);

            this.list.add(conditionModel);
        }

        this.frequency = tag.getInteger("frequency");
        this.isGlobal = tag.getBoolean("global");
        this.isShadow = tag.getBoolean("shadow");
    }

    public NBTTagCompound toNBT(NBTTagCompound tag)
    {
        NBTTagList list = new NBTTagList();

        for (ConditionModel element : this.list)
        {
            list.appendTag(element.serializeNBT());
        }

        tag.setTag("list", list);
        tag.setInteger("frequency", this.frequency);
        tag.setBoolean("global", this.isGlobal);
        tag.setBoolean("shadow", this.isShadow);
        return tag;
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, this.getBlockMetadata(), this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
    {
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        NBTTagCompound settings = new NBTTagCompound();
        this.settings.toNBT(settings);

        tag.setTag("settings", settings);

        this.toNBT(tag);

        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        this.settings = new TileEntityModelSettings();
        this.settings.fromNBT((NBTTagCompound) tag.getTag("settings"));

        this.fill(tag);
    }
}
