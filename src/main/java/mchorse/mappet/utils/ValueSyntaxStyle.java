package mchorse.mappet.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import mchorse.mappet.client.gui.scripts.themes.GuiThemeEditorOverlayPanel;
import mchorse.mappet.client.gui.scripts.themes.Themes;
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
        GuiButtonElement button = new GuiButtonElement(mc, IKey.lang("mappet.gui.syntax_theme.edit"), (t) ->
        {
            GuiOverlay.addOverlay(GuiBase.getCurrent(), new GuiThemeEditorOverlayPanel(mc), 0.6F, 0.95F);
        });

        return Arrays.asList(button.tooltip(IKey.lang(this.getCommentKey())));
    }

    @Override
    public void valueFromJSON(JsonElement element)
    {
        String file = element.getAsString();
        SyntaxStyle style = Themes.readTheme(Themes.themeFile(file));

        if (style != null)
        {
            this.style = style;
            this.file = file;
        }
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