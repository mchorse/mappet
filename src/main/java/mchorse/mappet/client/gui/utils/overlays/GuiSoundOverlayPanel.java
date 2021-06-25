package mchorse.mappet.client.gui.utils.overlays;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import mchorse.mappet.utils.Utils;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.client.gui.utils.keys.LangKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.io.IOUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class GuiSoundOverlayPanel extends GuiResourceLocationOverlayPanel
{
    private static Set<ResourceLocation> extraSounds = new HashSet<ResourceLocation>();
    private static long lastUpdate;

    private static Set<ResourceLocation> getSoundEvents()
    {
        Set<ResourceLocation> locations = new HashSet<ResourceLocation>();

        if (lastUpdate < LangKey.lastTime)
        {
            extraSounds.clear();

            updateSounds("b.a");
            updateSounds("mp.sounds");

            lastUpdate = LangKey.lastTime;
        }

        locations.addAll(ForgeRegistries.SOUND_EVENTS.getKeys());
        locations.addAll(extraSounds);

        return locations;
    }

    private static void updateSounds(String rp)
    {
        try
        {
            IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(rp, "sounds.json"));
            JsonElement element = new JsonParser().parse(IOUtils.toString(resource.getInputStream(), Utils.getCharset()));

            if (element.isJsonObject())
            {
                for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet())
                {
                    extraSounds.add(new ResourceLocation(rp, entry.getKey()));
                }
            }
        }
        catch (Exception e)
        {}
    }

    public GuiSoundOverlayPanel(Minecraft mc, Consumer<ResourceLocation> callback)
    {
        super(mc, IKey.lang("mappet.gui.overlays.sounds.main"), getSoundEvents(), callback);

        GuiIconElement edit = new GuiIconElement(mc, Icons.SOUND, (b) -> this.playSound());

        edit.flex().wh(16, 16);
        this.icons.add(edit);
    }

    private void playSound()
    {
        if (this.rls.list.getIndex() <= 0)
        {
            return;
        }

        ResourceLocation location = new ResourceLocation(this.rls.list.getCurrentFirst());
        float x = (float) this.mc.player.posX;
        float y = (float) this.mc.player.posY;
        float z = (float) this.mc.player.posZ;

        this.mc.getSoundHandler().playSound(new PositionedSoundRecord(location, SoundCategory.MASTER, 1, 1, false, 0, ISound.AttenuationType.LINEAR, x, y, z));
    }
}