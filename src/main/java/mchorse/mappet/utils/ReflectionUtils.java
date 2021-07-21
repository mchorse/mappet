package mchorse.mappet.utils;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.Set;

public class ReflectionUtils
{
    public static Set<TileEntity> getGlobalTiles(RenderGlobal global)
    {
        return ObfuscationReflectionHelper.<Set<TileEntity>, RenderGlobal>getPrivateValue(RenderGlobal.class, global, "setTileEntities", "field_181024_n");
    }
}