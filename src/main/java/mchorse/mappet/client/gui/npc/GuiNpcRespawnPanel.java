package mchorse.mappet.client.gui.npc;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.client.gui.utils.GuiVecPosElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;

public class GuiNpcRespawnPanel extends GuiNpcPanel
{
    public GuiToggleElement respawn;
    public GuiTrackpadElement respawnDelay;

    public GuiToggleElement respawnOnCoordinates;

    public GuiVecPosElement respawnCoordinates;

    public GuiToggleElement respawnSaveUUID;

    public GuiNpcRespawnPanel(Minecraft mc)
    {
        super(mc);

        this.respawn = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.respawn.isRespawn"), (b) -> this.state.respawn = b.isToggled());
        this.respawnDelay = new GuiTrackpadElement(mc, (v) -> this.state.respawnDelay = v.intValue());
        this.respawnDelay.integer().limit(0);
        this.respawnOnCoordinates = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.respawn.isRespawnOnCoordinates"), (b) -> this.state.respawnOnCoorinates = b.isToggled());
        this.respawnCoordinates = new GuiVecPosElement(mc, (pos) -> {
            this.state.respawnPosX = pos.x;
            this.state.respawnPosY = pos.y;
            this.state.respawnPosZ = pos.z;
        });
        this.respawnSaveUUID = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.respawn.isRespawnSaveUUID"), (b) -> this.state.respawnSaveUUID = b.isToggled());


        this.add(this.respawn);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.respawn.respawnDelay")), this.respawnDelay);
        this.add(this.respawnOnCoordinates, this.respawnCoordinates);
        this.add(this.respawnSaveUUID);
    }

    @Override
    public void set(NpcState state)
    {
        super.set(state);

        this.respawn.toggled(state.respawn);
        this.respawnDelay.setValue(state.respawnDelay);
        this.respawnOnCoordinates.toggled(state.respawnOnCoorinates);
        this.respawnCoordinates.set(new Vec3d(state.respawnPosX, state.respawnPosY, state.respawnPosZ));
        this.respawnSaveUUID.toggled(state.respawnSaveUUID);
    }
}
