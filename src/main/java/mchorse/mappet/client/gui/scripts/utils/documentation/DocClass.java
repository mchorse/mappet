package mchorse.mappet.client.gui.scripts.utils.documentation;

import java.util.ArrayList;
import java.util.List;

public class DocClass extends DocEntry
{
    public List<DocMethod> methods = new ArrayList<DocMethod>();

    @Override
    public List<DocEntry> getEntries()
    {
        List<DocEntry> entries = new ArrayList<DocEntry>();

        entries.addAll(this.methods);

        return entries;
    }

    public DocMethod getMethod(String name)
    {
        for (DocMethod method : this.methods)
        {
            if (method.name.equals(name))
            {
                return method;
            }
        }

        return null;
    }

    public DocMethod getExactMethod(String name) {
        for (DocMethod method : this.methods) {
            if (method.getName().equals(name)) {
                return method;
            }
        }

        return null;
    }

    public void setup()
    {
        this.methods.removeIf(method -> method.annotations.contains("mchorse.mappet.api.ui.utils.DiscardMethod"));

        for (DocMethod method : this.methods)
        {
            method.parent = this;
        }
    }
}