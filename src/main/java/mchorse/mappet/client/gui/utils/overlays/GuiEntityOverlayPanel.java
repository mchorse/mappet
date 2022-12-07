package mchorse.mappet.client.gui.utils.overlays;

import mchorse.mappet.api.quests.objectives.KillObjective;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class GuiEntityOverlayPanel extends GuiResourceLocationOverlayPanel
{
    private static Set<ResourceLocation> getKeys()
    {
        Set<ResourceLocation> keys = new HashSet<ResourceLocation>(ForgeRegistries.ENTITIES.getKeys());

        keys.add(KillObjective.PLAYER_ID);

        return keys;
    }

    public GuiEntityOverlayPanel(Minecraft mc, Consumer<ResourceLocation> callback)
    {
        super(mc, IKey.lang("mappet.gui.overlays.entities.main"), getKeys(), callback);
    }
}