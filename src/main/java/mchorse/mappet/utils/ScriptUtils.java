package mchorse.mappet.utils;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import mchorse.blockbuster.common.tileentity.TileEntityModel;
import mchorse.blockbuster.network.common.PacketModifyModelBlock;
import mchorse.mappet.network.common.blocks.PacketEditConditionModel;
import mchorse.mappet.network.common.blocks.PacketEditRegion;
import mchorse.mappet.tile.TileConditionModel;
import mchorse.mappet.tile.TileRegion;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static mchorse.mappet.network.Dispatcher.DISPATCHER;

public class ScriptUtils
{
    private static ScriptEngineManager manager;

    public static List<ScriptEngine> getAllEngines()
    {
        return getManager().getEngineFactories().stream()
                .filter(factory -> !factory.getExtensions().contains("scala"))
                .map(factory ->
                {
                    try
                    {
                        return factory.getScriptEngine();
                    }
                    catch (Exception | NoClassDefFoundError ignored)
                    {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static ScriptEngine getEngineByExtension(String extension)
    {
        extension = extension.replace(".", "");

        ScriptEngine engine = getManager().getEngineByExtension(extension);

        if (extension.equals("py"))
        {
            try
            {
                Field fieldInterpreter = Class.forName("org.python.jsr223.PyScriptEngine").getDeclaredField("interp");
                fieldInterpreter.setAccessible(true);
                Object interpreter = fieldInterpreter.get(engine);

                Field fieldcFlags = Class.forName("org.python.util.PythonInterpreter").getDeclaredField("cflags");
                fieldcFlags.setAccessible(true);
                Object cFlags = fieldcFlags.get(interpreter);

                Class.forName("org.python.core.CompilerFlags").getDeclaredField("source_is_utf8").setBoolean(cFlags, true);

                return engine;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return engine;
    }

    /**
     * Run something to avoid it loading first time
     */
    public static void initiateScriptEngines()
    {
        List<ScriptEngine> engineList = getAllEngines();

        for (ScriptEngine engine : engineList)
        {
            try
            {
                if (!engine.eval(Objects.equals(engine.getFactory().getLanguageName(), "python") ? "True" : "true").equals(Boolean.TRUE))
                {
                    throw new Exception("Something went wrong with " + engine.getFactory().getEngineName());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static ScriptEngineManager getManager()
    {
        try
        {
            if (manager == null)
            {
                manager = new ScriptEngineManager();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return manager;
    }

    public static ScriptEngine sanitize(ScriptEngine engine)
    {
        /* Remove */
        Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);

        bindings.remove("load");
        bindings.remove("loadWithNewGlobal");
        bindings.remove("exit");
        bindings.remove("quit");

        return engine;
    }

    public static String getScriptContent(ScriptObjectMirror script) {
        String fullScript = script.toString();
        String scriptContent = "";

        Pattern pattern = Pattern.compile("\\{(.*)\\}", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(fullScript);
        if (matcher.find()) {
            scriptContent = matcher.group(1).trim();
        }

        /* Formatting the script content */

        // Splitting the script content into lines
        String[] lines = scriptContent.split("\\n");

        // Get the common spaces
        int commonSpaces = Integer.MAX_VALUE;
        for (int i = 1; i < lines.length; i++) {
            int spaceCount = 0;
            while (lines[i].length() > spaceCount && lines[i].charAt(spaceCount) == ' ') {
                spaceCount++;
            }
            commonSpaces = Math.min(commonSpaces, spaceCount);
        }

        // Removing the common leading spaces
        StringBuilder adjustedScript = new StringBuilder(lines[0].trim() + "\n");
        for (int i = 1; i < lines.length; i++) {
            adjustedScript.append(lines[i].substring(commonSpaces)).append("\n");
        }
        scriptContent = adjustedScript.toString().trim(); // Removing trailing new line

        return scriptContent;
    }

    /* MappetBlock- */

    /**
     * This method sends the packet informing about the model block update
     */
    public static <T> void sendTileUpdatePacket(T tile)
    {
        try
        {
            if (tile instanceof TileEntityModel)
            {
                TileEntityModel bbModelBlock = (TileEntityModel) tile;
                PacketModifyModelBlock message = new PacketModifyModelBlock(bbModelBlock.getPos(), bbModelBlock, true);
                DISPATCHER.get().sendToAll(message);
            }
            else if (tile instanceof TileConditionModel)
            {
                TileConditionModel conditionModelBlock = (TileConditionModel) tile;
                NBTTagCompound tag = new NBTTagCompound();
                PacketEditConditionModel message = new PacketEditConditionModel(conditionModelBlock.getPos(), conditionModelBlock.toNBT(tag));
                DISPATCHER.get().sendToAll(message);
            }
            else if (tile instanceof TileRegion)
            {
                TileRegion region = (TileRegion) tile;
                NBTTagCompound tag = new NBTTagCompound();
                PacketEditRegion message = new PacketEditRegion(region.getPos(), region.region.serializeNBT());
                DISPATCHER.get().sendToAll(message);
            }
            else
            {
                throw new IllegalArgumentException("Invalid tile type");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This interface provides a mechanism to perform post-placement operations on a TileEntity.
     *
     * @param <T> The type of TileEntity on which to perform operations.
     */
    public interface PostPlace<T extends TileEntity> {
        /**
         * Apply operation to the given TileEntity.
         *
         * @param tileEntity The TileEntity on which to perform operations.
         */
        void apply(T tileEntity);
    }

    /**
     * Places a block of type T at a given position and performs post-placement operations on the associated TileEntity.
     *
     * @param <T> The type of Block to be placed.
     * @param <U> The type of TileEntity associated with the block.
     * @param <V> The return type of the method.
     * @param mcWorld The Minecraft world where the block is to be placed.
     * @param pos The position where the block is to be placed.
     * @param blockType The type of block to place.
     * @param tileEntityType The type of TileEntity associated with the block.
     * @param postPlace An instance of the PostPlace interface to perform operations on the TileEntity after the block is placed.
     * @param returnVal A Supplier functional interface instance that supplies the return value of the method.
     * @return Returns a value of type V, supplied by the returnVal parameter.
     */
    public static <T extends Block, U extends TileEntity, V> V place(World mcWorld, BlockPos pos, T blockType, Class<U> tileEntityType, PostPlace<U> postPlace, Supplier<V> returnVal) {
        if (mcWorld.getBlockState(pos).getBlock() != Blocks.AIR) {
            mcWorld.setBlockState(pos, Blocks.AIR.getDefaultState(), 2 | 4);
        }

        mcWorld.setBlockState(pos, blockType.getDefaultState(), 2 | 4);

        if (mcWorld.getBlockState(pos).getBlock() == blockType) {
            U tileEntity = tileEntityType.cast(mcWorld.getTileEntity(pos));
            if (tileEntity != null) {
                postPlace.apply(tileEntity);
                tileEntity.markDirty();
            }
        }

        return returnVal.get();
    }
}