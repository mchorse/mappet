package mchorse.mappet.entities.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSource;

import javax.annotation.Nullable;

public class NpcDamageSource extends EntityDamageSource
{
    public NpcDamageSource(@Nullable Entity sourceMob)
    {
        super("mob", sourceMob);
    }

    @Override
    public boolean isDifficultyScaled()
    {
        return false;
    }
}