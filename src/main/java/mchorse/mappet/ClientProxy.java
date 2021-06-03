package mchorse.mappet;

import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.KeyboardHandler;
import mchorse.mappet.client.RenderingHandler;
import mchorse.mappet.client.SoundPack;
import mchorse.mappet.client.gui.scripts.utils.SyntaxStyle;
import mchorse.mappet.client.renders.entity.RenderNpc;
import mchorse.mappet.client.renders.tile.TileRegionRenderer;
import mchorse.mappet.client.renders.tile.TileTriggerRenderer;
import mchorse.mappet.entities.EntityNpc;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.content.PacketContentRequestNames;
import mchorse.mappet.tile.TileRegion;
import mchorse.mappet.tile.TileTrigger;
import mchorse.mappet.utils.NBTToJsonLike;
import mchorse.mclib.utils.ReflectionUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    private static int requestId = 0;
    private static Map<Integer, Consumer<List<String>>> consumers = new HashMap<Integer, Consumer<List<String>>>();

    public static File editorThemes;

    public static void requestNames(ContentType type, Consumer<List<String>> consumer)
    {
        consumers.put(requestId, consumer);
        Dispatcher.sendToServer(new PacketContentRequestNames(type, requestId));

        requestId += 1;
    }

    public static void process(List<String> names, int id)
    {
        Consumer<List<String>> consumer = consumers.remove(id);

        if (consumer != null)
        {
            consumer.accept(names);
        }
    }

    public static void writeTheme(File file, SyntaxStyle style)
    {
        try
        {
            FileUtils.writeStringToFile(file, NBTToJsonLike.toJson(style.toNBT()), Charset.defaultCharset());
        }
        catch (Exception e)
        {}
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);

        MinecraftForge.EVENT_BUS.register(new KeyboardHandler());
        MinecraftForge.EVENT_BUS.register(new RenderingHandler());

        ClientRegistry.bindTileEntitySpecialRenderer(TileTrigger.class, new TileTriggerRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileRegion.class, new TileRegionRenderer());

        RenderingRegistry.registerEntityRenderingHandler(EntityNpc.class, new RenderNpc.Factory());

        ReflectionUtils.registerResourcePack(new SoundPack(new File(CommonProxy.configFolder, "sounds")));

        this.createThemes();
    }

    private void createThemes()
    {
        editorThemes = new File(configFolder, "themes");
        editorThemes.mkdirs();

        File monokai = new File(editorThemes, "monokai.json");
        File dracula = new File(editorThemes, "dracula.json");

        if (!monokai.isFile())
        {
            writeTheme(monokai, new SyntaxStyle());
        }

        if (!dracula.isFile())
        {
            SyntaxStyle draculaStyle = new SyntaxStyle();

            draculaStyle.title = "Dracula";
            draculaStyle.shadow = true;
            draculaStyle.primary = 0xcc7832;
            draculaStyle.secondary = 0x9876aa;
            draculaStyle.identifier = 0xffc66d;
            draculaStyle.special = 0xcc7832;
            draculaStyle.strings = 0x619554;
            draculaStyle.comments = 0x808080;
            draculaStyle.numbers = 0x6694b8;
            draculaStyle.other = 0xa9b7c6;
            draculaStyle.lineNumbers = 0x5e6163;
            draculaStyle.background = 0x2b2b2b;

            writeTheme(dracula, draculaStyle);
        }
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
    }
}