package mchorse.mappet.client.renders.tile;

import mchorse.mappet.tile.TileTrigger;
import mchorse.mclib.utils.Color;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileTriggerRenderer extends TileBaseBlockRenderer<TileTrigger>
{
    public TileTriggerRenderer()
    {
        super(new Color(0.94F, 1F, 0.11F, 0.5F));
    }
}