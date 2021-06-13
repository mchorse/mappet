package mchorse.mappet.client.gui.npc;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.IGuiElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiInventoryElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.ScrollDirection;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GuiNpcEditor extends GuiScrollElement
{
    private NpcState state;

    private GuiNpcMetaPanel meta;
    private GuiNpcGeneralPanel general;
    private GuiNpcHealthPanel health;
    private GuiNpcDamagePanel damage;
    private GuiNpcMovementPanel movement;
    private GuiNpcBehaviorPanel behavior;

    public GuiNpcEditor(Minecraft mc, boolean id, Supplier<GuiInventoryElement> inventory)
    {
        super(mc, ScrollDirection.HORIZONTAL);

        this.scroll.scrollSpeed *= 2;

        this.meta = new GuiNpcMetaPanel(mc, id);
        this.general = new GuiNpcGeneralPanel(mc, inventory);
        this.health = new GuiNpcHealthPanel(mc);
        this.damage = new GuiNpcDamagePanel(mc);
        this.movement = new GuiNpcMovementPanel(mc);
        this.behavior = new GuiNpcBehaviorPanel(mc);

        this.flex().column(5).scroll().width(180).padding(15);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.meta.title")).background().marginBottom(6));
        this.addChildren(this.meta);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.general.title")).background().marginBottom(6).marginTop(24));
        this.addChildren(this.general);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.health.title")).background().marginBottom(6).marginTop(24));
        this.addChildren(this.health);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.damage.title")).background().marginBottom(6).marginTop(24));
        this.addChildren(this.damage);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.movement.title")).background().marginBottom(6).marginTop(24));
        this.addChildren(this.movement);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.behavior.title")).background().marginBottom(6).marginTop(24));
        this.addChildren(this.behavior);
    }

    private void addChildren(GuiElement element)
    {
        List<IGuiElement> elements = new ArrayList<IGuiElement>(element.getChildren());

        for (IGuiElement child : elements)
        {
            if (child instanceof GuiElement)
            {
                ((GuiElement) child).removeFromParent();

                this.add(child);
            }
        }
    }

    public void set(NpcState state)
    {
        this.state = state;

        this.meta.set(state);
        this.general.set(state);
        this.health.set(state);
        this.damage.set(state);
        this.movement.set(state);
        this.behavior.set(state);

        this.resize();
    }

    public NpcState get()
    {
        return this.state;
    }
}