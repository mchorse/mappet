package mchorse.mappet.api.expressions;

import mchorse.mappet.api.expressions.functions.State;
import mchorse.mappet.api.expressions.functions.dialogue.DialogueRead;
import mchorse.mappet.api.expressions.functions.factions.FactionFriendly;
import mchorse.mappet.api.expressions.functions.factions.FactionHas;
import mchorse.mappet.api.expressions.functions.factions.FactionHostile;
import mchorse.mappet.api.expressions.functions.factions.FactionNeutral;
import mchorse.mappet.api.expressions.functions.factions.FactionScore;
import mchorse.mappet.api.expressions.functions.inventory.InventoryArmor;
import mchorse.mappet.api.expressions.functions.inventory.InventoryHas;
import mchorse.mappet.api.expressions.functions.inventory.InventoryHolds;
import mchorse.mappet.api.expressions.functions.player.PlayerArmor;
import mchorse.mappet.api.expressions.functions.player.PlayerHp;
import mchorse.mappet.api.expressions.functions.player.PlayerHunger;
import mchorse.mappet.api.expressions.functions.player.PlayerIsAlive;
import mchorse.mappet.api.expressions.functions.player.PlayerXp;
import mchorse.mappet.api.expressions.functions.player.PlayerXpLevel;
import mchorse.mappet.api.expressions.functions.quests.QuestCompleted;
import mchorse.mappet.api.expressions.functions.quests.QuestPresent;
import mchorse.mappet.api.expressions.functions.quests.QuestPresentCompleted;
import mchorse.mappet.api.expressions.functions.world.WorldIsDay;
import mchorse.mappet.api.expressions.functions.world.WorldIsNight;
import mchorse.mappet.api.expressions.functions.world.WorldTime;
import mchorse.mappet.api.expressions.functions.world.WorldTotalTime;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.MathBuilder;
import mchorse.mclib.math.Variable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class ExpressionManager
{
    public MathBuilder builder;
    public Variable varValue;
    public Variable varSubject;

    public MinecraftServer server;
    public World world;
    public EntityLivingBase subject;

    public ExpressionManager()
    {
        this.builder = new MathBuilder();
        this.builder.register(this.varValue = new Variable("value", 0));
        this.builder.register(this.varSubject = new Variable("subject", ""));

        /* Functions */
        this.builder.functions.put("quest_present", QuestPresent.class);
        this.builder.functions.put("quest_completed", QuestCompleted.class);
        this.builder.functions.put("quest_present_or_completed", QuestPresentCompleted.class);

        this.builder.functions.put("faction_friendly", FactionFriendly.class);
        this.builder.functions.put("faction_neutral", FactionNeutral.class);
        this.builder.functions.put("faction_hostile", FactionHostile.class);
        this.builder.functions.put("faction_has", FactionHas.class);
        this.builder.functions.put("faction_score", FactionScore.class);

        this.builder.functions.put("state", State.class);

        this.builder.functions.put("inv_has", InventoryHas.class);
        this.builder.functions.put("inv_holds", InventoryHolds.class);
        this.builder.functions.put("inv_armor", InventoryArmor.class);

        this.builder.functions.put("player_armor", PlayerArmor.class);
        this.builder.functions.put("player_hp", PlayerHp.class);
        this.builder.functions.put("player_hunger", PlayerHunger.class);
        this.builder.functions.put("player_is_alive", PlayerIsAlive.class);
        this.builder.functions.put("player_xp", PlayerXp.class);
        this.builder.functions.put("player_xp_level", PlayerXpLevel.class);

        this.builder.functions.put("dialogue_read", DialogueRead.class);

        this.builder.functions.put("world_time", WorldTime.class);
        this.builder.functions.put("world_total_time", WorldTotalTime.class);
        this.builder.functions.put("world_is_day", WorldIsDay.class);
        this.builder.functions.put("world_is_night", WorldIsNight.class);
    }

    /* TODO: look into caching these values or something */

    public ExpressionManager set(MinecraftServer server)
    {
        this.server = server;
        this.subject = null;
        this.world = server.getEntityWorld();

        this.varSubject.set("");
        this.varValue.set(0);

        return this;
    }

    public ExpressionManager set(World world)
    {
        this.server = world.getMinecraftServer();
        this.subject = null;
        this.world = world;

        this.varSubject.set("");
        this.varValue.set(0);

        return this;
    }

    public ExpressionManager set(EntityLivingBase subject)
    {
        this.server = subject.getServer();
        this.subject = subject;
        this.world = subject.getEntityWorld();

        this.varSubject.set(subject.getCachedUniqueIdString());
        this.varValue.set(0);

        return this;
    }

    public ExpressionManager set(double value)
    {
        this.varValue.set(value);

        return this;
    }

    public IValue evaluate(String expression)
    {
        try
        {
            return this.builder.parse(expression);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private void reset()
    {
        this.server = null;
        this.subject = null;
        this.world = null;

        this.varSubject.set("");
        this.varValue.set(0);
    }
}