package mchorse.mappet.api.expressions;

import mchorse.mappet.api.expressions.functions.State;
import mchorse.mappet.api.expressions.functions.factions.FactionFriendly;
import mchorse.mappet.api.expressions.functions.factions.FactionHas;
import mchorse.mappet.api.expressions.functions.factions.FactionHostile;
import mchorse.mappet.api.expressions.functions.factions.FactionNeutral;
import mchorse.mappet.api.expressions.functions.factions.FactionScore;
import mchorse.mappet.api.expressions.functions.inventory.InventoryArmor;
import mchorse.mappet.api.expressions.functions.inventory.InventoryHas;
import mchorse.mappet.api.expressions.functions.inventory.InventoryHolds;
import mchorse.mappet.api.expressions.functions.quests.QuestCompleted;
import mchorse.mappet.api.expressions.functions.quests.QuestPresent;
import mchorse.mappet.api.expressions.functions.quests.QuestPresentCompleted;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.MathBuilder;
import mchorse.mclib.math.Variable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;

public class ExpressionManager
{
    public MathBuilder builder;
    public Variable varValue;
    public Variable varSubject;

    public MinecraftServer server;
    public EntityLivingBase subject;
    public EntityLivingBase object;

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
    }

    /* TODO: look into caching these values or something */

    public IValue evalute(String expression, MinecraftServer server, EntityLivingBase subject, double value)
    {
        this.server = server;
        this.subject = subject;
        this.object = null;

        this.varSubject.set(subject == null ? "" : subject.getCachedUniqueIdString());
        this.varValue.set(value);

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

    public IValue evalute(String expression, MinecraftServer server, EntityLivingBase subject)
    {
        this.server = server;
        this.subject = subject;
        this.object = null;

        this.varSubject.set(subject == null ? "" : subject.getCachedUniqueIdString());
        this.varValue.set(0);

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
}