package mchorse.mappet.items;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.entities.EntityNpc;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.npc.PacketNpcList;
import mchorse.mappet.network.common.npc.PacketNpcState;
import mchorse.mclib.utils.OpHelper;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
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
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand)
    {
        if (!player.world.isRemote && target instanceof EntityNpc)
        {
            if (Mappet.npcsToolOnlyOP.get() && !OpHelper.isPlayerOp((EntityPlayerMP) player))
            {
                return super.itemInteractionForEntity(stack, player, target, hand);
            }

            if (Mappet.npcsToolOnlyCreative.get() && !player.capabilities.isCreativeMode)
            {
                return super.itemInteractionForEntity(stack, player, target, hand);
            }

            EntityNpc npc = (EntityNpc) target;

            if (player.isSneaking())
            {
                npc.setDead();
            }
            else
            {
                Dispatcher.sendTo(new PacketNpcState(target.getEntityId(), npc.getState().serializeNBT()), (EntityPlayerMP) player);
            }

            return true;
        }

        return super.itemInteractionForEntity(stack, player, target, hand);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if (!worldIn.isRemote)
        {
            if (Mappet.npcsToolOnlyOP.get() && !OpHelper.isPlayerOp((EntityPlayerMP) playerIn))
            {
                return super.onItemRightClick(worldIn, playerIn, handIn);
            }

            if (Mappet.npcsToolOnlyCreative.get() && !playerIn.capabilities.isCreativeMode)
            {
                return super.onItemRightClick(worldIn, playerIn, handIn);
            }

            if (this.openNpcTool(playerIn, playerIn.getHeldItem(handIn)))
            {
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
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

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);

        if (!worldIn.isRemote)
        {
            if (Mappet.npcsToolOnlyOP.get() && !OpHelper.isPlayerOp((EntityPlayerMP) player))
            {
                return EnumActionResult.PASS;
            }

            if (Mappet.npcsToolOnlyCreative.get() && !player.capabilities.isCreativeMode)
            {
                return EnumActionResult.PASS;
            }

            EntityNpc entity = new EntityNpc(worldIn);
            BlockPos posOffset = pos.offset(facing);

            entity.setPosition(posOffset.getX() + hitX, posOffset.getY() + hitY, posOffset.getZ() + hitZ);

            this.setupState(entity, stack);

            entity.world.spawnEntity(entity);
            entity.initialize();

            if (!player.isSneaking())
            {
                Dispatcher.sendTo(new PacketNpcState(entity.getEntityId(), entity.getState().serializeNBT()), (EntityPlayerMP) player);
            }
        }

        return stack.getItem() == Mappet.npcTool ? EnumActionResult.SUCCESS : super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    private void setupState(EntityNpc entity, ItemStack stack)
    {
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

            if (state != null)
            {
                entity.setNpc(npc, state);
            }
        }
        else
        {
            tag = new NBTTagCompound();
            tag.setString("Name", "blockbuster.fred");

            AbstractMorph morph = MorphManager.INSTANCE.morphFromNBT(tag);

            entity.getState().morph = morph;
            entity.setMorph(morph);
        }
    }
}