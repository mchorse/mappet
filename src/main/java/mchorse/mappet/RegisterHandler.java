package mchorse.mappet;

import mchorse.mappet.api.crafting.CraftingManager;
import mchorse.mappet.api.dialogues.DialogueManager;
import mchorse.mappet.api.events.EventManager;
import mchorse.mappet.api.factions.FactionManager;
import mchorse.mappet.api.huds.HUDManager;
import mchorse.mappet.api.npcs.NpcManager;
import mchorse.mappet.api.quests.QuestManager;
import mchorse.mappet.api.quests.chains.QuestChainManager;
import mchorse.mappet.api.scripts.ScriptManager;
import mchorse.mappet.blocks.BlockEmitter;
import mchorse.mappet.blocks.BlockRegion;
import mchorse.mappet.blocks.BlockTrigger;
import mchorse.mappet.client.KeyboardHandler;
import mchorse.mappet.client.RenderingHandler;
import mchorse.mappet.entities.EntityNpc;
import mchorse.mappet.items.ItemNpcTool;
import mchorse.mappet.tile.TileEmitter;
import mchorse.mappet.tile.TileRegion;
import mchorse.mappet.tile.TileTrigger;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RegisterHandler
{
    public static boolean sentSkin;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        if (!event.isLocal())
        {
            Mappet.quests = new QuestManager(null);
            Mappet.crafting = new CraftingManager(null);
            Mappet.events = new EventManager(null);
            Mappet.dialogues = new DialogueManager(null);
            Mappet.npcs = new NpcManager(null);
            Mappet.factions = new FactionManager(null);
            Mappet.chains = new QuestChainManager(null);
            Mappet.scripts = new ScriptManager(null);
            Mappet.huds = new HUDManager(null);
        }

        KeyboardHandler.clientPlayerJournal = true;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        sentSkin = false;

        Mappet.quests = null;
        Mappet.crafting = null;
        Mappet.events = null;
        Mappet.dialogues = null;
        Mappet.npcs = null;
        Mappet.factions = null;
        Mappet.chains = null;
        Mappet.scripts = null;
        Mappet.huds = null;

        KeyboardHandler.hotkeys.clear();
        RenderingHandler.reset();
    }

    @SubscribeEvent
    public void onBlocksRegister(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(Mappet.emitterBlock = new BlockEmitter());
        event.getRegistry().register(Mappet.triggerBlock = new BlockTrigger());
        event.getRegistry().register(Mappet.regionBlock = new BlockRegion());
    }

    @SubscribeEvent
    public void onItemsRegister(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(Mappet.npcTool = new ItemNpcTool()
            .setRegistryName(new ResourceLocation(Mappet.MOD_ID, "npc_tool"))
            .setUnlocalizedName(Mappet.MOD_ID + ".npc_tool"));

        event.getRegistry().register(new ItemBlock(Mappet.emitterBlock)
            .setRegistryName(new ResourceLocation(Mappet.MOD_ID, "emitter"))
            .setUnlocalizedName(Mappet.MOD_ID + ".emitter"));

        event.getRegistry().register(new ItemBlock(Mappet.triggerBlock)
            .setRegistryName(new ResourceLocation(Mappet.MOD_ID, "trigger"))
            .setUnlocalizedName(Mappet.MOD_ID + ".trigger"));

        event.getRegistry().register(new ItemBlock(Mappet.regionBlock)
            .setRegistryName(new ResourceLocation(Mappet.MOD_ID, "region"))
            .setUnlocalizedName(Mappet.MOD_ID + ".region"));
    }

    @SubscribeEvent
    public void onEntityRegister(RegistryEvent.Register<EntityEntry> event)
    {
        event.getRegistry().register(EntityEntryBuilder.create()
            .entity(EntityNpc.class)
            .name(Mappet.MOD_ID + ".npc")
            .id(new ResourceLocation(Mappet.MOD_ID, "npc"), 0)
            .tracker(EntityNpc.RENDER_DISTANCE, 3, false)
            .build());

        GameRegistry.registerTileEntity(TileEmitter.class, Mappet.MOD_ID + ":emitter");
        GameRegistry.registerTileEntity(TileTrigger.class, Mappet.MOD_ID + ":trigger");
        GameRegistry.registerTileEntity(TileRegion.class, Mappet.MOD_ID + ":region");
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onModelRegistry(ModelRegistryEvent event)
    {
        ModelLoader.setCustomModelResourceLocation(Mappet.npcTool, 0, new ModelResourceLocation(Mappet.MOD_ID + ":npc_tool", "inventory"));

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Mappet.emitterBlock), 0, new ModelResourceLocation(Mappet.MOD_ID + ":emitter", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Mappet.triggerBlock), 0, new ModelResourceLocation(Mappet.MOD_ID + ":trigger", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Mappet.regionBlock), 0, new ModelResourceLocation(Mappet.MOD_ID + ":region", "inventory"));
    }
}