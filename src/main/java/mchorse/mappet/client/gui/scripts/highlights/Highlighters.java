package mchorse.mappet.client.gui.scripts.highlights;

import com.google.common.collect.ImmutableSet;
import mchorse.mappet.ClientProxy;
import mchorse.mappet.client.gui.scripts.utils.SyntaxHighlighter;
import mchorse.mappet.utils.NBTToJsonLike;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Highlighters
{
    private static File editorHighlighters;

    private static SyntaxHighlighter defaultHighlighter;

    public static List<File> highlighters()
    {
        List<File> highlighters = new ArrayList<File>();
        File[] files = editorHighlighters.listFiles();

        if (files != null)
        {
            for (File file : files)
            {
                if (file.isFile() && file.getName().endsWith(".json"))
                {
                    highlighters.add(file);
                }
            }
        }

        return highlighters;
    }

    public static File highlighterFile(String name)
    {
        if (!name.endsWith(".json"))
        {
            name += ".json";
        }

        return new File(editorHighlighters, name);
    }

    public static SyntaxHighlighter readHighlighter(File file)
    {
        try
        {
            return new SyntaxHighlighter(NBTToJsonLike.read(file));
        }
        catch (Exception e)
        {}

        return defaultHighlighter;
    }

    public static void writeHighlighter(File file, SyntaxHighlighter highlighter)
    {
        try
        {
            NBTToJsonLike.write(file, highlighter.toNBT());
        }
        catch (Exception e)
        {}
    }

    public static void initiate()
    {
        if (editorHighlighters != null)
        {
            return;
        }

        editorHighlighters = new File(ClientProxy.configFolder, "highlights");
        editorHighlighters.mkdirs();

        File js = new File(editorHighlighters, "js.json");
        File kts = new File(editorHighlighters, "kts.json");

        if (!js.isFile())
        {
            SyntaxHighlighter jsHighlighter = new SyntaxHighlighter();
            jsHighlighter.operators = ImmutableSet.of("+", "-", "=", "/", "*", "<", ">", "~", "&", "|", "!");
            jsHighlighter.primaryKeywords = ImmutableSet.of(
                    "break", "continue", "switch", "case", "default", "try",
                    "catch", "delete", "do", "while", "else", "finally", "if",
                    "else", "for", "each", "in", "instanceof",
                    "new", "throw", "typeof", "with", "yield", "return"
            );
            jsHighlighter.secondaryKeywords = ImmutableSet.of("const", "function", "var", "let", "prototype", "Math", "JSON", "mappet");
            jsHighlighter.special = ImmutableSet.of("this", "arguments");
            jsHighlighter.typeKeywords = ImmutableSet.of("true", "false", "null", "undefined");
            jsHighlighter.functionName = Pattern.compile("[\\w_][\\d\\w_]*", Pattern.CASE_INSENSITIVE);

            writeHighlighter(js, jsHighlighter);
            defaultHighlighter = jsHighlighter;
        }

        if (!kts.isFile())
        {
            SyntaxHighlighter ktsHighlighter = new SyntaxHighlighter();
            ktsHighlighter.operators = ImmutableSet.of("+", "-", "=", "/", "*", "<", ">", "~", "&", "|", "!");
            ktsHighlighter.primaryKeywords = ImmutableSet.of(
                    "break", "continue", "switch", "case", "default", "try",
                    "catch", "delete", "do", "while", "else", "finally", "if",
                    "else", "for", "each", "in", "instanceof",
                    "new", "throw", "typeof", "with", "yield", "return"
            );
            ktsHighlighter.secondaryKeywords = ImmutableSet.of("const", "function", "var", "let", "prototype", "Math", "JSON", "mappet");
            ktsHighlighter.special = ImmutableSet.of("this", "arguments");
            ktsHighlighter.typeKeywords = ImmutableSet.of("true", "false", "null", "undefined");
            ktsHighlighter.functionName = Pattern.compile("[\\w_][\\d\\w_]*", Pattern.CASE_INSENSITIVE);

            writeHighlighter(kts, ktsHighlighter);
        }
    }
}
