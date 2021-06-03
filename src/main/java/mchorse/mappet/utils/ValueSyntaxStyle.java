package mchorse.mappet.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import mchorse.mappet.ClientProxy;
import mchorse.mappet.client.gui.scripts.themes.GuiThemeEditorOverlayPanel;
import mchorse.mappet.client.gui.scripts.utils.SyntaxStyle;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.config.gui.GuiConfigPanel;
import mchorse.mclib.config.values.IConfigGuiProvider;
import mchorse.mclib.config.values.Value;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

public class ValueSyntaxStyle extends Value implements IConfigGuiProvider
{
    private SyntaxStyle style = new SyntaxStyle();
    private String file = "monokai.json";

    public ValueSyntaxStyle(String id)
    {
        super(id);

        this.clientSide();
    }

    public SyntaxStyle get()
    {
        return this.style;
    }

    public String getFile()
    {
        return this.file;
    }

    public void set(String file, SyntaxStyle style)
    {
        this.file = file;
        this.style = new SyntaxStyle(style.toNBT());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<GuiElement> getFields(Minecraft mc, GuiConfigPanel config)
    {
        GuiElement element = new GuiElement(mc);
        GuiLabel label = Elements.label(IKey.lang(this.getLabelKey()), 0).anchor(0, 0.5F);
        GuiButtonElement button = new GuiButtonElement(mc, IKey.lang("Edit theme..."), (t) ->
        {
            GuiOverlay.addOverlay(GuiBase.getCurrent(), new GuiThemeEditorOverlayPanel(mc), 0.6F, 0.95F);
        });

        button.flex().w(90);

        element.flex().row(0).preferred(0).height(20);
        element.add(label, button.removeTooltip());

        return Arrays.asList(element.tooltip(IKey.lang(this.getCommentKey())));
    }

    @Override
    public void valueFromJSON(JsonElement element)
    {
        try
        {
            String file = element.getAsString();

            if (!file.endsWith(".json"))
            {
                file += ".json";
            }

            File syntaxFile = new File(ClientProxy.editorThemes, file);

            this.style = new SyntaxStyle(NBTToJsonLike.fromJson(FileUtils.readFileToString(syntaxFile, Charset.defaultCharset())));
            this.file = file;
        }
        catch (Exception e)
        {}
    }

    @Override
    public JsonElement valueToJSON()
    {
        return new JsonPrimitive(this.file);
    }

    @Override
    public void copy(Value value)
    {
        if (value instanceof ValueSyntaxStyle)
        {
            ValueSyntaxStyle config = (ValueSyntaxStyle) value;

            this.file = config.file;
            this.style = new SyntaxStyle(config.style.toNBT());
        }
    }
}