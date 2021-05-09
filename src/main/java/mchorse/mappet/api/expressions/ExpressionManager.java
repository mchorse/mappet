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
import mchorse.mappet.api.utils.DataContext;
import mchorse.mclib.math.Constant;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.MathBuilder;
import mchorse.mclib.math.Variable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.Map;

public class ExpressionManager
{
    public static IValue ONE = new Constant(1);
    public static IValue ZERO = new Constant(0);

    public MathBuilder builder;
    public DataContext context;

    public ExpressionManager()
    {
        this.builder = new MathBuilder();

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

    private void reset()
    {
        for (Map.Entry<String, Variable> entry : this.builder.variables.entrySet())
        {
            String key = entry.getKey();
            Variable variable = entry.getValue();

            if (key.equals("PI") || key.equals("E"))
            {
                continue;
            }

            if (variable.isNumber())
            {
                variable.set(0);
            }
            else
            {
                variable.set("");
            }
        }

        this.context = null;
    }

    public ExpressionManager set(World world)
    {
        return this.set(new DataContext(world));
    }

    public ExpressionManager set(EntityLivingBase subject)
    {
        return this.set(new DataContext(subject));
    }

    public ExpressionManager set(DataContext context)
    {
        this.reset();

        this.context = context;

        for (Map.Entry<String, Object> entry : context.getValues())
        {
            String key = entry.getKey();
            Variable variable = this.builder.variables.get(key);

            if (variable == null)
            {
                variable = new Variable(key, 0);
                this.builder.register(variable);
            }

            if (entry.getValue() instanceof Number)
            {
                variable.set(((Number) entry.getValue()).doubleValue());
            }
            else if (entry.getValue() instanceof String)
            {
                variable.set((String) entry.getValue());
            }
        }

        return this;
    }

    public IValue evaluate(String expression)
    {
        return this.evaluate(expression, ZERO);
    }

    public IValue evaluate(String expression, IValue defaultValue)
    {
        try
        {
            return this.builder.parse(expression);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return defaultValue;
    }

    /* External API */

    public World getWorld()
    {
        return this.context.world;
    }

    public MinecraftServer getServer()
    {
        return this.context.getSender().getServer();
    }
}