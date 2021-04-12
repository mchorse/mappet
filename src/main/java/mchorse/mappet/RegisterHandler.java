package mchorse.mappet;

import mchorse.mappet.blocks.BlockEmitter;
import mchorse.mappet.tile.TileEmitter;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RegisterHandler
{
    @SubscribeEvent
    public void onBlocksRegister(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(Mappet.emitterBlock = new BlockEmitter());
    }

    @SubscribeEvent
    public void onItemsRegister(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(new ItemBlock(Mappet.emitterBlock)
            .setRegistryName(new ResourceLocation(Mappet.MOD_ID, "emitter"))
            .setUnlocalizedName(Mappet.MOD_ID + ".emitter"));
    }

    @SubscribeEvent
    public void onEntityRegister(RegistryEvent.Register<EntityEntry> event)
    {
        GameRegistry.registerTileEntity(TileEmitter.class, Mappet.MOD_ID + ":emitter");
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onModelRegistry(ModelRegistryEvent event)
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Mappet.emitterBlock), 0, new ModelResourceLocation(Mappet.MOD_ID + ":emitter", "inventory"));
    }
}