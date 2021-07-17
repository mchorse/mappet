package mchorse.mappet.client.gui.scripts.utils.documentation;

public class DocParameter extends DocEntry
{
    private String type = "";

    public String getType()
    {
        int index = this.type.lastIndexOf(".");

        if (index < 0)
        {
            return this.type;
        }

        return this.type.substring(index + 1);
    }
}