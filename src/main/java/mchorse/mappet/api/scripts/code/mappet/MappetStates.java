package mchorse.mappet.api.scripts.code.mappet;

import mchorse.mappet.api.scripts.user.mappet.IMappetStates;
import mchorse.mappet.api.states.States;

import java.util.Set;

public class MappetStates implements IMappetStates
{
    public States states;

    public MappetStates(States states)
    {
        this.states = states;
    }

    @Override
    public double add(String id, double value)
    {
        this.states.add(id, value);

        return this.states.getNumber(id);
    }

    @Override
    public void setNumber(String id, double value)
    {
        this.states.setNumber(id, value);
    }

    @Override
    public void setString(String id, String value)
    {
        this.states.setString(id, value);
    }

    @Override
    public double getNumber(String id)
    {
        return this.states.getNumber(id);
    }

    @Override
    public boolean isNumber(String id)
    {
        return this.states.isNumber(id);
    }

    @Override
    public String getString(String id)
    {
        return this.states.getString(id);
    }

    @Override
    public boolean isString(String id)
    {
        return this.states.isString(id);
    }

    @Override
    public void reset(String id)
    {
        this.states.reset(id);
    }

    @Override
    public void resetMasked(String id)
    {
        this.states.resetMasked(id);
    }

    @Override
    public void clear()
    {
        this.states.clear();
    }

    @Override
    public boolean has(String id)
    {
        return this.states.values.containsKey(id);
    }

    @Override
    public Set<String> keys()
    {
        return this.states.values.keySet();
    }
}