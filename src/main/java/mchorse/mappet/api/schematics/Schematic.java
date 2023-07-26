package mchorse.mappet.api.schematics;

import mchorse.mappet.api.utils.AbstractData;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Schematic extends AbstractData
{
    private IBlockState[][][] blockStates;

    private byte[][][] metadata;

    private final List<TileEntity> tileEntities = new ArrayList<>();

    private final List<Entity> entities = new ArrayList<>();

    private int width;

    private int height;

    private int length;

    public Schematic()
    {
        this(1, 1, 1);
    }

    public Schematic(int width, int height, int length)
    {
        this.width = width;
        this.height = height;
        this.length = length;

        this.init();
    }

    public void init()
    {
        this.blockStates = new IBlockState[this.width][this.height][this.length];
        //this.metadata = new byte[this.width][this.height][this.length];
        this.tileEntities.clear();
        this.entities.clear();
    }

    public boolean setBlockState(BlockPos pos, IBlockState blockState)
    {
        if (isInvalid(pos))
        {
            return false;
        }

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        this.blockStates[x][y][z] = blockState;

        return true;
    }

    public IBlockState getBlockState(BlockPos pos)
    {
        if (isInvalid(pos))
        {
            return Blocks.AIR.getDefaultState();
        }

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return this.blockStates[x][y][z];
    }

    public void setTileEntity(final BlockPos pos, final TileEntity tileEntity)
    {
        if (isInvalid(pos))
        {
            return;
        }

        removeTileEntity(pos);

        if (tileEntity != null)
        {
            this.tileEntities.add(tileEntity);
        }
    }

    public TileEntity getTileEntity(BlockPos pos)
    {
        for (TileEntity tileEntity : this.tileEntities)
        {
            if (tileEntity.getPos().equals(pos))
            {
                return tileEntity;
            }
        }

        return null;
    }

    public void removeTileEntity(BlockPos pos)
    {
        this.tileEntities.removeIf(tileEntity -> tileEntity.getPos().equals(pos));
    }

    public List<TileEntity> getTileEntities()
    {
        return this.tileEntities;
    }

    private boolean isInvalid(BlockPos pos)
    {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.height || z >= this.length;
    }

    public void loadFromWorld(World world, int x1, int y1, int z1, int x2, int y2, int z2)
    {
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);

        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);
        int maxZ = Math.max(z1, z2);

        this.width = maxX - minX + 1;
        this.height = maxY - minY + 1;
        this.length = maxZ - minZ + 1;

        this.init();

        this.copyBlocks(world, minX, minY, minZ);
        this.copyEntities(world, minX, minY, minZ, maxX, maxY, maxZ);
    }

    public void place(World world, int x, int y, int z, boolean replaceBlocks, boolean placeAir)
    {
        for (int localX = 0; localX <= this.width; localX++)
        {
            for (int localY = 0; localY <= this.height; localY++)
            {
                for (int localZ = 0; localZ <= this.length; localZ++)
                {
                    BlockPos blockPos = new BlockPos(x + localX, y + localY, z + localZ);
                    BlockPos localPos = new BlockPos(localX, localY, localZ);

                    IBlockState blockState = this.getBlockState(localPos);

                    if (!replaceBlocks && !world.isAirBlock(blockPos))
                    {
                        continue;
                    }

                    if (!placeAir && blockState.equals(Blocks.AIR.getDefaultState()))
                    {
                        continue;
                    }

                    world.setBlockState(blockPos, blockState);

                    TileEntity tileEntity = world.getTileEntity(blockPos);

                    if (tileEntity != null)
                    {
                        tileEntity.deserializeNBT(this.getTileEntity(localPos).serializeNBT());
                    }
                }
            }
        }

        world.loadEntities(this.entities);
        for (Entity entity : this.entities)
        {
            entity.setUniqueId(UUID.randomUUID());
            world.spawnEntity(entity);
        }
    }

    private void copyBlocks(World world, int minX, int minY, int minZ)
    {
        this.init();

        for (int localX = 0; localX <= this.width; localX++)
        {
            for (int localY = 0; localY <= this.height; localY++)
            {
                for (int localZ = 0; localZ <= this.length; localZ++)
                {
                    BlockPos blockPos = new BlockPos(minX + localX, minY + localY, minZ + localZ);
                    BlockPos localPos = new BlockPos(localX, localY, localZ);

                    IBlockState blockState = world.getBlockState(blockPos);
                    Block block = blockState.getBlock();
                    boolean success = this.setBlockState(localPos, blockState);

                    if (success && block.hasTileEntity(blockState))
                    {
                        this.copyTileEntity(world, blockPos, localPos);
                    }
                }
            }
        }
    }

    private void copyEntities(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
    {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
        List<Entity> entityList = world.getEntitiesWithinAABB(Entity.class, axisAlignedBB).stream()
                .filter(entity -> !(entity instanceof EntityPlayer))
                .collect(Collectors.toList());

        this.entities.clear();

        for (Entity entity : entityList)
        {
            this.addEntity(entity);
        }
    }

    private void copyTileEntity(World world, BlockPos blockPos, BlockPos localPos)
    {
        TileEntity tileEntity = world.getTileEntity(blockPos);

        if (tileEntity == null)
        {
            return;
        }

        try
        {
            this.setTileEntity(localPos, tileEntity);
        }
        catch (Exception exception)
        {
            this.setBlockState(localPos, Blocks.BEDROCK.getDefaultState());
        }
    }


    public void addEntity(Entity entity)
    {
        if (entity == null || entity instanceof EntityPlayer)
        {
            return;
        }

        for (Entity e : this.entities)
        {
            if (entity.getUniqueID().equals(e.getUniqueID()))
            {
                return;
            }
        }

        this.entities.add(entity);
    }


    public void removeEntity(Entity entity)
    {
        if (entity == null)
        {
            return;
        }

        this.entities.removeIf(e -> entity.getUniqueID().equals(e.getUniqueID()));
    }

    public List<Entity> getEntities()
    {
        return this.entities;
    }


    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setShort("Width", (short) this.width);
        tag.setShort("Height", (short) this.height);
        tag.setShort("Length", (short) this.length);

        int size = this.width * this.length * this.height;
        byte[] localBlocks = new byte[size];
        byte[] localMetadata = new byte[size];
        byte[] extraBlocks = new byte[size];
        byte[] extraBlocksNibble = new byte[(int) Math.ceil(size / 2.0)];
        boolean extra = false;

        Map<String, Short> mappings = new HashMap<>();
        for (int x = 0; x < this.width; x++)
        {
            for (int y = 0; y < this.height; y++)
            {
                for (int z = 0; z < this.length; z++)
                {
                    int index = x + (y * this.length + z) * this.width;
                    IBlockState blockState = this.getBlockState(new BlockPos(x, y, z));
                    Block block = blockState.getBlock();
                    int blockId = Block.REGISTRY.getIDForObject(block);
                    localBlocks[index] = (byte) blockId;
                    localMetadata[index] = (byte) block.getMetaFromState(blockState);

                    if ((extraBlocks[index] = (byte) (blockId >> 8)) > 0)
                    {
                        extra = true;
                    }

                    final String name = String.valueOf(Block.REGISTRY.getNameForObject(block));

                    if (!mappings.containsKey(name))
                    {
                        mappings.put(name, (short) blockId);
                    }
                }
            }
        }

        final NBTTagList tileEntitiesList = new NBTTagList();

        for (final TileEntity tileEntity : this.getTileEntities())
        {
            try
            {
                NBTTagCompound tileEntityTagCompound = tileEntity.serializeNBT();

                tileEntitiesList.appendTag(tileEntityTagCompound);
            }
            catch (final Exception e)
            {
                final BlockPos tePos = tileEntity.getPos();
                final int index = tePos.getX() + (tePos.getY() * this.length + tePos.getZ()) * this.width;

                localBlocks[index] = (byte) Block.REGISTRY.getIDForObject(Blocks.BEDROCK);
                localMetadata[index] = 0;
                extraBlocks[index] = 0;
            }
        }

        for (int i = 0; i < extraBlocksNibble.length; i++)
        {
            if (i * 2 + 1 < extraBlocks.length)
            {
                extraBlocksNibble[i] = (byte) ((extraBlocks[i * 2] << 4) | extraBlocks[i * 2 + 1]);
            }
            else
            {
                extraBlocksNibble[i] = (byte) (extraBlocks[i * 2] << 4);
            }
        }

        final NBTTagList entityList = new NBTTagList();
        final List<Entity> entities = this.getEntities();

        for (final Entity entity : entities)
        {
            final NBTTagCompound entityCompound = entity.serializeNBT();
            entityList.appendTag(entityCompound);
        }


        tag.setString("Materials", "Alpha");
        tag.setByteArray("Blocks", localBlocks);
        tag.setByteArray("Data", localMetadata);
        if (extra)
        {
            tag.setByteArray("AddBlocks", extraBlocksNibble);
        }
        tag.setTag("Entities", entityList);
        tag.setTag("TileEntities", tileEntitiesList);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        final short width = tag.getShort("Width");
        final short length = tag.getShort("Length");
        final short height = tag.getShort("Height");

        this.width = width;
        this.length = length;
        this.height = height;

        this.init();

        byte[] blocks = tag.getByteArray("Blocks");
        byte[] metadata = tag.getByteArray("Data");

        boolean extra = false;
        byte[] extraBlocks = null;
        byte[] extraBlocksNibble;

        if (tag.hasKey("AddBlocks"))
        {
            extra = true;
            extraBlocksNibble = tag.getByteArray("AddBlocks");
            extraBlocks = new byte[extraBlocksNibble.length * 2];

            for (int i = 0; i < extraBlocksNibble.length; i++)
            {
                extraBlocks[i * 2] = (byte) ((extraBlocksNibble[i] >> 4) & 0xF);
                extraBlocks[i * 2 + 1] = (byte) (extraBlocksNibble[i] & 0xF);
            }
        }

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                for (int z = 0; z < length; z++)
                {
                    int index = x + (y * length + z) * width;
                    /*
                     Some byte shi... magic. Almost from here:
                     https://github.com/Lunatrius/Schematica/blob/master/src/main/java/com/github/lunatrius/schematica/world/schematic/SchematicAlpha.java
                     */
                    int blockID = (blocks[index] & 0xFF) | (extra ? ((extraBlocks[index] & 0xFF) << 8) : 0);
                    int meta = metadata[index] & 0xFF;

                    Block block = Block.REGISTRY.getObjectById(blockID);
                    BlockPos blockPos = new BlockPos(x, y, z);
                    IBlockState blockState = block.getStateFromMeta(meta);
                    this.setBlockState(blockPos, blockState);
                }
            }
        }

        NBTTagList tileEntities = tag.getTagList("TileEntities", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < tileEntities.tagCount(); i++)
        {
            final TileEntity tileEntity = TileEntity.create(null, tileEntities.getCompoundTagAt(i));
            if (tileEntity != null)
            {
                this.setTileEntity(tileEntity.getPos(), tileEntity);
            }
        }
    }
}
