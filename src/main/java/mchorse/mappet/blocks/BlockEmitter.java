package mchorse.mappet.blocks;

import mchorse.mappet.Mappet;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.blocks.PacketEditEmitter;
import mchorse.mappet.tile.TileEmitter;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockEmitter extends Block implements ITileEntityProvider
{
    /**
     * The playing state property of director block
     */
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public BlockEmitter()
    {
        super(Material.ROCK);
        this.setDefaultState(this.getDefaultState().withProperty(POWERED, false));
        this.setCreativeTab(Mappet.creativeTab);
        this.setBlockUnbreakable();
        this.setResistance(6000000.0F);
        this.setRegistryName(new ResourceLocation(Mappet.MOD_ID, "emitter"));
        this.setUnlocalizedName(Mappet.MOD_ID + ".emitter");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add(I18n.format("tile.mappet.emitter.tooltip"));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote && playerIn.isCreative())
        {
            TileEntity tile = worldIn.getTileEntity(pos);

            if (tile instanceof TileEmitter)
            {
                Dispatcher.sendTo(new PacketEditEmitter((TileEmitter) tile), (EntityPlayerMP) playerIn);
            }
        }

        return true;
    }

    /* States */

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(POWERED) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(POWERED, meta == 1);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, POWERED);
    }

    /* Redstone */

    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return blockState.getValue(POWERED) ? 15 : 0;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEmitter();
    }
}
