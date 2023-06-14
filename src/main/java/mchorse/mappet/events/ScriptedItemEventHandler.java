package mchorse.mappet.events;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.api.scripts.code.entities.ScriptEntityItem;
import mchorse.mappet.api.scripts.code.items.ScriptItemStack;
import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.common.ScriptedItemProps;
import mchorse.mappet.utils.NBTUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class ScriptedItemEventHandler
{
    @SubscribeEvent
    public void onScriptedItemRightClick(PlayerInteractEvent.RightClickItem event)
    {
        EntityPlayer playerIn = event.getEntityPlayer();
        World worldIn = event.getWorld();
        EnumHand handIn = event.getHand();

        if (!worldIn.isRemote)
        {
            ItemStack item = playerIn.getHeldItem(handIn);
            ScriptedItemProps props = NBTUtils.getScriptedItemProps(item);

            if (props == null)
            {
                return;
            }

            double reachDistance = playerIn.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();

            RayTraceResult rayTraceResult = playerIn.rayTrace(reachDistance, 1.0F);
            List<Entity> entityList = getEntitiesInPlayerReach(worldIn, playerIn, reachDistance);

            triggerInteractWithAir(event, props.interactWithAir, rayTraceResult, entityList, playerIn);
        }
    }

    @SubscribeEvent
    public void onPlayerWithScriptedItemInteractWithEntity(PlayerInteractEvent.EntityInteract event)
    {
        if (!event.getWorld().isRemote)
        {
            ItemStack item = event.getEntityPlayer().getHeldItem(event.getHand());
            ScriptedItemProps props = NBTUtils.getScriptedItemProps(item);

            if (props != null && props.interactWithEntity != null && !props.interactWithEntity.blocks.isEmpty())
            {
                DataContext context = new DataContext(event.getEntityPlayer(), event.getTarget());
                context.set("hand", event.getHand() == EnumHand.MAIN_HAND ? "main" : "off");
                CommonProxy.eventHandler.trigger(event, props.interactWithEntity, context);
            }
        }
    }

    @SubscribeEvent
    public void onEntityAttackedWithScriptedItem(LivingAttackEvent event)
    {
        if (!event.getEntity().world.isRemote && event.getSource().getTrueSource() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            ItemStack item = player.getHeldItem(EnumHand.MAIN_HAND);
            ScriptedItemProps props = NBTUtils.getScriptedItemProps(item);

            if (props != null && props.attackEntity != null && !props.attackEntity.blocks.isEmpty())
            {
                DamageSource source = event.getSource();
                DataContext context = new DataContext(event.getEntityLiving(), source.getTrueSource())
                        .set("damage", event.getAmount());
                CommonProxy.eventHandler.trigger(event, props.attackEntity, context);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerWithScriptedItemRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        if (!event.getWorld().isRemote)
        {
            ItemStack item = event.getEntityPlayer().getHeldItem(event.getHand());
            ScriptedItemProps props = NBTUtils.getScriptedItemProps(item);

            if (props != null && props.interactWithBlock != null && !props.interactWithBlock.blocks.isEmpty())
            {
                IBlockState state = event.getWorld().getBlockState(event.getPos());
                DataContext context = new DataContext(event.getWorld(), event.getPos())
                        .set("block", state.getBlock().getRegistryName().toString())
                        .set("meta", state.getBlock().getMetaFromState(state))
                        .set("x", event.getPos().getX())
                        .set("y", event.getPos().getY())
                        .set("z", event.getPos().getZ())
                        .set("hand", event.getHand() == EnumHand.MAIN_HAND ? "main" : "off");
                CommonProxy.eventHandler.trigger(event, props.interactWithBlock, context);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerHoldingScriptedItemTick(TickEvent.PlayerTickEvent event)
    {
        if (!event.player.world.isRemote)
        {
            for (EnumHand hand : EnumHand.values())
            { // Check for both main and off hands
                ItemStack item = event.player.getHeldItem(hand);
                ScriptedItemProps props = NBTUtils.getScriptedItemProps(item);

                if (props != null && props.onHolderTick != null && !props.onHolderTick.blocks.isEmpty())
                {
                    DataContext context = new DataContext(event.player);
                    context.set("hand", hand == EnumHand.MAIN_HAND ? "main" : "off"); // Add the hand used to the context
                    CommonProxy.eventHandler.trigger(event, props.onHolderTick, context);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerWithScriptedItemLeftClick(PlayerInteractEvent.LeftClickBlock event)
    {
        if (!event.getWorld().isRemote)
        {
            ItemStack item = event.getEntityPlayer().getHeldItem(event.getHand());
            ScriptedItemProps props = NBTUtils.getScriptedItemProps(item);

            if (props != null && props.hitBlock != null && !props.hitBlock.blocks.isEmpty())
            {
                DataContext context = new DataContext(event.getEntityPlayer())
                        .set("x", event.getPos().getX())
                        .set("y", event.getPos().getY())
                        .set("z", event.getPos().getZ())
                        .set("hand", event.getHand() == EnumHand.MAIN_HAND ? "main" : "off");
                CommonProxy.eventHandler.trigger(event, props.hitBlock, context);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerWithScriptedItemPlaceBlock(BlockEvent.PlaceEvent event)
    {
        if (!event.getWorld().isRemote)
        {
            EntityPlayer player = event.getPlayer();
            EnumHand handIn = EnumHand.MAIN_HAND;
            ItemStack item = player.getHeldItem(handIn);

            // Check if the item is not in the main hand, if so, switch to off hand
            if (item.isEmpty())
            {
                handIn = EnumHand.OFF_HAND;
                item = player.getHeldItem(handIn);
            }

            ScriptedItemProps props = NBTUtils.getScriptedItemProps(item);

            if (props != null && props.placeBlock != null && !props.placeBlock.blocks.isEmpty())
            {
                IBlockState state = event.getPlacedBlock();
                DataContext context = new DataContext(player)
                        .set("block", state.getBlock().getRegistryName().toString())
                        .set("meta", state.getBlock().getMetaFromState(state))
                        .set("x", event.getPos().getX())
                        .set("y", event.getPos().getY())
                        .set("z", event.getPos().getZ())
                        .set("hand", handIn == EnumHand.MAIN_HAND ? "main" : "off");
                CommonProxy.eventHandler.trigger(event, props.placeBlock, context);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerPickUpScriptedItem(EntityItemPickupEvent event)
    {
        if (!event.getItem().world.isRemote)
        {
            EntityPlayer player = event.getEntityPlayer();
            ItemStack item = event.getItem().getItem();
            ScriptedItemProps props = NBTUtils.getScriptedItemProps(item);

            if (props != null && props.pickup != null && !props.pickup.blocks.isEmpty())
            {
                DataContext context = new DataContext(player);
                context.getValues().put("item", ScriptItemStack.create(event.getItem().getItem()));
                context.getValues().put("entityItem", ScriptEntityItem.create(event.getItem()));
                CommonProxy.eventHandler.trigger(event, props.pickup, context);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTossScriptedItem(ItemTossEvent event)
    {
        if (!event.getEntityItem().world.isRemote)
        {
            EntityPlayer player = event.getPlayer();
            ItemStack item = event.getEntityItem().getItem();
            ScriptedItemProps props = NBTUtils.getScriptedItemProps(item);

            if (props != null && props.toss != null && !props.toss.blocks.isEmpty())
            {
                DataContext context = new DataContext(player);
                context.getValues().put("item", ScriptItemStack.create(event.getEntityItem().getItem()));
                context.getValues().put("entityItem", ScriptEntityItem.create(event.getEntityItem()));
                CommonProxy.eventHandler.trigger(event, props.toss, context);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerWithScriptedItemBreakBlock(BlockEvent.BreakEvent event)
    {
        if (!event.getWorld().isRemote)
        {
            EntityPlayer player = event.getPlayer();
            ItemStack item = player.getHeldItem(EnumHand.MAIN_HAND);
            ScriptedItemProps props = NBTUtils.getScriptedItemProps(item);

            if (props != null && props.breakBlock != null && !props.breakBlock.blocks.isEmpty())
            {
                IBlockState state = event.getState();
                DataContext context = new DataContext(player)
                        .set("block", state.getBlock().getRegistryName().toString())
                        .set("meta", state.getBlock().getMetaFromState(state))
                        .set("x", event.getPos().getX())
                        .set("y", event.getPos().getY())
                        .set("z", event.getPos().getZ());
                CommonProxy.eventHandler.trigger(event, props.breakBlock, context);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerWithScriptedItemTick(TickEvent.PlayerTickEvent event)
    {
        if (!event.player.world.isRemote && event.side == Side.SERVER && event.phase == TickEvent.Phase.START)
        {
            for (ItemStack item : event.player.getHeldEquipment())
            {
                ScriptedItemProps props = NBTUtils.getScriptedItemProps(item);

                if (props != null && props.onHolderTick != null && !props.onHolderTick.blocks.isEmpty())
                {
                    DataContext context = new DataContext(event.player);
                    CommonProxy.eventHandler.trigger(event, props.onHolderTick, context);
                }
            }
        }
    }

    private void triggerInteractWithAir(PlayerInteractEvent.RightClickItem event, Trigger interactWithAirTrigger, RayTraceResult rayTraceResult, List<Entity> entityList, EntityPlayer playerIn)
    {
        if (interactWithAirTrigger != null && !interactWithAirTrigger.blocks.isEmpty() &&
                (rayTraceResult == null || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) && entityList.isEmpty())
        {
            DataContext context = new DataContext(playerIn)
                    .set("x", event.getPos().getX())
                    .set("y", event.getPos().getY())
                    .set("z", event.getPos().getZ())
                    .set("hand", event.getHand() == EnumHand.MAIN_HAND ? "main" : "off");
            CommonProxy.eventHandler.trigger(event, interactWithAirTrigger, context);
        }
    }

    private List<Entity> getEntitiesInPlayerReach(World worldIn, EntityPlayer playerIn, double reachDistance)
    {
        Vec3d eyePos = playerIn.getPositionEyes(1.0F);
        Vec3d lookVec = playerIn.getLookVec();
        Vec3d reachVec = eyePos.add(new Vec3d(lookVec.x * reachDistance, lookVec.y * reachDistance, lookVec.z * reachDistance));

        AxisAlignedBB playerReach = new AxisAlignedBB(eyePos.x, eyePos.y, eyePos.z, reachVec.x, reachVec.y, reachVec.z);

        List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, playerReach);

        list.removeIf(entity -> !entity.canBeCollidedWith() || !EntitySelectors.NOT_SPECTATING.apply(entity));
        list.removeIf(entity -> entity.getEntityBoundingBox().calculateIntercept(eyePos, reachVec) == null);

        return list;
    }

    @SubscribeEvent
    public void onFirstItemPickup(EntityItemPickupEvent event)
    {
        if (!event.getItem().world.isRemote)
        {
            EntityPlayer player = event.getEntityPlayer();
            ItemStack itemStack = event.getItem().getItem();

            ScriptedItemProps props = NBTUtils.getScriptedItemProps(itemStack);

            // Check if the item has been picked up before
            if (props != null && props.pickedUp) {
                // The item has been picked up before, so we do nothing
                return;
            }

            // This is the first time the item has been picked up
            // Set the pickedUp property to true
            if (props != null) {
                props.pickedUp = true;
                // Save the updated props back to the item
                NBTUtils.setScriptedItemProps(itemStack, props);

                // Check that there's a Trigger for first pickup and it has actions
                if (props.firstPickup != null && !props.firstPickup.blocks.isEmpty())
                {
                    DataContext context = new DataContext(player);
                    context.getValues().put("item", ScriptItemStack.create(itemStack));
                    context.getValues().put("entityItem", ScriptEntityItem.create(event.getItem()));
                    CommonProxy.eventHandler.trigger(event, props.firstPickup, context);
                }
            }
        }
    }

    /**
     * on started using & on stopped using
     */
    @SubscribeEvent
    public void onLivingEquipmentChange(LivingEquipmentChangeEvent event)
    {
        // Skip if on client side
        if (event.getEntityLiving().world.isRemote)
        {
            return;
        }

        ItemStack previousItem = event.getFrom();
        ItemStack newItem = event.getTo();
        int slotIndex = event.getSlot().getSlotIndex(); // Always get slot index from the event

        // Check if entity started holding an item
        if (previousItem.isEmpty() && !newItem.isEmpty())
        {
            ScriptedItemProps props = NBTUtils.getScriptedItemProps(newItem);
            if (props != null && props.startedHolding != null && !props.startedHolding.blocks.isEmpty())
            {
                DataContext context = new DataContext(event.getEntityLiving());
                context.getValues().put("item", ScriptItemStack.create(newItem));
                CommonProxy.eventHandler.trigger(event, props.startedHolding, context);
            }
        }
        // Check if entity stopped holding an item
        else if (!previousItem.isEmpty() && newItem.isEmpty())
        {
            ScriptedItemProps props = NBTUtils.getScriptedItemProps(previousItem);
            if (props != null && props.stoppedHolding != null && !props.stoppedHolding.blocks.isEmpty())
            {
                DataContext context = new DataContext(event.getEntityLiving());
                context.getValues().put("item", ScriptItemStack.create(previousItem));
                CommonProxy.eventHandler.trigger(event, props.stoppedHolding, context);
            }
        }
    }

    @SubscribeEvent
    public void onScriptedItemUseStart(LivingEntityUseItemEvent.Start event) {
        if (!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ItemStack item = event.getItem();
            ScriptedItemProps props = NBTUtils.getScriptedItemProps(item);

            if (props != null && props.useStart != null && !props.useStart.blocks.isEmpty()) {
                DataContext context = new DataContext(player);
                context.getValues().put("item", ScriptItemStack.create(item));
                context.getValues().put("duration", event.getDuration());
                CommonProxy.eventHandler.trigger(event, props.useStart, context);
            }
        }
    }

    @SubscribeEvent
    public void onScriptedItemUseStop(LivingEntityUseItemEvent.Stop event) {
        if (!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ItemStack item = event.getItem();
            ScriptedItemProps props = NBTUtils.getScriptedItemProps(item);

            if (props != null && props.useStop != null && !props.useStop.blocks.isEmpty()) {
                DataContext context = new DataContext(player);
                context.getValues().put("item", ScriptItemStack.create(item));
                context.getValues().put("duration", event.getDuration());
                CommonProxy.eventHandler.trigger(event, props.useStop, context);
            }
        }
    }

    @SubscribeEvent
    public void onScriptedItemUseTick(LivingEntityUseItemEvent.Tick event) {
        if (!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ItemStack item = event.getItem();
            ScriptedItemProps props = NBTUtils.getScriptedItemProps(item);

            if (props != null && props.onUseTick != null && !props.onUseTick.blocks.isEmpty()) {
                DataContext context = new DataContext(player);
                context.getValues().put("item", ScriptItemStack.create(item));
                context.getValues().put("duration", event.getDuration());
                CommonProxy.eventHandler.trigger(event, props.onUseTick, context);
            }
        }
    }

    @SubscribeEvent
    public void onScriptedItemUseFinish(LivingEntityUseItemEvent.Finish event)
    {
        if (!event.getEntityLiving().getEntityWorld().isRemote)
        {
            if(event.getEntityLiving() instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                ItemStack item = event.getItem();
                ScriptedItemProps props = NBTUtils.getScriptedItemProps(item);

                if (props != null && props.finishedUsing != null && !props.finishedUsing.blocks.isEmpty())
                {
                    DataContext context = new DataContext(player);
                    context.getValues().put("item", ScriptItemStack.create(item));
                    context.getValues().put("duration", event.getDuration());
                    CommonProxy.eventHandler.trigger(event, props.finishedUsing, context);
                }
            }
        }
    }
}
