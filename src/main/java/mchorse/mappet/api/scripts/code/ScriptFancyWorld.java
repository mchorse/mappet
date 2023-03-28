package mchorse.mappet.api.scripts.code;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.api.scripts.code.blocks.ScriptBlockState;
import mchorse.mappet.api.scripts.user.IScriptFancyWorld;
import mchorse.mappet.api.scripts.user.blocks.IScriptBlockState;
import mchorse.mappet.api.scripts.user.data.ScriptVector;
import mchorse.mappet.api.scripts.user.entities.IScriptEntity;
import mchorse.mappet.api.scripts.user.entities.IScriptNpc;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.mappet.utils.RunnableExecutionFork;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScriptFancyWorld implements IScriptFancyWorld
{
    private final ScriptWorld scriptWorld;
    private final World world;
    private final ScriptFactory factory = new ScriptFactory();

    public ScriptFancyWorld(World world)
    {
        this.world = world;
        this.scriptWorld = new ScriptWorld(world);
    }

    @Override
    public List<IScriptEntity> explode(int x1, int y1, int z1, int x2, int y2, int z2, int blocksPercentage)
    {
        List<IScriptEntity> entities = new ArrayList<IScriptEntity>();
        int xMin = Math.min(x1, x2);
        int xMax = Math.max(x1, x2);
        int yMin = Math.min(y1, y2);
        int yMax = Math.max(y1, y2);
        int zMin = Math.min(z1, z2);
        int zMax = Math.max(z1, z2);
        int xCentre = (xMin + xMax) / 2;
        int yCentre = (yMin + yMax) / 2;
        int zCentre = (zMin + zMax) / 2;

        scriptWorld.playSound("minecraft:entity.generic.explode", xCentre, yCentre, zCentre, 1, 1);

        for (int x = xMin; x <= xMax; x++)
        {
            for (int y = yMin; y <= yMax; y++)
            {
                for (int z = zMin; z <= zMax; z++)
                {
                    //if the blocks are closer the centre, they are more likely to be destroyed
                    if (Math.random() < (1 - (Math.sqrt(Math.pow(xCentre - x, 2) + Math.pow(yCentre - y, 2) + Math.pow(zCentre - z, 2)) / Math.sqrt(Math.pow(xCentre - xMin, 2) + Math.pow(yCentre - yMin, 2) + Math.pow(zCentre - zMin, 2)))) * blocksPercentage / 100)
                    {
                        IScriptEntity entity = scriptWorld.setFallingBlock(x, y, z);

                        if (entity != null)
                        {
                            entity.addMotion((x - xCentre) / 2.0, (y - yCentre) / 2.0, (z - zCentre) / 2.0);
                            entities.add(entity);

                            //particles
                            ScriptVector pos = entity.getPosition();
                            EnumParticleTypes particle = factory.getParticleType("explode");

                            scriptWorld.spawnParticles(particle, true, pos.x, pos.y, pos.z, 10, 1, 1, 1, 0.1);
                        }
                    }
                }
            }
        }

        return entities;
    }

    @Override
    public List<IScriptEntity> explode(int x, int y, int z, int radius, int blocksPercentage)
    {
        return explode(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius, blocksPercentage);
    }

    @Override
    public void tpExplode(int x1, int y1, int z1, int x2, int y2, int z2, int blocksPercentage)
    {
        int xMin = Math.min(x1, x2);
        int xMax = Math.max(x1, x2);
        int yMin = Math.min(y1, y2);
        int yMax = Math.max(y1, y2);
        int zMin = Math.min(z1, z2);
        int zMax = Math.max(z1, z2);
        int xCentre = (xMin + xMax) / 2;
        int yCentre = (yMin + yMax) / 2;
        int zCentre = (zMin + zMax) / 2;

        scriptWorld.playSound("minecraft:entity.generic.explode", xCentre, yCentre, zCentre, 1, 1);

        for (int x = xMin; x <= xMax; x++)
        {
            for (int y = yMin; y <= yMax; y++)
            {
                for (int z = zMin; z <= zMax; z++)
                {
                    //if the blocks are closer the centre, they are more likely to be destroyed
                    if (Math.random() < (1 - (Math.sqrt(Math.pow(xCentre - x, 2) + Math.pow(yCentre - y, 2) + Math.pow(zCentre - z, 2)) / Math.sqrt(Math.pow(xCentre - xMin, 2) + Math.pow(yCentre - yMin, 2) + Math.pow(zCentre - zMin, 2)))) * blocksPercentage / 100)
                    {
                        IScriptBlockState state = scriptWorld.getBlock(x, y, z);

                        if (!state.getBlockId().equals("minecraft:air"))
                        {
                            scriptWorld.setBlock(ScriptBlockState.create(Blocks.AIR.getDefaultState()), x, y, z);
                            //place the removed blocks randomly far away from the centre (but not far)
                            int xNew = (int) (Math.random() * (xMax - xMin) + xMin);
                            int yNew = (int) (Math.random() * (yMax - yMin) + yMin);
                            int zNew = (int) (Math.random() * (zMax - zMin) + zMin);
                            scriptWorld.setBlock(state, xCentre + (int) (Math.random() * 10 - 5), yCentre + (int) (Math.random() * 10 - 5), zCentre + (int) (Math.random() * 10 - 5));

                            //particles
                            EnumParticleTypes particle = factory.getParticleType("explode");
                            scriptWorld.spawnParticles(particle, true, xNew + 0.5, yNew + 0.5, zNew + 0.5, 10, 1, 1, 1, 0.1);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void tpExplode(int x, int y, int z, int radius, int blocksPercentage)
    {
        tpExplode(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius, blocksPercentage);
    }

    @Override
    public void setBlock(IScriptBlockState state, int x, int y, int z, EnumParticleTypes particleType, int particlesAmount, String soundEvent, float volume, float pitch)
    {
        scriptWorld.setBlock(state, x, y, z);
        scriptWorld.playSound(soundEvent, x, y, z, volume, pitch);

        for (int i = 0; i < particlesAmount; i++)
        {
            double x_1 = x + Math.random();
            double y_1 = y + Math.random();
            double z_1 = z + Math.random();
            double x_2 = Math.random() - 0.5;
            double y_2 = Math.random() - 0.5;
            double z_2 = Math.random() - 0.5;

            scriptWorld.spawnParticles(particleType, true, x_1, y_1, z_1, 1, x_2, y_2, z_2, 0.1);
        }
    }


    @Override
    public void fill(String mode, IScriptBlockState state, int x1, int y1, int z1, int x2, int y2, int z2, int delayBetweenLayers, EnumParticleTypes particleType, int particlesPerBlock, String soundEvent, float volume, float pitch)
    {
        int xMin = Math.min(x1, x2);
        int xMax = Math.max(x1, x2);
        int yMin = Math.min(y1, y2);
        int yMax = Math.max(y1, y2);
        int zMin = Math.min(z1, z2);
        int zMax = Math.max(z1, z2);
        
        switch (mode)
        {
            case "0":
            case "du":
                for (int y = yMin; y <= yMax; y++)
                {
                    final int finalY = y;
                    CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (y - yMin), () ->
                    {
                        for (int x = xMin; x <= xMax; x++)
                        {
                            for (int z = zMin; z <= zMax; z++)
                            {
                                setBlock(state, x, finalY, z, particleType, particlesPerBlock, soundEvent, volume, pitch);
                            }
                        }
                    }));
                }
                break;
            case "1":
            case "ud":
                for (int y = yMax; y >= yMin; y--)
                {
                    final int finalY = y;
                    CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (yMax - y), () ->
                    {
                        for (int x = xMin; x <= xMax; x++)
                        {
                            for (int z = zMin; z <= zMax; z++)
                            {
                                setBlock(state, x, finalY, z, particleType, particlesPerBlock, soundEvent, volume, pitch);
                            }
                        }
                    }));
                }
                break;
            case "2":
            case "ns":
                for (int z = zMin; z <= zMax; z++)
                {
                    final int finalZ = z;
                    CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (z - zMin), () ->
                    {
                        for (int x = xMin; x <= xMax; x++)
                        {
                            for (int y = yMin; y <= yMax; y++)
                            {
                                setBlock(state, x, y, finalZ, particleType, particlesPerBlock, soundEvent, volume, pitch);
                            }
                        }
                    }));
                }
                break;
            case "3":
            case "sn":
                for (int z = zMax; z >= zMin; z--)
                {
                    final int finalZ = z;
                    CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (zMax - z), () ->
                    {
                        for (int x = xMin; x <= xMax; x++)
                        {
                            for (int y = yMin; y <= yMax; y++)
                            {
                                setBlock(state, x, y, finalZ, particleType, particlesPerBlock, soundEvent, volume, pitch);
                            }
                        }
                    }));
                }
                break;
            case "4":
            case "we":
                for (int x = xMin; x <= xMax; x++)
                {
                    final int finalX = x;
                    CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (x - xMin), () ->
                    {
                        for (int y = yMin; y <= yMax; y++)
                        {
                            for (int z = zMin; z <= zMax; z++)
                            {
                                setBlock(state, finalX, y, z, particleType, particlesPerBlock, soundEvent, volume, pitch);
                            }
                        }
                    }));
                }
                break;
            case "5":
            case "ew":
                for (int x = xMax; x >= xMin; x--)
                {
                    final int finalX = x;
                    CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (xMax - x), () ->
                    {
                        for (int y = yMin; y <= yMax; y++)
                        {
                            for (int z = zMin; z <= zMax; z++)
                            {
                                setBlock(state, finalX, y, z, particleType, particlesPerBlock, soundEvent, volume, pitch);
                            }
                        }
                    }));
                }
                break;
        }
    }

    @Override
    public void setTileEntity(int x, int y, int z, IScriptBlockState blockState, INBTCompound tileData, EnumParticleTypes particleType, int particlesAmount, String soundEvent, float volume, float pitch)
    {
        setBlock(blockState, x, y, z, particleType, particlesAmount, soundEvent, volume, pitch);
        tileData.setInt("x", x);
        tileData.setInt("y", y);
        tileData.setInt("z", z);
        scriptWorld.getTileEntity(x, y, z).setData(tileData);
    }

    @Override
    public void fillTileEntities(String mode, int x1, int y1, int z1, int x2, int y2, int z2, IScriptBlockState state, INBTCompound tileData, int delayBetweenLayers, EnumParticleTypes particleType, int particlesPerBlock, String soundEvent, float volume, float pitch)
    {
        int xMin = Math.min(x1, x2);
        int xMax = Math.max(x1, x2);
        int yMin = Math.min(y1, y2);
        int yMax = Math.max(y1, y2);
        int zMin = Math.min(z1, z2);
        int zMax = Math.max(z1, z2);
        switch (mode)
        {
            case "0":
            case "du":
                for (int y = yMin; y <= yMax; y++)
                {
                    final int finalY = y;
                    CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (y - yMin), () ->
                    {
                        for (int x = xMin; x <= xMax; x++)
                        {
                            for (int z = zMin; z <= zMax; z++)
                            {
                                setTileEntity(x, finalY, z, state, tileData, particleType, particlesPerBlock, soundEvent, volume, pitch);
                            }
                        }
                    }));
                }
                break;
            case "1":
            case "ud":
                for (int y = yMax; y >= yMin; y--)
                {
                    final int finalY = y;
                    CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (yMax - y), () ->
                    {
                        for (int x = xMin; x <= xMax; x++)
                        {
                            for (int z = zMin; z <= zMax; z++)
                            {
                                setTileEntity(x, finalY, z, state, tileData, particleType, particlesPerBlock, soundEvent, volume, pitch);
                            }
                        }
                    }));
                }
                break;
            case "2":
            case "ns":
                for (int z = zMin; z <= zMax; z++)
                {
                    final int finalZ = z;
                    CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (z - zMin), () ->
                    {
                        for (int x = xMin; x <= xMax; x++)
                        {
                            for (int y = yMin; y <= yMax; y++)
                            {
                                setTileEntity(x, y, finalZ, state, tileData, particleType, particlesPerBlock, soundEvent, volume, pitch);
                            }
                        }
                    }));
                }
                break;
            case "3":
            case "sn":
                for (int z = zMax; z >= zMin; z--)
                {
                    final int finalZ = z;
                    CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (zMax - z), () ->
                    {
                        for (int x = xMin; x <= xMax; x++)
                        {
                            for (int y = yMin; y <= yMax; y++)
                            {
                                setTileEntity(x, y, finalZ, state, tileData, particleType, particlesPerBlock, soundEvent, volume, pitch);
                            }
                        }
                    }));
                }
                break;
            case "4":
            case "we":
                for (int x = xMin; x <= xMax; x++)
                {
                    final int finalX = x;
                    CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (x - xMin), () ->
                    {
                        for (int z = zMin; z <= zMax; z++)
                        {
                            for (int y = yMin; y <= yMax; y++)
                            {
                                setTileEntity(finalX, y, z, state, tileData, particleType, particlesPerBlock, soundEvent, volume, pitch);
                            }
                        }
                    }));
                }
                break;
            case "5":
            case "ew":
                for (int x = xMax; x >= xMin; x--)
                {
                    final int finalX = x;
                    CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (xMax - x), () ->
                    {
                        for (int z = zMin; z <= zMax; z++)
                        {
                            for (int y = yMin; y <= yMax; y++)
                            {
                                setTileEntity(finalX, y, z, state, tileData, particleType, particlesPerBlock, soundEvent, volume, pitch);
                            }
                        }
                    }));
                }
                break;
        }
    }


    @Override
    public void clone(int x, int y, int z, int xNew, int yNew, int zNew, EnumParticleTypes particleType, int particlesAmount, String soundEvent, float volume, float pitch)
    {
        IScriptBlockState state = scriptWorld.getBlock(x, y, z);
        if (!state.getBlockId().equals("minecraft:air"))
        {
            setBlock(state, xNew, yNew, zNew, particleType, particlesAmount, soundEvent, volume, pitch);
            if (scriptWorld.getTileEntity(x, y, z) != null)
            {
                INBTCompound tile = scriptWorld.getTileEntity(x, y, z).getData();
                tile.setInt("x", xNew);
                tile.setInt("y", yNew);
                tile.setInt("z", zNew);
                setBlock(state, x, y, z, particleType, particlesAmount, soundEvent, volume, pitch);
                scriptWorld.getTileEntity(xNew, yNew, zNew).setData(tile);
            }
        }
    }

    @Override
    public void clone(String mode, int x1, int y1, int z1, int x2, int y2, int z2, int xNew, int yNew, int zNew, int delayBetweenLayers, EnumParticleTypes particleType, int particlesPerBlock, String soundEvent, float volume, float pitch)
    {
        int xMin = Math.min(x1, x2);
        int xMax = Math.max(x1, x2);
        int yMin = Math.min(y1, y2);
        int yMax = Math.max(y1, y2);
        int zMin = Math.min(z1, z2);
        int zMax = Math.max(z1, z2);
        int xCentre = (xMin + xMax) / 2;
        int yCentre = (yMin + yMax) / 2;
        int zCentre = (zMin + zMax) / 2;
        switch (mode)
        {
            case "0":
            case "du":
                for (int y = yMin; y <= yMax; y++)
                {
                    final int finalY = y;
                    CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (y - yMin), () ->
                    {
                        for (int x = xMin; x <= xMax; x++)
                        {
                            for (int z = zMin; z <= zMax; z++)
                            {
                                clone(x, finalY, z, xNew + x - xCentre, yNew + finalY - yCentre, zNew + z - zCentre, particleType, particlesPerBlock, soundEvent, volume, pitch);
                            }
                        }
                    }));
                }
                break;
            case "1":
            case "ud":
                for (int y = yMax; y >= yMin; y--)
                {
                    final int finalY = y;
                    CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (yMax - y), () ->
                    {
                        for (int x = xMin; x <= xMax; x++)
                        {
                            for (int z = zMin; z <= zMax; z++)
                            {
                                clone(x, finalY, z, xNew + x - xCentre, yNew + finalY - yCentre, zNew + z - zCentre, particleType, particlesPerBlock, soundEvent, volume, pitch);
                            }
                        }
                    }));
                }
                break;
            case "2":
            case "ns":
                for (int z = zMin; z <= zMax; z++)
                {
                    final int finalZ = z;
                    CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (z - zMin), () ->
                    {
                        for (int x = xMin; x <= xMax; x++)
                        {
                            for (int y = yMin; y <= yMax; y++)
                            {
                                clone(x, y, finalZ, xNew + x - xCentre, yNew + y - yCentre, zNew + finalZ - zCentre, particleType, particlesPerBlock, soundEvent, volume, pitch);
                            }
                        }
                    }));
                }
                break;
            case "3":
            case "sn":
                for (int z = zMax; z >= zMin; z--)
                {
                    final int finalZ = z;
                    CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (zMax - z), () ->
                    {
                        for (int x = xMin; x <= xMax; x++)
                        {
                            for (int y = yMin; y <= yMax; y++)
                            {
                                clone(x, y, finalZ, xNew + x - xCentre, yNew + y - yCentre, zNew + finalZ - zCentre, particleType, particlesPerBlock, soundEvent, volume, pitch);
                            }
                        }
                    }));
                }
                break;
            case "4":
            case "we":
                for (int x = xMin; x <= xMax; x++)
                {
                    final int finalX = x;
                    CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (x - xMin), () ->
                    {
                        for (int z = zMin; z <= zMax; z++)
                        {
                            for (int y = yMin; y <= yMax; y++)
                            {
                                clone(finalX, y, z, xNew + finalX - xCentre, yNew + y - yCentre, zNew + z - zCentre, particleType, particlesPerBlock, soundEvent, volume, pitch);
                            }
                        }
                    }));
                }
                break;
            case "5":
            case "ew":
                for (int x = xMax; x >= xMin; x--)
                {
                    final int finalX = x;
                    CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (xMax - x), () ->
                    {
                        for (int z = zMin; z <= zMax; z++)
                        {
                            for (int y = yMin; y <= yMax; y++)
                            {
                                clone(finalX, y, z, xNew + finalX - xCentre, yNew + y - yCentre, zNew + z - zCentre, particleType, particlesPerBlock, soundEvent, volume, pitch);
                            }
                        }
                    }));
                }
                break;
        }
    }


    @Override
    public void loadSchematic(String mode, String name, int target_x, int target_y, int target_z, int delayBetweenLayers, EnumParticleTypes particleType, int particlesPerBlock, String soundEvent, float volume, float pitch)
    {
        File file = new File("saves/" + this.world.getWorldInfo().getWorldName() + "/mappet/schematics/" + name + ".nbt");
        if (file.exists())
        {
            try
            {
                NBTTagCompound tag = CompressedStreamTools.read(file);
                NBTTagList blocks = tag.getTagList("blocks", 10);
                int xMin = tag.getInteger("xMin");
                int yMin = tag.getInteger("yMin");
                int zMin = tag.getInteger("zMin");
                int xMax = tag.getInteger("xMax");
                int yMax = tag.getInteger("yMax");
                int zMax = tag.getInteger("zMax");

                List<List<NBTTagCompound>> layers = new ArrayList<>();
                switch (mode)
                {
                    case "0":
                    case "du":
                        for (int y = yMin; y <= yMax; y++)
                        {
                            List<NBTTagCompound> layer = new ArrayList<>();
                            for (int i = 0; i < blocks.tagCount(); i++)
                            {
                                NBTTagCompound block = blocks.getCompoundTagAt(i);
                                if (block.getInteger("y") == y)
                                {
                                    layer.add(block);
                                }
                            }
                            layers.add(layer);
                        }
                        for (int y = yMin; y <= yMax; y++)
                        {
                            final int y1 = y;
                            CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (y - yMin), () ->
                            {
                                for (NBTTagCompound block : layers.get(y1 - yMin))
                                {
                                    int x1 = block.getInteger("x");
                                    int z1 = block.getInteger("z");
                                    IScriptBlockState state = factory.createBlockState(block.getString("block"), block.getInteger("meta"));
                                    setBlock(state, target_x + x1 - xMin, target_y + y1 - yMin, target_z + z1 - zMin, particleType, particlesPerBlock, soundEvent, volume, pitch);
                                    if (block.hasKey("tile"))
                                    {
                                        INBTCompound tile = factory.createCompound(block.getCompoundTag("tile").toString());
                                        tile.setInt("x", target_x + x1 - xMin);
                                        tile.setInt("y", target_y + y1 - yMin);
                                        tile.setInt("z", target_z + z1 - zMin);
                                        scriptWorld.getTileEntity(target_x + x1 - xMin, target_y + y1 - yMin, target_z + z1 - zMin).setData(tile);
                                    }
                                }
                            }));
                        }
                        break;
                    case "1":
                    case "ud":
                        for (int y = yMax; y >= yMin; y--)
                        {
                            List<NBTTagCompound> layer = new ArrayList<>();
                            for (int i = 0; i < blocks.tagCount(); i++)
                            {
                                NBTTagCompound block = blocks.getCompoundTagAt(i);
                                if (block.getInteger("y") == y)
                                {
                                    layer.add(block);
                                }
                            }
                            layers.add(layer);
                        }
                        for (int y = yMax; y >= yMin; y--)
                        {
                            final int y1 = y;
                            CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (yMax - y), () ->
                            {
                                for (NBTTagCompound block : layers.get(yMax - y1))
                                {
                                    int x1 = block.getInteger("x");
                                    int z1 = block.getInteger("z");
                                    IScriptBlockState state = factory.createBlockState(block.getString("block"), block.getInteger("meta"));
                                    setBlock(state, target_x + x1 - xMin, target_y + y1 - yMin, target_z + z1 - zMin, particleType, particlesPerBlock, soundEvent, volume, pitch);
                                    if (block.hasKey("tile"))
                                    {
                                        INBTCompound tile = factory.createCompound(block.getCompoundTag("tile").toString());
                                        tile.setInt("x", target_x + x1 - xMin);
                                        tile.setInt("y", target_y + y1 - yMin);
                                        tile.setInt("z", target_z + z1 - zMin);
                                        scriptWorld.getTileEntity(target_x + x1 - xMin, target_y + y1 - yMin, target_z + z1 - zMin).setData(tile);
                                    }
                                }
                            }));
                        }
                        break;
                    case "2":
                    case "ns":
                        for (int z = zMin; z <= zMax; z++)
                        {
                            List<NBTTagCompound> layer = new ArrayList<>();
                            for (int i = 0; i < blocks.tagCount(); i++)
                            {
                                NBTTagCompound block = blocks.getCompoundTagAt(i);
                                if (block.getInteger("z") == z)
                                {
                                    layer.add(block);
                                }
                            }
                            layers.add(layer);
                        }
                        for (int z = zMin; z <= zMax; z++)
                        {
                            final int z1 = z;
                            CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (z - zMin), () ->
                            {
                                for (NBTTagCompound block : layers.get(z1 - zMin))
                                {
                                    int x1 = block.getInteger("x");
                                    int y1 = block.getInteger("y");
                                    IScriptBlockState state = factory.createBlockState(block.getString("block"), block.getInteger("meta"));
                                    setBlock(state, target_x + x1 - xMin, target_y + y1 - yMin, target_z + z1 - zMin, particleType, particlesPerBlock, soundEvent, volume, pitch);
                                    if (block.hasKey("tile"))
                                    {
                                        INBTCompound tile = factory.createCompound(block.getCompoundTag("tile").toString());
                                        tile.setInt("x", target_x + x1 - xMin);
                                        tile.setInt("y", target_y + y1 - yMin);
                                        tile.setInt("z", target_z + z1 - zMin);
                                        scriptWorld.getTileEntity(target_x + x1 - xMin, target_y + y1 - yMin, target_z + z1 - zMin).setData(tile);
                                    }
                                }
                            }));
                        }
                        break;
                    case "3":
                    case "sn":
                        for (int z = zMax; z >= zMin; z--)
                        {
                            List<NBTTagCompound> layer = new ArrayList<>();
                            for (int i = 0; i < blocks.tagCount(); i++)
                            {
                                NBTTagCompound block = blocks.getCompoundTagAt(i);
                                if (block.getInteger("z") == z)
                                {
                                    layer.add(block);
                                }
                            }
                            layers.add(layer);
                        }
                        for (int z = zMax; z >= zMin; z--)
                        {
                            final int z1 = z;
                            CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (zMax - z), () ->
                            {
                                for (NBTTagCompound block : layers.get(zMax - z1))
                                {
                                    int x1 = block.getInteger("x");
                                    int y1 = block.getInteger("y");
                                    IScriptBlockState state = factory.createBlockState(block.getString("block"), block.getInteger("meta"));
                                    setBlock(state, target_x + x1 - xMin, target_y + y1 - yMin, target_z + z1 - zMin, particleType, particlesPerBlock, soundEvent, volume, pitch);
                                    if (block.hasKey("tile"))
                                    {
                                        INBTCompound tile = factory.createCompound(block.getCompoundTag("tile").toString());
                                        tile.setInt("x", target_x + x1 - xMin);
                                        tile.setInt("y", target_y + y1 - yMin);
                                        tile.setInt("z", target_z + z1 - zMin);
                                        scriptWorld.getTileEntity(target_x + x1 - xMin, target_y + y1 - yMin, target_z + z1 - zMin).setData(tile);
                                    }
                                }
                            }));
                        }
                        break;
                    case "4":
                    case "we":
                        for (int x = xMin; x <= xMax; x++)
                        {
                            List<NBTTagCompound> layer = new ArrayList<>();
                            for (int i = 0; i < blocks.tagCount(); i++)
                            {
                                NBTTagCompound block = blocks.getCompoundTagAt(i);
                                if (block.getInteger("x") == x)
                                {
                                    layer.add(block);
                                }
                            }
                            layers.add(layer);
                        }
                        for (int x = xMin; x <= xMax; x++)
                        {
                            final int x1 = x;
                            CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (x - xMin), () ->
                            {
                                for (NBTTagCompound block : layers.get(x1 - xMin))
                                {
                                    int y1 = block.getInteger("y");
                                    int z1 = block.getInteger("z");
                                    IScriptBlockState state = factory.createBlockState(block.getString("block"), block.getInteger("meta"));
                                    setBlock(state, target_x + x1 - xMin, target_y + y1 - yMin, target_z + z1 - zMin, particleType, particlesPerBlock, soundEvent, volume, pitch);
                                    if (block.hasKey("tile"))
                                    {
                                        INBTCompound tile = factory.createCompound(block.getCompoundTag("tile").toString());
                                        tile.setInt("x", target_x + x1 - xMin);
                                        tile.setInt("y", target_y + y1 - yMin);
                                        tile.setInt("z", target_z + z1 - zMin);
                                        scriptWorld.getTileEntity(target_x + x1 - xMin, target_y + y1 - yMin, target_z + z1 - zMin).setData(tile);
                                    }
                                }
                            }));
                        }
                        break;
                    case "5":
                    case "ew":
                        for (int x = xMax; x >= xMin; x--)
                        {
                            List<NBTTagCompound> layer = new ArrayList<>();
                            for (int i = 0; i < blocks.tagCount(); i++)
                            {
                                NBTTagCompound block = blocks.getCompoundTagAt(i);
                                if (block.getInteger("x") == x)
                                {
                                    layer.add(block);
                                }
                            }
                            layers.add(layer);
                        }
                        for (int x = xMax; x >= xMin; x--)
                        {
                            final int x1 = x;
                            CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(delayBetweenLayers * (xMax - x), () ->
                            {
                                for (NBTTagCompound block : layers.get(xMax - x1))
                                {
                                    int y1 = block.getInteger("y");
                                    int z1 = block.getInteger("z");
                                    IScriptBlockState state = factory.createBlockState(block.getString("block"), block.getInteger("meta"));
                                    setBlock(state, target_x + x1 - xMin, target_y + y1 - yMin, target_z + z1 - zMin, particleType, particlesPerBlock, soundEvent, volume, pitch);
                                    if (block.hasKey("tile"))
                                    {
                                        INBTCompound tile = factory.createCompound(block.getCompoundTag("tile").toString());
                                        tile.setInt("x", target_x + x1 - xMin);
                                        tile.setInt("y", target_y + y1 - yMin);
                                        tile.setInt("z", target_z + z1 - zMin);
                                        scriptWorld.getTileEntity(target_x + x1 - xMin, target_y + y1 - yMin, target_z + z1 - zMin).setData(tile);
                                    }
                                }
                            }));
                        }
                        break;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public IScriptNpc spawnNpc(String id, String state, double x, double y, double z, float yaw, float pitch, float yawHead, EnumParticleTypes particleType, double particleSpeed, int particlesAmount, String soundEvent, float volume, float volumePitch)
    {
        IScriptNpc npc = scriptWorld.spawnNpc(id, state, x, y, z, yaw, pitch, yawHead);
        if (npc != null)
        {
            scriptWorld.playSound(soundEvent, x, y, z, volume, volumePitch);
            for (int i = 0; i < particlesAmount; i++)
            {
                double x_1 = npc.getPosition().x;
                double y_1 = npc.getPosition().y;
                double z_1 = npc.getPosition().z;
                double x_2 = Math.random() - 0.5;
                double y_2 = Math.random() - 0.5;
                double z_2 = Math.random() - 0.5;
                scriptWorld.spawnParticles(particleType, true, x_1, y_1, z_1, 1, x_2, y_2, z_2, particleSpeed);
            }
        }
        return npc;
    }
}