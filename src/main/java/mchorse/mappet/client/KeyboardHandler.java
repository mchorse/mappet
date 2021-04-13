package mchorse.mappet.client;

import mchorse.mappet.client.gui.GuiMappetDashboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

/**
 * Keyboard handler
 * 
 * This class is responsible for handling keyboard input (i.e. key 
 * presses) and storing keybindings associated with this mod.
 */
@SideOnly(Side.CLIENT)
public class KeyboardHandler
{
    public KeyBinding openMappetDashboard;

    public KeyboardHandler()
    {
        String prefix = "mappet.keys.";

        this.openMappetDashboard = new KeyBinding(prefix + "dashboard", Keyboard.KEY_NONE, prefix + "category");

        ClientRegistry.registerKeyBinding(this.openMappetDashboard);
    }

    @SubscribeEvent
    public void onKeyPress(KeyInputEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (this.openMappetDashboard.isPressed())
        {
            mc.displayGuiScreen(new GuiMappetDashboard(mc));
        }
    }
}