package mchorse.mappet.client.gui.scripts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mchorse.mappet.client.gui.scripts.utils.documentation.DocClass;
import mchorse.mappet.client.gui.scripts.utils.documentation.DocEntry;
import mchorse.mappet.client.gui.scripts.utils.documentation.DocMethod;
import mchorse.mappet.client.gui.scripts.utils.documentation.DocPackage;
import mchorse.mappet.client.gui.scripts.utils.documentation.Docs;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlayPanel;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiListElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class GuiDocumentationOverlayPanel extends GuiOverlayPanel
{
    public GuiDocEntryList list;
    public GuiScrollElement documentation;

    private Map<String, DocClass> data = new HashMap<String, DocClass>();
    private DocPackage topPackage;

    private DocClass currentClass;
    private DocEntry entry;

    public GuiDocumentationOverlayPanel(Minecraft mc)
    {
        super(mc, IKey.str("Scripting documentation"));

        this.list = new GuiDocEntryList(mc, (l) -> this.pick(l.get(0)));
        this.documentation = new GuiScrollElement(mc);

        this.list.flex().relative(this.content).w(120).h(1F);
        this.documentation.flex().relative(this.content).x(120).w(1F, -120).h(1F).column(4).vertical().stretch().scroll().padding(10);

        this.content.add(this.list, this.documentation);

        this.parseDocumentation();
    }

    private void pick(DocEntry entry)
    {
        if (entry instanceof DocPackage)
        {
            this.currentClass = null;
            this.fill(entry);
            this.fillList();
        }
        else if (entry instanceof DocClass)
        {
            this.currentClass = (DocClass) entry;
            this.fill(entry);
            this.fillList();
        }
        else
        {
            this.fill(entry);
        }
    }

    private void fillList()
    {
        this.list.clear();

        if (this.entry == this.topPackage)
        {
            for (DocClass docClass : this.data.values())
            {
                this.list.add(docClass);
            }
        }
        else if (this.currentClass != null)
        {
            this.list.add(this.topPackage);

            for (DocMethod docMethod : this.currentClass.methods)
            {
                this.list.add(docMethod);
            }
        }

        this.list.sort();
    }

    private void fill(DocEntry entry)
    {
        if (this.entry == entry)
        {
            return;
        }

        this.entry = entry;

        this.documentation.scroll.scrollTo(0);
        this.documentation.removeAll();
        this.entry.fillIn(this.mc, this.documentation);

        this.resize();
    }

    private void parseDocumentation()
    {
        InputStream docs = this.getClass().getResourceAsStream("/assets/mappet/docs.json");
        Gson gson = new GsonBuilder().create();
        Scanner scanner = new Scanner(docs, "UTF-8");

        Docs data = gson.fromJson(scanner.useDelimiter("\\A").next(), Docs.class);

        for (DocClass docClass : data.classes)
        {
            this.data.put(docClass.getName(), docClass);
        }

        for (DocPackage docPackage : data.packages)
        {
            if (!docPackage.doc.isEmpty())
            {
                this.topPackage = docPackage;

                break;
            }
        }

        this.fill(this.topPackage);
        this.fillList();
    }

    public static class GuiDocEntryList extends GuiListElement<DocEntry>
    {
        public GuiDocEntryList(Minecraft mc, Consumer<List<DocEntry>> callback)
        {
            super(mc, callback);

            this.scroll.scrollItemSize = 16;
        }

        @Override
        protected boolean sortElements()
        {
            this.list.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));

            return true;
        }

        @Override
        protected String elementToString(DocEntry element)
        {
            return element.getName();
        }
    }
}