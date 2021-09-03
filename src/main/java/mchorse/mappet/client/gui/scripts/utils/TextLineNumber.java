package mchorse.mappet.client.gui.scripts.utils;

public class TextLineNumber
{
    public String line;
    public int x;
    public int y;
    public boolean draw;

    public void set(String line, int x, int y)
    {
        this.line = line;
        this.x = x;
        this.y = y;
        this.draw = true;
    }
}