package mchorse.mappet.api.scripts.code;

import mchorse.mappet.api.scripts.code.items.ScriptItemStack;
import mchorse.mappet.api.scripts.code.nbt.ScriptNBTCompound;
import mchorse.mappet.api.scripts.code.nbt.ScriptNBTList;
import mchorse.mappet.api.scripts.user.IScriptBlockState;
import mchorse.mappet.api.scripts.user.IScriptFactory;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.mappet.api.scripts.user.nbt.INBTList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ScriptFactory implements IScriptFactory
{
    @Override
    public IScriptBlockState blockState(String blockId, int meta)
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

    @Override
    public INBTCompound compound(String nbt)
    {
        NBTTagCompound tag = new NBTTagCompound();

        if (nbt != null)
        {
            try
            {
                tag = JsonToNBT.getTagFromJson(nbt);
            }
            catch (Exception e)
            {}
        }

        return new ScriptNBTCompound(tag);
    }

    @Override
    public INBTList list(String nbt)
    {
        NBTTagList list = new NBTTagList();

        if (nbt != null)
        {
            try
            {
                list = (NBTTagList) JsonToNBT.getTagFromJson("{List:" + nbt + "}").getTag("List");
            }
            catch (Exception e)
            {}
        }

        return new ScriptNBTList(list);
    }

    @Override
    public IScriptItemStack itemStack(INBTCompound compound)
    {
        if (compound instanceof ScriptItemStack)
        {
            return new ScriptItemStack(new ItemStack(((ScriptNBTCompound) compound).getNBTCompound()));
        }

        return ScriptItemStack.EMPTY;
    }
}