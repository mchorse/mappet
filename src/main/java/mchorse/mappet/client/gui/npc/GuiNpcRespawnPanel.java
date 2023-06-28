package mchorse.mappet.client.gui.npc;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.client.gui.triggers.GuiTriggerElement;
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
    public GuiTriggerElement triggerRespawn;

    public GuiNpcRespawnPanel(Minecraft mc)
    {
        super(mc);

        this.respawn = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.respawn.respawn"), (b) -> this.state.respawn.set(b.isToggled()));
        this.respawnDelay = new GuiTrackpadElement(mc, (v) -> this.state.respawnDelay.set(v.intValue()));
        this.respawnDelay.integer().limit(0);
        this.respawnOnCoordinates = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.respawn.respawn_on_coordinates"), (b) -> this.state.respawnOnCoordinates.set(b.isToggled()));
        this.respawnCoordinates = new GuiVecPosElement(mc, (pos) ->
        {
            this.state.respawnPosX.set(pos.x);
            this.state.respawnPosY.set(pos.y);
            this.state.respawnPosZ.set(pos.z);
        });
        this.respawnSaveUUID = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.respawn.respawn_save_uuid"), (b) ->
        {
            this.state.respawnSaveUUID.set(b.isToggled());

            /* Prevents final NPC despawn if the original NPC still exists */
            if (this.state.respawnSaveUUID.get() && this.state.respawnDelay.get() < 20)
            {
                this.respawnDelay.setValue(20);
                this.state.respawnDelay.set(20);
            }
        });
        this.triggerRespawn = new GuiTriggerElement(mc);

        this.add(this.respawn);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.respawn.respawn_delay")), this.respawnDelay);
        this.add(this.respawnOnCoordinates, this.respawnCoordinates);
        this.add(this.respawnSaveUUID);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.respawn.respawn_trigger")).background().marginTop(12).marginBottom(5), this.triggerRespawn);
    }

    @Override
    public void set(NpcState state)
    {
        super.set(state);

        this.respawn.toggled(state.respawn.get());
        this.respawnDelay.setValue(state.respawnDelay.get());
        this.respawnOnCoordinates.toggled(state.respawnOnCoordinates.get());
        this.respawnCoordinates.set(new Vec3d(state.respawnPosX.get(), state.respawnPosY.get(), state.respawnPosZ.get()));
        this.respawnSaveUUID.toggled(state.respawnSaveUUID.get());
        this.triggerRespawn.set(state.triggerRespawn);
    }
}
