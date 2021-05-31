package mchorse.mappet.api.scripts.code.mappet;

import mchorse.mappet.api.scripts.user.mappet.IMappetStates;
import mchorse.mappet.api.states.States;

import java.util.Set;

public class MappetStates implements IMappetStates
{
    private States states;

    public MappetStates(States states)
    {
        this.states = states;
    }

    @Override
    public double add(String id, double value)
    {
        this.states.add(id, value);

        return this.states.get(id);
    }

    @Override
    public void set(String id, double value)
    {
        this.states.set(id, value);
    }

    @Override
    public double get(String id)
    {
        return this.states.get(id);
    }

    @Override
    public void reset(String id)
    {
        this.states.reset(id);
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