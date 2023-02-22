package mchorse.mappet.client.gui.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import mchorse.mappet.api.utils.logs.LoggerLevel;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiListElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiLoggingLevelList extends GuiListElement<Boolean>
{
    public Map<LoggerLevel, Boolean> flags;

    public GuiLoggingLevelList(Minecraft mc, Consumer<List<Boolean>> callback)
    {
        super(mc, callback);
        this.scroll.scrollItemSize = 16;

        this.flags = new HashMap<>();

        this.addLevel(LoggerLevel.ERROR);
        this.addLevel(LoggerLevel.WARNING);
        this.addLevel(LoggerLevel.INFO);
        this.addLevel(LoggerLevel.DEBUG);
    }

    public void addLevel(LoggerLevel level)
    {
        this.flags.put(level, true);

        GuiToggleElement toggleElement = new GuiToggleElement(this.mc, IKey.str(level.value.getName()), true, null);
        toggleElement.callback = (b) ->
        {
            this.flags.put(level, b.isToggled());

            this.callback.accept(Collections.EMPTY_LIST);
        };

        this.add(toggleElement);
    }
}
