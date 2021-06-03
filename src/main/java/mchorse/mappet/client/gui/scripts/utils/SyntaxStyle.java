package mchorse.mappet.client.gui.scripts.utils;

import net.minecraft.nbt.NBTTagCompound;

public class SyntaxStyle
{
    public String title;
    public boolean shadow;
    public int primary;
    public int secondary;
    public int identifier;
    public int special;
    public int strings;
    public int comments;
    public int numbers;
    public int other;
    public int lineNumbers;
    public int background;

    public SyntaxStyle()
    {
        this.title = "Monokai";
        this.shadow = true;
        this.primary = 0xf92472;
        this.secondary = 0x67d8ef;
        this.identifier = 0xa6e22b;
        this.special = 0xfd9622;
        this.strings = 0xe7db74;
        this.comments = 0x74705d;
        this.numbers = 0xac80ff;
        this.other = 0xffffff;
        this.lineNumbers = 0x90918b;
        this.background = 0x282923;
    }

    public SyntaxStyle(NBTTagCompound tag)
    {
        this.title = tag.getString("Title");
        this.shadow = tag.getBoolean("Shadow");
        this.primary = tag.getInteger("Primary");
        this.secondary = tag.getInteger("Secondary");
        this.identifier = tag.getInteger("Identifier");
        this.special = tag.getInteger("Special");
        this.strings = tag.getInteger("Strings");
        this.comments = tag.getInteger("Comments");
        this.numbers = tag.getInteger("Numbers");
        this.other = tag.getInteger("Other");
        this.lineNumbers = tag.getInteger("LineNumbers");
        this.background = tag.getInteger("Background");
    }

    public NBTTagCompound toNBT()
    {
        return this.toNBT(new NBTTagCompound());
    }

    public NBTTagCompound toNBT(NBTTagCompound tag)
    {
        tag.setString("Title", this.title);
        tag.setBoolean("Shadow", this.shadow);
        tag.setInteger("Primary", this.primary);
        tag.setInteger("Secondary", this.secondary);
        tag.setInteger("Identifier", this.identifier);
        tag.setInteger("Special", this.special);
        tag.setInteger("Strings", this.strings);
        tag.setInteger("Comments", this.comments);
        tag.setInteger("Numbers", this.numbers);
        tag.setInteger("Other", this.other);
        tag.setInteger("LineNumbers", this.lineNumbers);
        tag.setInteger("Background", this.background);

        return tag;
    }
}