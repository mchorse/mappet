package mchorse.mappet.client.gui.scripts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mchorse.mappet.client.gui.scripts.utils.documentation.DocClass;
import mchorse.mappet.client.gui.scripts.utils.documentation.DocDelegate;
import mchorse.mappet.client.gui.scripts.utils.documentation.DocEntry;
import mchorse.mappet.client.gui.scripts.utils.documentation.DocList;
import mchorse.mappet.client.gui.scripts.utils.documentation.DocMethod;
import mchorse.mappet.client.gui.scripts.utils.documentation.Docs;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlayPanel;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiListElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class GuiDocumentationOverlayPanel extends GuiOverlayPanel
{
    private static Docs docs;
    private static DocEntry top;
    private static DocEntry entry;

    public GuiDocEntryList list;
    public GuiScrollElement documentation;

    public static List<DocClass> search(String text)
    {
        List<DocClass> list = new ArrayList<DocClass>();

        for (DocClass docClass : getDocs().classes)
        {
            if (docClass.getMethod(text) != null)
            {
                list.add(docClass);
            }
        }

        return list;
    }

    public static Docs getDocs()
    {
        parseDocs();

        return docs;
    }

    private static void parseDocs()
    {
        /* Update the docs data only if it's in dev environment */
        final boolean dev = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

        if (dev || docs == null)
        {
            InputStream stream = GuiDocumentationOverlayPanel.class.getResourceAsStream("/assets/mappet/docs.json");
            Gson gson = new GsonBuilder().create();
            Scanner scanner = new Scanner(stream, "UTF-8");

            docs = gson.fromJson(scanner.useDelimiter("\\A").next(), Docs.class);
            entry = null;

            docs.copyMethods("UILabelBaseComponent", "UIButtonComponent", "UILabelComponent", "UITextComponent", "UITextareaComponent", "UITextboxComponent", "UIToggleComponent");
            docs.remove("UIParentComponent");
            docs.remove("UILabelBaseComponent");

            DocList topPackage = new DocList();
            DocList scripting = new DocList();
            DocList ui = new DocList();

            topPackage.doc = docs.getPackage("mchorse.mappet.api.scripts.user.mappet").doc;
            scripting.name = "Scripting API";
            scripting.doc = docs.getPackage("mchorse.mappet.api.scripts.user").doc;
            scripting.parent = topPackage;
            ui.name = "UI API";
            ui.doc = docs.getPackage("mchorse.mappet.api.ui.components").doc;
            ui.parent = topPackage;

            for (DocClass docClass : docs.classes)
            {
                docClass.setup();

                if (docClass.name.contains("ui.components"))
                {
                    ui.entries.add(docClass);
                    docClass.parent = ui;
                }
                else
                {
                    scripting.entries.add(docClass);
                    docClass.parent = scripting;
                }
            }

            topPackage.entries.add(scripting);
            topPackage.entries.add(ui);

            top = topPackage;
        }
    }

    public GuiDocumentationOverlayPanel(Minecraft mc)
    {
        this(mc, null);
    }

    public GuiDocumentationOverlayPanel(Minecraft mc, DocEntry entry)
    {
        super(mc, IKey.lang("mappet.gui.scripts.documentation.title"));

        this.list = new GuiDocEntryList(mc, (l) -> this.pick(l.get(0)));
        this.documentation = new GuiScrollElement(mc);

        this.list.flex().relative(this.content).w(120).h(1F);
        this.documentation.flex().relative(this.content).x(120).w(1F, -120).h(1F).column(4).vertical().stretch().scroll().padding(10);

        this.content.add(this.list, this.documentation);

        this.setupDocs(entry);
    }

    private void pick(DocEntry entryIn)
    {
        boolean isDelegate = entryIn instanceof DocDelegate;
        boolean isMethod = entryIn instanceof DocMethod;

        entryIn = entryIn.getEntry();

        if (entry == entryIn || isDelegate || isMethod)
        {
            DocEntry listEntry = isMethod ? ((DocMethod) entryIn).parentClass : entryIn;
            List<DocEntry> entries = listEntry.getEntries();

            if (!entries.isEmpty() || listEntry.parent != null)
            {
                this.list.clear();

                if (listEntry.parent != null)
                {
                    this.list.add(new DocDelegate(listEntry.parent));
                }

                this.list.add(entries);
                this.list.sort();

                if (isMethod)
                {
                    this.list.setCurrentScroll(entryIn);
                }
            }
        }

        this.fill(entryIn);
    }

    private void fill(DocEntry entryIn)
    {
        if (!(entryIn instanceof DocMethod))
        {
            entry = entryIn;
        }

        this.documentation.scroll.scrollTo(0);
        this.documentation.removeAll();
        entryIn.fillIn(this.mc, this.documentation);

        this.resize();
    }

    private void setupDocs(DocEntry in)
    {
        parseDocs();

        if (in != null)
        {
            entry = in;
        }
        else if (entry == null)
        {
            entry = top;
        }

        this.pick(entry);
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