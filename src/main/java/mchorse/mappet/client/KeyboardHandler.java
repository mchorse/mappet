package mchorse.mappet.client;

import mchorse.mappet.client.gui.GuiJournalScreen;
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
    public KeyBinding openJournal;

    public KeyboardHandler()
    {
        String prefix = "mappet.keys.";

        this.openMappetDashboard = new KeyBinding(prefix + "dashboard", Keyboard.KEY_EQUALS, prefix + "category");
        this.openJournal = new KeyBinding(prefix + "journal", Keyboard.KEY_J, prefix + "category");

        ClientRegistry.registerKeyBinding(this.openMappetDashboard);
        ClientRegistry.registerKeyBinding(this.openJournal);
    }

    @SubscribeEvent
    public void onKeyPress(KeyInputEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (this.openMappetDashboard.isPressed())
        {
            mc.displayGuiScreen(GuiMappetDashboard.get(mc));
        }

        if (this.openJournal.isPressed())
        {
            mc.displayGuiScreen(new GuiJournalScreen(mc));
        }
    }
}