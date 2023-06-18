package mchorse.mappet.api.scripts.code.mappet.blocks;

import mchorse.blockbuster.common.tileentity.TileEntityModelSettings;
import mchorse.mappet.api.scripts.code.items.ScriptItemStack;
import mchorse.mappet.api.scripts.user.mappet.blocks.IMappetModelSettings;

public class MappetModelSettings implements IMappetModelSettings {
    protected TileEntityModelSettings settings;
    protected Object modelBlock;

    public MappetModelSettings()
    {
        this(new TileEntityModelSettings());
    }

    public MappetModelSettings(TileEntityModelSettings settings)
    {
        this.settings = settings;
    }

    public MappetModelSettings(Object modelBlock)
    {
        this.modelBlock = modelBlock;
        if (modelBlock instanceof MappetBlockBBModel)
        {
            this.settings = ((MappetBlockBBModel) modelBlock).settings;
        }
        else if (modelBlock instanceof MappetBlockConditionModel)
        {
            this.settings = ((MappetBlockConditionModel) modelBlock).settings;
        }
        else
        {
            throw new IllegalArgumentException("MappetModelSettings can only be used with MappetBlockBBModel or TileConditionModel");
        }
    }

    @Override
    public MappetModelSettings setTranslate(double x, double y, double z)
    {
        this.settings.setX((float) x);
        this.settings.setY((float) y);
        this.settings.setZ((float) z);
        return this;
    }

    @Override
    public MappetModelSettings setScale(double x, double y, double z)
    {
        this.settings.setSx((float) x);
        this.settings.setSy((float) y);
        this.settings.setSz((float) z);
        return this;
    }

    @Override
    public MappetModelSettings setScale(double xyz)
    {
        return this.setScale(xyz, xyz, xyz);
    }

    @Override
    public MappetModelSettings setRotate(double x, double y, double z)
    {
        this.settings.setRotateBody((float) x);
        this.settings.setRotatePitch((float) y);
        this.settings.setRotateYawHead((float) z);
        return this;
    }

    @Override
    public MappetModelSettings setRotateAxis(double x, double y, double z)
    {
        this.settings.setRx((float) x);
        this.settings.setRy((float) y);
        this.settings.setRz((float) z);
        return this;
    }

    @Override
    public MappetModelSettings setEnabled(boolean enabled)
    {
        this.settings.setEnabled(enabled);
        return this;
    }

    @Override
    public MappetModelSettings setGlobalEnabled(boolean enabled) {
        this.settings.setGlobal(enabled);
        return this;
    }

    @Override
    public MappetModelSettings setShadowEnabled(boolean enabled)
    {
        this.settings.setShadow(enabled);
        return this;
    }

    @Override
    public MappetModelSettings setBlockHitboxEnabled(boolean enabled)
    {
        this.settings.setEnableBlockHitbox(enabled);
        return this;
    }

    @Override
    public MappetModelSettings setRenderAlwaysEnabled(boolean enabled)
    {
        this.settings.setRenderAlways(enabled);
        return this;
    }

    @Override
    public MappetModelSettings setRenderLastEnabled(boolean enabled)
    {
        this.settings.setRenderLast(enabled);
        return this;
    }

    @Override
    public MappetModelSettings setLightValue(int value)
    {
        this.settings.setLightValue(value);
        return this;
    }

    @Override
    public MappetModelSettings setSlot(ScriptItemStack item, int slot)
    {
        this.settings.setSlot(item.getMinecraftItemStack(), slot);
        return this;
    }

    @Override
    public MappetModelSettings set(MappetModelSettings source)
    {
        this.settings.copy(source.settings);
        return this;
    }
}