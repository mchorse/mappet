package mchorse.mappet.api.scripts.user.mappet;

public interface IMappetSchematic
{
    /**
     * Uses to copy blocks from world to schematic.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var schematic = c.world.createSchematic();
     *     schematic.loadFromWorld(0, 4, 0, 4, 8, 4).saveToFile("mySchematic").place(0, 4, 4).place(0, 4, 8);
     * }
     * }</pre>
     *
     * @return {@link IMappetSchematic}
     */
    IMappetSchematic loadFromWorld(int x1, int y1, int z1, int x2, int y2, int z2);

    /**
     * Places schematic's blocks into the world.
     *
     * <pre>{@code
     * function main(c)
     * {
     *      var schematic = c.world.createSchematic();
     *      schematic.loadFromFile("myTestSchematic").place(0, 0, 0, true, true);
     * }
     * }</pre>
     *
     * @return {@link IMappetSchematic}
     */
    IMappetSchematic place(int x, int y, int z, boolean replaceBlocks, boolean placeAir);

    /**
     * Places schematic's blocks into the world.
     *
     * <pre>{@code
     * function main(c)
     * {
     *      var schematic = c.world.createSchematic();
     *      schematic.loadFromFile("myTestSchematic").place(0, 0, 0, true);
     * }
     * }</pre>
     *
     * @return {@link IMappetSchematic}
     */
    IMappetSchematic place(int x, int y, int z, boolean replaceBlocks);

    /**
     * Places schematic's blocks into the world.
     *
     * <pre>{@code
     * function main(c)
     * {
     *      var schematic = c.world.createSchematic();
     *      schematic.loadFromFile("myTestSchematic").place(0, 0, 0);
     * }
     * }</pre>
     *
     * @return {@link IMappetSchematic}
     */
    IMappetSchematic place(int x, int y, int z);

    /**
     * Uses to save schematic.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var schematic = c.world.createSchematic();
     *     schematic.loadFromWorld(0, 4, 0, 4, 8, 4).saveToFile("mySchematic");
     * }
     * }</pre>
     *
     * @return {@link IMappetSchematic}
     */
    IMappetSchematic saveToFile(String name);

    /**
     * Uses to get schematic from file.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var schematic = c.world.createSchematic();
     *     schematic.loadFromFile("mySchematic").place(0, 4, 4);
     * }
     * }</pre>
     *
     * @return {@link IMappetSchematic}
     */
    IMappetSchematic loadFromFile(String name);
}
