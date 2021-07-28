package mchorse.mappet.client.gui.huds;

import mchorse.mappet.api.huds.HUDMorph;
import mchorse.mappet.api.huds.HUDScene;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlayPanel;
import mchorse.mappet.utils.Colors;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.Consumer;

public class GuiHUDMorphsOverlayPanel extends GuiOverlayPanel
{
    public GuiHUDMorphListElement morphs;

    private HUDScene scene;
    private Consumer<HUDMorph> callback;

    public GuiHUDMorphsOverlayPanel(Minecraft mc, HUDScene scene, Consumer<HUDMorph> callback)
    {
        super(mc, IKey.lang("mappet.gui.huds.overlay.title"));

        this.scene = scene;
        this.callback = callback;

        this.morphs = new GuiHUDMorphListElement(mc, (list) -> this.accept(list.get(0)));
        this.morphs.sorting().context(() ->
        {
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(mc);

            menu.action(Icons.ADD, IKey.lang("mappet.gui.huds.context.add"), this::addMorph);

            if (!this.morphs.isDeselected())
            {
                menu.action(Icons.COPY, IKey.lang("mappet.gui.huds.context.copy"), this::copyMorph);

                try
                {
                    NBTTagCompound tag = JsonToNBT.getTagFromJson(GuiScreen.getClipboardString());

                    if (tag.hasKey("_HUDMorphCopy"))
                    {
                        HUDMorph morph = new HUDMorph();

                        morph.deserializeNBT(tag);
                        menu.action(Icons.PASTE, IKey.lang("mappet.gui.huds.context.paste"), () -> this.addMorph(morph));
                    }
                }
                catch (Exception e)
                {}

                menu.action(Icons.REMOVE, IKey.lang("mappet.gui.huds.context.remove"), this::removeMorph, Colors.NEGATIVE);
            }

            return menu;
        });
        this.morphs.flex().relative(this.content).wh(1F, 1F);

        this.morphs.setList(this.scene.morphs);
        this.morphs.scroll.scrollSpeed *= 2;

        this.content.add(this.morphs);
    }

    private void addMorph()
    {
        this.addMorph(new HUDMorph());
    }

    private void addMorph(HUDMorph morph)
    {
        this.scene.morphs.add(morph);
        this.morphs.update();

        this.morphs.setCurrentScroll(morph);
        this.accept(morph);
    }

    private void copyMorph()
    {
        NBTTagCompound tag = this.morphs.getCurrentFirst().serializeNBT();
        tag.setBoolean("_HUDMorphCopy", true);

        GuiScreen.setClipboardString(tag.toString());
    }

    private void removeMorph()
    {
        int index = this.morphs.getIndex();

        this.scene.morphs.remove(this.morphs.getIndex());
        this.morphs.update();
        this.morphs.setIndex(index < 1 ? 0 : index - 1);

        this.accept(this.morphs.getCurrentFirst());
    }

    public GuiHUDMorphsOverlayPanel set(HUDMorph morph)
    {
        this.morphs.setCurrentScroll(morph);

        return this;
    }

    protected void accept(HUDMorph morph)
    {
        if (this.callback != null)
        {
            this.callback.accept(morph);
        }
    }

    @Override
    public void draw(GuiContext context)
    {
        super.draw(context);

        if (this.scene.morphs.size() <= 1)
        {
            GuiMappetUtils.drawRightClickHere(context, this.area);
        }
    }
}