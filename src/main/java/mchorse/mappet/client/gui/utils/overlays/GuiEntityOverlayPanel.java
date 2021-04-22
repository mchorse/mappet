package mchorse.mappet.client.gui.utils.overlays;

import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiStringListElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.function.Consumer;

public class GuiEntityOverlayPanel extends GuiOverlayPanel
{
    public GuiTextElement id;
    public GuiStringListElement entities;

    private Consumer<ResourceLocation> callback;

    public GuiEntityOverlayPanel(Minecraft mc, Consumer<ResourceLocation> callback)
    {
        super(mc, IKey.str("Pick entity ID..."));

        this.callback = callback;

        this.id = new GuiTextElement(mc, 1000, this::accept);
        this.entities = new GuiStringListElement(mc, (list) ->
        {
            this.accept(list.get(0));
            this.id.setText(list.get(0));
        });

        this.id.flex().relative(this.content).w(1F);
        this.entities.flex().relative(this.content).xy(0, 20).w(1F).h(1F, -20);

        for (ResourceLocation location : ForgeRegistries.ENTITIES.getKeys())
        {
            this.entities.add(location.toString());
        }

        this.entities.sort();

        this.content.add(this.id, this.entities);
    }

    public GuiEntityOverlayPanel set(ResourceLocation rl)
    {
        this.id.setText(rl.toString());
        this.entities.setCurrentScroll(rl.toString());

        return this;
    }

    private void accept(String string)
    {
        if (this.callback != null)
        {
            this.callback.accept(new ResourceLocation(string));
        }
    }
}