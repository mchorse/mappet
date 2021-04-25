package mchorse.mappet.api.factions;

public enum FactionAttitude
{
    AGGRESSIVE, PASSIVE, FRIENDLY;

    public static FactionAttitude get(String name)
    {
        for (FactionAttitude attitude : values())
        {
            if (attitude.name().equals(name))
            {
                return attitude;
            }
        }

        return PASSIVE;
    }

    public boolean isAggressive()
    {
        return this == AGGRESSIVE;
    }

    public boolean isPassive()
    {
        return this == PASSIVE;
    }

    public boolean isFriendly()
    {
        return this == FRIENDLY;
    }
}