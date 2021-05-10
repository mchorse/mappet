package mchorse.mappet.client.gui.factions;

import mchorse.mappet.api.factions.FactionAttitude;
import mchorse.mappet.client.gui.panels.GuiFactionPanel;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.util.Map;

public class GuiFactions extends GuiElement
{
    private Map<String, FactionAttitude> relations;

    public GuiFactions(Minecraft mc)
    {
        super(mc);

        this.flex().column(5).stretch().vertical();
    }

    /**
     * Add a relation to the relation map
     */
    public void addRelation(String faction, FactionAttitude attitude, boolean put)
    {
        GuiCirculateElement button = GuiFactionPanel.createButton(this.mc, (a) ->
        {
            this.relations.put(faction, a);
        });
        GuiElement row = Elements.row(this.mc, 5, Elements.label(IKey.str(faction), 20).anchor(0, 0.5F), button);

        GuiFactionPanel.setValue(button, attitude);
        row.context(() -> new GuiSimpleContextMenu(this.mc).action(Icons.REMOVE, IKey.lang("mappet.gui.factions.relations.context.remove"), () ->
        {
            row.removeFromParent();
            this.relations.remove(faction);
            this.getParentContainer().resize();
        }));

        this.add(row);

        if (put)
        {
            this.relations.put(faction, attitude);
        }

        this.getParentContainer().resize();
    }

    /**
     * Fill in faction's relation data
     */
    public void set(Map<String, FactionAttitude> relations)
    {
        this.relations = relations;

        this.removeAll();

        for (String key : relations.keySet())
        {
            this.addRelation(key, relations.get(key), false);
        }
    }
}