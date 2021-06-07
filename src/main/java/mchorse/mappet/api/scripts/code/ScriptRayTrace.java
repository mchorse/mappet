package mchorse.mappet.api.scripts.code;

import mchorse.mappet.api.scripts.user.IScriptEntity;
import mchorse.mappet.api.scripts.user.IScriptRayTrace;
import mchorse.mappet.api.scripts.user.data.ScriptVector;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class ScriptRayTrace implements IScriptRayTrace
{
    private RayTraceResult result;
    private IScriptEntity entity;

    public ScriptRayTrace(RayTraceResult result)
    {
        this.result = result;
    }

    @Override
    public RayTraceResult getMinecraftRayTraceResult()
    {
        return this.result;
    }

    @Override
    public boolean isMissed()
    {
        return this.result.typeOfHit == RayTraceResult.Type.MISS;
    }

    @Override
    public boolean isBlock()
    {
        return this.result.typeOfHit == RayTraceResult.Type.BLOCK;
    }

    @Override
    public boolean isEntity()
    {
        return this.result.typeOfHit == RayTraceResult.Type.ENTITY;
    }

    @Override
    public IScriptEntity getEntity()
    {
        if (this.result.entityHit == null)
        {
            return null;
        }

        if (this.entity == null)
        {
            this.entity = new ScriptEntity(this.result.entityHit);
        }

        return this.entity;
    }

    @Override
    public ScriptVector getBlock()
    {
        BlockPos pos = this.result.getBlockPos();

        return new ScriptVector(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public ScriptVector getHitPosition()
    {
        Vec3d vec = this.result.hitVec;

        return new ScriptVector(vec.x, vec.y, vec.z);
    }
}