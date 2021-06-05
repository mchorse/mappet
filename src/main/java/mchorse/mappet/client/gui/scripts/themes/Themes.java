package mchorse.mappet.client.gui.scripts.themes;

import mchorse.mappet.ClientProxy;
import mchorse.mappet.client.gui.scripts.utils.SyntaxStyle;
import mchorse.mappet.utils.NBTToJsonLike;
import mchorse.mclib.client.gui.utils.GuiUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Themes
{
    private static File editorThemes;

    /**
     * Open editor themes folder
     */
    public static void open()
    {
        GuiUtils.openWebLink(editorThemes.toURI());
    }

    /**
     * Get all theme files
     */
    public static List<File> themes()
    {
        List<File> themes = new ArrayList<File>();
        File[] files = editorThemes.listFiles();

        if (files != null)
        {
            for (File file : files)
            {
                if (file.isFile() && file.getName().endsWith(".json"))
                {
                    themes.add(file);
                }
            }
        }

        return themes;
    }

    /**
     * Get theme file
     */
    public static File themeFile(String name)
    {
        if (!name.endsWith(".json"))
        {
            name += ".json";
        }

        return new File(editorThemes, name);
    }

    /**
     * Read theme out of the file
     */
    public static SyntaxStyle readTheme(File file)
    {
        try
        {
            String code = FileUtils.readFileToString(file, Charset.defaultCharset());

            return new SyntaxStyle(NBTToJsonLike.fromJson(code));
        }
        catch (Exception e)
        {}

        return null;
    }

    /**
     * Write a them into a file
     */
    public static void writeTheme(File file, SyntaxStyle style)
    {
        try
        {
            FileUtils.writeStringToFile(file, NBTToJsonLike.toJson(style.toNBT()), Charset.defaultCharset());
        }
        catch (Exception e)
        {}
    }

    /**
     * Initiate themes system
     */
    public static void initiate()
    {
        /* Just in case */
        if (editorThemes != null)
        {
            return;
        }

        editorThemes = new File(ClientProxy.configFolder, "themes");
        editorThemes.mkdirs();

        File monokai = new File(editorThemes, "monokai.json");
        File dracula = new File(editorThemes, "dracula.json");

        if (!monokai.isFile())
        {
            writeTheme(monokai, new SyntaxStyle());
        }

        if (!dracula.isFile())
        {
            SyntaxStyle draculaStyle = new SyntaxStyle();

            draculaStyle.title = "Dracula";
            draculaStyle.shadow = true;
            draculaStyle.primary = 0xcc7832;
            draculaStyle.secondary = 0x9876aa;
            draculaStyle.identifier = 0xffc66d;
            draculaStyle.special = 0xcc7832;
            draculaStyle.strings = 0x619554;
            draculaStyle.comments = 0x808080;
            draculaStyle.numbers = 0x6694b8;
            draculaStyle.other = 0xa9b7c6;
            draculaStyle.lineNumbers = 0x5e6163;
            draculaStyle.background = 0x2b2b2b;

            writeTheme(dracula, draculaStyle);
        }
    }
}