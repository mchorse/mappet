package mchorse.mappet.client.gui.scripts.utils.documentation;

import java.util.ArrayList;
import java.util.List;

public class DocClass extends DocEntry
{
    public List<DocMethod> methods = new ArrayList<DocMethod>();

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

    public void removeDisabledMethods()
    {
        this.methods.removeIf(method -> method.annotations.contains("mchorse.mappet.api.ui.utils.DiscardMethod"));
    }
}