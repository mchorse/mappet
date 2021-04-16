package mchorse.mappet.client.renders;

import mchorse.mappet.tile.TileTrigger;
import mchorse.mclib.client.Draw;
import mchorse.mclib.utils.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

@SideOnly(Side.CLIENT)
public class TileTriggerRenderer extends TileBaseBlockRenderer<TileTrigger>
{
    public TileTriggerRenderer()
    {
        super(new Color(0.94F, 1F, 0.11F, 0.5F));
    }
}