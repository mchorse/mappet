package mchorse.mappet.client.gui.scripts.utils.documentation;

import joptsimple.internal.Strings;
import mchorse.mappet.Mappet;
import mchorse.mappet.client.gui.scripts.GuiTextEditor;
import mchorse.mappet.client.gui.utils.text.GuiText;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class DocEntry
{
    public DocEntry parent;

    public String name = "";
    public String doc = "";

    public static String processCode(String code)
    {
        List<String> strings = new ArrayList<String>(Arrays.asList(code.split("\n")));
        int first = 0;

        /* Find first non-empty string */
        for (String string : strings)
        {
            if (string.trim().isEmpty())
            {
                first += 1;
            }
            else
            {
                break;
            }
        }

        /* Once first string is found, find the first string's indentation*/
        String firstLine = strings.get(first);
        int indent = 0;

        for (int i = 0; i < firstLine.length(); i++)
        {
            if (firstLine.charAt(i) == ' ')
            {
                indent += 1;
            }
            else
            {
                break;
            }
        }

        /* Remove last string which should contain "}</pre>" */
        strings.remove(strings.size() - 1);

        /* Remove the first line's indentation from the rest of the code */
        if (indent > 0)
        {
            for (int i = 0; i < strings.size(); i++)
            {
                String string = strings.get(i);

                if (string.length() > indent)
                {
                    strings.set(i, string.substring(indent));
                }
            }
        }

        return Strings.join(strings, "\n").trim();
    }

    public static void process(String doc, Minecraft mc, GuiScrollElement target)
    {
        String[] splits = doc.split("\n{2,}");
        boolean parsing = false;
        String code = "";

        for (String line : splits)
        {
            if (line.trim().startsWith("<pre>{@code"))
            {
                parsing = true;
                line = line.trim().substring("<pre>{@code".length() + 1);
            }

            if (parsing)
            {
                code += "\n\n" + line;
            }
            else
            {
                boolean p = line.trim().startsWith("<p>");

                line = line.replaceAll("\n", "").trim();
                line = line.replaceAll("<b>", TextFormatting.BOLD.toString());
                line = line.replaceAll("<i>", TextFormatting.ITALIC.toString());
                line = line.replaceAll("<s>", TextFormatting.STRIKETHROUGH.toString());
                line = line.replaceAll("<code>", TextFormatting.GRAY.toString());
                line = line.replaceAll("<li> *", "\n- ");
                line = line.replaceAll("</(b|i|s|code|ul|li)>", TextFormatting.RESET.toString());
                line = line.replaceAll("</?(p|ul|li)>", "");
                line = line.replaceAll("\\{@link +[^}]+\\.([^}]+)}", TextFormatting.GOLD + "$1" + TextFormatting.RESET);
                line = line.replaceAll("\\{@link +([^}]*)#([^}]+)}", TextFormatting.GOLD + "$1" + TextFormatting.RESET + "." + TextFormatting.GRAY + "$2" + TextFormatting.RESET);
                line = line.replaceAll("\\{@link ([^}]+)}", TextFormatting.GOLD + "$1" + TextFormatting.RESET);
                line = line.replaceAll("&lt;", "<");
                line = line.replaceAll("&gt;", ">");
                line = line.replaceAll("&amp;", "&");

                GuiText text = new GuiText(mc).text(line.trim().replaceAll(" {2,}", " "));

                if (p)
                {
                    text.marginTop(12);
                }

                target.add(text);
            }

            if (line.trim().endsWith("}</pre>"))
            {
                GuiTextEditor editor = new GuiTextEditor(mc, null);
                String text = processCode(code).replaceAll("§", "\\\\u00A7");

                editor.setText(text);
                editor.background().flex().h(editor.getLines().size() * 12 + 20);
                editor.getHighlighter().setStyle(Mappet.scriptEditorSyntaxStyle.get());
                target.add(editor);

                parsing = false;
                code = "";
            }
        }
    }

    public String getName()
    {
        int index = this.name.lastIndexOf(".");

        if (index < 0)
        {
            return this.name;
        }

        return this.name.substring(index + 1);
    }

    public void fillIn(Minecraft mc, GuiScrollElement target)
    {
        process(this.doc, mc, target);
    }

    public List<DocEntry> getEntries()
    {
        return Collections.emptyList();
    }

    public DocEntry getEntry()
    {
        return this;
    }
}