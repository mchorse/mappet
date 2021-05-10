package mchorse.mappet.items;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.entities.EntityNpc;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.npc.PacketNpcList;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemNpcTool extends Item
{
    public ItemNpcTool()
    {
        this.setCreativeTab(Mappet.creativeTab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(I18n.format("item.mappet.npc_tool.tooltip"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if (!worldIn.isRemote && playerIn.isSneaking() && this.openNpcTool(playerIn, playerIn.getHeldItem(handIn)))
        {
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);

        if (!worldIn.isRemote)
        {
            if (player.isSneaking() && this.openNpcTool(player, stack))
            {
                return EnumActionResult.SUCCESS;
            }

            NBTTagCompound tag = stack.getTagCompound();

            if (tag != null)
            {
                String npcId = tag.getString("Npc");
                String stateId = tag.getString("State");

                Npc npc = Mappet.npcs.load(npcId);
                NpcState state = npc == null ? null : npc.states.get(stateId);

                if (npc != null && state == null && npc.states.containsKey("default"))
                {
                    state = npc.states.get("default");
                }

                if (npc != null && state != null)
                {
                    EntityNpc entity = new EntityNpc(worldIn);
                    BlockPos posOffset = pos.offset(facing);

                    entity.setPosition(posOffset.getX() + hitX, posOffset.getY() + hitY, posOffset.getZ() + hitZ);
                    entity.setNpc(npc, state);

                    entity.world.spawnEntity(entity);
                    entity.initialize();
                }
            }
        }

        return stack.getItem() == Mappet.npcTool ? EnumActionResult.SUCCESS : super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    private boolean openNpcTool(EntityPlayer player, ItemStack stack)
    {
        Collection<String> npcs = Mappet.npcs.getKeys();

        if (!npcs.isEmpty() && player instanceof EntityPlayerMP)
        {
            List<String> states = new ArrayList<String>();

            try
            {
                NBTTagCompound tag = stack.getTagCompound();
                Npc npc = Mappet.npcs.load(tag.getString("Npc"));

                states.addAll(npc.states.keySet());
            }
            catch (Exception e)
            {}

            Dispatcher.sendTo(new PacketNpcList(npcs, states), (EntityPlayerMP) player);

            return true;
        }

        return false;
    }
}