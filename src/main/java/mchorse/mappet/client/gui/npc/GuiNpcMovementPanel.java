package mchorse.mappet.client.gui.npc;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.client.gui.utils.GuiBlockPosElement;
import mchorse.mappet.client.gui.utils.GuiBlockPosList;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
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
    public GuiToggleElement canSwim;
    public GuiToggleElement hasPost;
    public GuiBlockPosElement postPosition;
    public GuiTrackpadElement postRadius;
    public GuiToggleElement patrolCirculate;
    public GuiBlockPosList patrol;
    public GuiTextElement follow;

    public GuiNpcMovementPanel(Minecraft mc)
    {
        super(mc);

        this.speed = new GuiTrackpadElement(mc, (v) -> this.state.speed = v.floatValue());
        this.canSwim = new GuiToggleElement(mc, IKey.str("Can swim"), (b) -> this.state.canSwim = b.isToggled());
        this.hasPost = new GuiToggleElement(mc, IKey.str("Has a post"), (b) -> this.state.hasPost = b.isToggled());
        this.postPosition = new GuiBlockPosElement(mc, (pos) -> this.state.postPosition = pos);
        this.postRadius = new GuiTrackpadElement(mc, (v) -> this.state.postRadius = v.floatValue());
        this.patrolCirculate = new GuiToggleElement(mc, IKey.str("Circular patrol"), (b) -> this.state.patrolCirculate = b.isToggled());
        this.patrol = new GuiBlockPosList(mc);
        this.follow = new GuiTextElement(mc, 1000, (t) -> this.state.follow = t);

        GuiLabel patrolLabel = Elements.label(IKey.str("Patrol points")).background();
        patrolLabel.marginTop(12);
        GuiIconElement add = new GuiIconElement(mc, Icons.ADD, (b) -> this.patrol.addBlockPos());

        add.flex().relative(patrolLabel).x(1F, -20);
        patrolLabel.add(add);

        this.scroll.add(Elements.label(IKey.str("Speed")).background(), this.speed);
        this.scroll.add(this.canSwim);
        this.scroll.add(Elements.label(IKey.str("Post point")).background().marginTop(12), this.hasPost, this.postPosition, this.postRadius);
        this.scroll.add(patrolLabel, this.patrolCirculate, this.patrol);
        this.scroll.add(Elements.label(IKey.str("Follow target")).background().marginTop(12), this.follow);
    }

    @Override
    public void set(NpcState state)
    {
        super.set(state);

        this.speed.setValue(state.speed);
        this.canSwim.toggled(state.canSwim);
        this.hasPost.toggled(state.hasPost);
        this.postPosition.set(state.postPosition);
        this.postRadius.setValue(state.postRadius);
        this.patrolCirculate.toggled(state.patrolCirculate);
        this.patrol.set(state.patrol);
        this.follow.setText(state.follow);
    }
}