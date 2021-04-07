package mchorse.mappet.api.crafting;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

public class CraftingTable implements INBTSerializable<NBTTagCompound>
{
    public List<CraftingRecipe> recipes = new ArrayList<CraftingRecipe>();

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList recipes = new NBTTagList();

        for (CraftingRecipe recipe : this.recipes)
        {
            NBTTagCompound recipeTag = recipe.serializeNBT();

            if (recipeTag.getSize() > 0)
            {
                recipes.appendTag(recipeTag);
            }
        }

        if (recipes.tagCount() > 0)
        {
            tag.setTag("Recipes", recipes);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Recipes"))
        {
            NBTTagList recipes = tag.getTagList("Recipes", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < recipes.tagCount(); i++)
            {
                CraftingRecipe recipe = new CraftingRecipe();

                recipe.deserializeNBT(recipes.getCompoundTagAt(i));
                this.recipes.add(recipe);
            }
        }
    }
}