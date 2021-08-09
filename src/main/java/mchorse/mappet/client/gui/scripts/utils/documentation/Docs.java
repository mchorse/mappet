package mchorse.mappet.client.gui.scripts.utils.documentation;

import java.util.ArrayList;
import java.util.List;

public class Docs
{
    public List<DocClass> classes = new ArrayList<DocClass>();
    public List<DocPackage> packages = new ArrayList<DocPackage>();

    public DocPackage getPackage(String name)
    {
        for (DocPackage docPackage : this.packages)
        {
            if (docPackage.name.equals(name))
            {
                return docPackage;
            }
        }

        return null;
    }
}