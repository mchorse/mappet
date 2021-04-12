package mchorse.mappet.blocks;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.PacketEditTrigger;
import mchorse.mappet.tile.TileTrigger;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTrigger extends Block implements ITileEntityProvider
{
    public BlockTrigger()
    {
        super(Material.ROCK);
        this.setCreativeTab(Mappet.creativeTab);
        this.setBlockUnbreakable();
        this.setResistance(6000000.0F);
        this.setRegistryName(new ResourceLocation(Mappet.MOD_ID, "trigger"));
        this.setUnlocalizedName(Mappet.MOD_ID + ".trigger");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add(I18n.format("tile.mappet.trigger.tooltip"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
    {
        return true;
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
    {
        if (!worldIn.isRemote && !playerIn.isCreative())
        {
            TileEntity tile = worldIn.getTileEntity(pos);

            if (tile instanceof TileTrigger)
            {
                Mappet.events.execute(((TileTrigger) tile).leftClick, new EventContext(playerIn.getServer(), playerIn));
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            TileEntity tile = worldIn.getTileEntity(pos);

            if (!(tile instanceof TileTrigger))
            {
                return true;
            }

            TileTrigger trigger = (TileTrigger) tile;

            if (playerIn.isCreative())
            {
                Dispatcher.sendTo(new PacketEditTrigger(trigger), (EntityPlayerMP) playerIn);
            }
            else
            {
                Mappet.events.execute(trigger.rightClick, new EventContext(playerIn.getServer(), playerIn));
            }
        }

        return true;
    }

    /* Make the block walkable through and invisible */

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean canSpawnInBlock()
    {
        return true;
    }

    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return Block.NULL_AABB;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileTrigger();
    }
}