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
import net.minecraft.launchwrapper.Launch;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class GuiDocumentationOverlayPanel extends GuiOverlayPanel
{
    private static Docs docs;
    private static DocClass currentClass;
    private static DocEntry entry;

    public GuiDocEntryList list;
    public GuiScrollElement documentation;

    private Map<String, DocClass> data = new HashMap<String, DocClass>();
    private DocPackage topPackage;

    public GuiDocumentationOverlayPanel(Minecraft mc)
    {
        super(mc, IKey.lang("mappet.gui.scripts.documentation.title"));

        this.list = new GuiDocEntryList(mc, (l) -> this.pick(l.get(0)));
        this.documentation = new GuiScrollElement(mc);

        this.list.flex().relative(this.content).w(120).h(1F);
        this.documentation.flex().relative(this.content).x(120).w(1F, -120).h(1F).column(4).vertical().stretch().scroll().padding(10);

        this.content.add(this.list, this.documentation);

        this.parseDocumentation();
    }

    private void pick(DocEntry entryIn)
    {
        if (entry == entryIn)
        {
            return;
        }

        if (entryIn instanceof DocPackage)
        {
            currentClass = null;
            this.fill(entryIn);
            this.fillList();
        }
        else if (entryIn instanceof DocClass)
        {
            currentClass = (DocClass) entryIn;
            this.fill(entryIn);
            this.fillList();
        }
        else
        {
            this.fill(entryIn);
        }
    }

    private void fillList()
    {
        this.list.clear();

        if (entry == this.topPackage)
        {
            for (DocClass docClass : this.data.values())
            {
                this.list.add(docClass);
            }
        }
        else if (currentClass != null)
        {
            this.list.add(this.topPackage);

            for (DocMethod docMethod : this.currentClass.methods)
            {
                this.list.add(docMethod);
            }
        }

        this.list.sort();
    }

    private void fill(DocEntry entryIn)
    {
        entry = entryIn;

        this.documentation.scroll.scrollTo(0);
        this.documentation.removeAll();
        entry.fillIn(this.mc, this.documentation);

        this.resize();
    }

    private void parseDocumentation()
    {
        /* Update the docs data only if it's in dev environment */
        final boolean dev = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

        if (dev || docs == null)
        {
            InputStream stream = this.getClass().getResourceAsStream("/assets/mappet/docs.json");
            Gson gson = new GsonBuilder().create();
            Scanner scanner = new Scanner(stream, "UTF-8");

            docs = gson.fromJson(scanner.useDelimiter("\\A").next(), Docs.class);

            entry = null;
        }

        for (DocClass docClass : docs.classes)
        {
            docClass.removeDisabledMethods();

            this.data.put(docClass.getName(), docClass);
        }

        for (DocPackage docPackage : docs.packages)
        {
            if (!docPackage.doc.isEmpty())
            {
                this.topPackage = docPackage;

                break;
            }
        }

        if (entry == null)
        {
            currentClass = null;
            entry = this.topPackage;
        }

        this.fill(entry);
        this.fillList();

        if (entry instanceof DocMethod)
        {
            this.list.setCurrentScroll(entry);
        }
    }

    public static class GuiDocEntryList extends GuiListElement<DocEntry>
    {
        public GuiDocEntryList(Minecraft mc, Consumer<List<DocEntry>> callback)
        {
            super(mc, callback);

            this.scroll.scrollItemSize = 16;
            this.scroll.scrollSpeed *= 2;
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