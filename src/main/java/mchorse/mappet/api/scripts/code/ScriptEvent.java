package mchorse.mappet.api.scripts.code;

import mchorse.mappet.api.scripts.user.IScriptBlockState;
import mchorse.mappet.api.scripts.user.IScriptEntity;
import mchorse.mappet.api.scripts.user.IScriptEvent;
import mchorse.mappet.api.scripts.user.IScriptWorld;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Map;

public class ScriptEvent implements IScriptEvent
{
    private DataContext context;

    private IScriptEntity subject;
    private IScriptEntity object;

    public ScriptEvent(DataContext context)
    {
        this.context = context;
    }

    @Override
    public IScriptEntity getSubject()
    {
        if (this.subject == null && this.context.subject != null)
        {
            this.subject = new ScriptEntity(this.context.subject);
        }

        return this.subject;
    }

    @Override
    public IScriptEntity getObject()
    {
        if (this.object == null && this.context.object != null)
        {
            this.object = new ScriptEntity(this.context.object);
        }

        return this.object;
    }

    @Override
    public IScriptWorld getWorld()
    {
        return new ScriptWorld(this.context.world);
    }

    @Override
    public Map<String, Object> getValues()
    {
        return this.context.getValues();
    }

    /* Factory stuff */

    @Override
    public IScriptBlockState getBlockState(String blockId, int meta)
    {
        ResourceLocation location = new ResourceLocation(blockId);
        Block block = ForgeRegistries.BLOCKS.getValue(location);

        if (block != null)
        {
            IBlockState state = block.getStateFromMeta(meta);

            return new ScriptBlockState(state);
        }

        return null;
    }

    /* Useful methods */

    @Override
    public void executeCommand(String command)
    {
        this.context.execute(command);
    }

    @Override
    public void sendMessage(String message)
    {
        for (EntityPlayer player : this.context.server.getPlayerList().getPlayers())
        {
            player.sendMessage(new TextComponentString(message));
        }
    }

    @Override
    public boolean sendMessageTo(IScriptEntity entity, String message)
    {
        if (entity.isPlayer())
        {
            ((ScriptEntity) entity).getEntity().sendMessage(new TextComponentString(message));
        }

        return false;
    }
}