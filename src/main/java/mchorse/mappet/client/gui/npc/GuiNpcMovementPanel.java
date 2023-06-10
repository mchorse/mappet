package mchorse.mappet.client.gui.npc;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.client.gui.utils.GuiBlockPosAndTriggerList;
import mchorse.mappet.client.gui.utils.GuiBlockPosElement;
import mchorse.mappet.client.gui.utils.GuiBlockPosList;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiNpcMovementPanel extends GuiNpcPanel
{
    public GuiTrackpadElement speed;
    public GuiTrackpadElement jumpPower;
    public GuiBlockPosList steeringOffset;
    public GuiToggleElement canSwim;
    public GuiToggleElement immovable;
    public GuiToggleElement hasPost;
    public GuiBlockPosElement postPosition;
    public GuiTrackpadElement postRadius;
    public GuiToggleElement patrolCirculate;
    public GuiBlockPosAndTriggerList patrol;
    public GuiTextElement follow;

    public GuiNpcMovementPanel(Minecraft mc)
    {
        super(mc);

        this.speed = new GuiTrackpadElement(mc, (v) -> this.state.speed = v.floatValue());
        this.jumpPower = new GuiTrackpadElement(mc, (v) -> this.state.jumpPower = v.floatValue());
        this.steeringOffset = new GuiBlockPosList(mc);

        GuiLabel steeringOffsetLabel = Elements.label(IKey.lang("mappet.gui.npcs.movement.steering_offset")).background();
        GuiIconElement addSteeringOffset = new GuiIconElement(mc, Icons.ADD, (b) -> this.steeringOffset.addBlockPos());

        addSteeringOffset.flex().relative(steeringOffsetLabel).xy(1F, 0.5F).w(10).anchor(1F, 0.5F);
        steeringOffsetLabel.add(addSteeringOffset);

        this.canSwim = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.movement.can_swim"), (b) -> this.state.canSwim = b.isToggled());
        this.immovable = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.movement.immovable"), (b) -> this.state.immovable = b.isToggled());
        this.hasPost = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.movement.post"), (b) -> this.state.hasPost = b.isToggled());
        this.postPosition = new GuiBlockPosElement(mc, (pos) -> this.state.postPosition = pos);
        this.postRadius = new GuiTrackpadElement(mc, (v) -> this.state.postRadius = v.floatValue());
        this.patrolCirculate = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.movement.patrol_circulate"), (b) -> this.state.patrolCirculate = b.isToggled());
        this.patrol = new GuiBlockPosAndTriggerList(mc);
        this.follow = new GuiTextElement(mc, 1000, (t) -> this.state.follow = t);

        GuiLabel patrolLabel = Elements.label(IKey.lang("mappet.gui.npcs.movement.patrol_points")).background();
        GuiIconElement addPatrol = new GuiIconElement(mc, Icons.ADD, (b) -> this.patrol.addBlockPos());
        addPatrol.flex().relative(patrolLabel).xy(1F, 0.5F).w(10).anchor(1F, 0.5F);
        patrolLabel.add(addPatrol);

        this.add(Elements.label(IKey.lang("mappet.gui.npcs.movement.speed")), this.speed);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.movement.jump_power")), this.jumpPower);
        this.add(this.canSwim, this.immovable);
        this.add(this.hasPost.marginTop(12), this.postPosition, this.postRadius);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.movement.follow")).marginTop(12), this.follow);

        GuiElement patrolArea = new GuiElement(mc);
        patrolArea.add(patrolLabel, this.patrol, this.patrolCirculate);
        patrolArea.flex().column(5).vertical().stretch();
        this.add(patrolArea.marginTop(12));

        GuiElement steeringOffsetArea = new GuiElement(mc);
        steeringOffsetArea.add(steeringOffsetLabel, this.steeringOffset);
        steeringOffsetArea.flex().column(5).vertical().stretch();
        this.add(steeringOffsetArea.marginTop(12));
    }

    @Override
    public void set(NpcState state)
    {
        super.set(state);

        this.speed.setValue(state.speed);
        this.jumpPower.setValue(state.jumpPower);
        this.steeringOffset.set(state.steeringOffset);
        this.canSwim.toggled(state.canSwim);
        this.immovable.toggled(state.immovable);
        this.hasPost.toggled(state.hasPost);
        this.postPosition.set(state.postPosition);
        this.postRadius.setValue(state.postRadius);
        this.patrolCirculate.toggled(state.patrolCirculate);
        this.patrol.set(state.patrol, state.patrolTriggers);
        this.follow.setText(state.follow);
    }
}