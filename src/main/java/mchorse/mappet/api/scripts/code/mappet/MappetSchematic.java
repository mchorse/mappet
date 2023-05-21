package mchorse.mappet.api.scripts.code.mappet;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.schematics.Schematic;
import mchorse.mappet.api.scripts.user.IScriptWorld;
import mchorse.mappet.api.scripts.user.mappet.IMappetSchematic;

public class MappetSchematic implements IMappetSchematic
{
    private Schematic schematic;

    private final IScriptWorld world;

    public static MappetSchematic create(IScriptWorld world)
    {
        return new MappetSchematic(world);
    }

    public MappetSchematic(IScriptWorld world)
    {
        this.schematic = new Schematic();
        this.world = world;
    }

    @Override
    public MappetSchematic loadFromWorld(int x1, int y1, int z1, int x2, int y2, int z2)
    {
        this.schematic.loadFromWorld(this.world.getMinecraftWorld(), x1, y1, z1, x2, y2, z2);
        return this;
    }

    @Override
    public MappetSchematic place(int x, int y, int z, boolean replaceBlocks, boolean placeAir)
    {
        this.schematic.place(this.world.getMinecraftWorld(), x ,y ,z, replaceBlocks, placeAir);
        return this;
    }

    @Override
    public IMappetSchematic place(int x, int y, int z, boolean replaceBlocks)
    {
        return this.place(x, y, z, replaceBlocks, true);
    }

    @Override
    public IMappetSchematic place(int x, int y, int z)
    {
        return this.place(x, y, z, true, true);
    }

    @Override
    public MappetSchematic saveToFile(String name)
    {
        Mappet.schematics.save(name, this.schematic);
        return this;
    }

    @Override
    public MappetSchematic loadFromFile(String name)
    {
        this.schematic = Mappet.schematics.load(name);
        return this;
    }
}
