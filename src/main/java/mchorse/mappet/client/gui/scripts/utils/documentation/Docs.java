package mchorse.mappet.client.gui.scripts.utils.documentation;

import java.util.ArrayList;
import java.util.List;

public class Docs
{
    public List<DocClass> classes = new ArrayList<DocClass>();
    public List<DocPackage> packages = new ArrayList<DocPackage>();
    public String source = "Mappet";

    public DocClass getClass(String name)
    {
        for (DocClass docClass : this.classes)
        {
            if (docClass.name.endsWith(name))
            {
                return docClass;
            }
        }

        return null;
    }

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

    public void remove(String name)
    {
        this.classes.removeIf(clazz -> clazz.name.endsWith(name));
    }

    public void copyMethods(String from, String... to)
    {
        DocClass source = this.getClass(from);

        for (String string : to)
        {
            DocClass target = this.getClass(string);

            target.methods.addAll(source.methods);
        }
    }
}