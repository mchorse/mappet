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
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class GuiDocumentationOverlayPanel extends GuiOverlayPanel
{
    private static Docs docs;
    private static DocEntry entry;

    public GuiDocEntryList list;
    public GuiScrollElement documentation;

    private DocEntry topPackage;

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
        entryIn = entryIn.getEntry();

        List<DocEntry> entries = entryIn.getEntries();

        if (!entries.isEmpty() || entryIn.parent != null)
        {
            this.list.clear();

            if (entryIn.parent != null)
            {
                this.list.add(new DocDelegate(entryIn.parent));
            }

            this.list.add(entries);
            this.list.sort();
        }

        this.fill(entryIn);
    }

    private void fill(DocEntry entryIn)
    {
        if (!(entry instanceof DocMethod))
        {
            entry = entryIn;
        }

        this.documentation.scroll.scrollTo(0);
        this.documentation.removeAll();
        entryIn.fillIn(this.mc, this.documentation);

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

            DocList top = new DocList();
            DocList scripting = new DocList();
            DocList ui = new DocList();

            top.doc = docs.getPackage("mchorse.mappet.api.scripts.user.mappet").doc;
            scripting.name = "Scripting API";
            scripting.doc = docs.getPackage("mchorse.mappet.api.scripts.user").doc;
            scripting.parent = top;
            ui.name = "UI API";
            ui.doc = docs.getPackage("mchorse.mappet.api.ui.components").doc;
            ui.parent = top;

            for (DocClass docClass : docs.classes)
            {
                docClass.removeDisabledMethods();

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

            top.entries.add(scripting);
            top.entries.add(ui);
            this.topPackage = top;
        }

        if (entry == null)
        {
            entry = this.topPackage;
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