package mchorse.mappet.client.renders;

import mchorse.mclib.client.Draw;
import mchorse.mclib.utils.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

@SideOnly(Side.CLIENT)
public abstract class TileBaseBlockRenderer <T extends TileEntity> extends TileEntitySpecialRenderer<T>
{
    protected Color color;

    public TileBaseBlockRenderer(Color color)
    {
        this.color = color;
    }

    @Override
    public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.gameSettings.showDebugInfo && !mc.gameSettings.hideGUI)
        {
            int shader = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);

            if (shader != 0)
            {
                OpenGlHelper.glUseProgram(0);
            }

            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();

            Draw.cube(x + 0.25F, y + 0.25F, z + 0.25F, x + 0.75F, y + 0.75F, z + 0.75F, this.color.r, this.color.g, this.color.b, this.color.a);

            this.drawMoreDebug(te, x, y, z, partialTicks, destroyStage, alpha);

            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();

            if (shader != 0)
            {
                OpenGlHelper.glUseProgram(shader);
            }
        }
    }

    protected void drawMoreDebug(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {}
}
