package mchorse.mappet.api.utils.logs;

import java.util.logging.Level;

public enum LoggerLevel
{
    ERROR(new MappetLoggerLevel("ERROR", 999), 0xff5555),

    WARNING(Level.WARNING, 0xffaa00),

    INFO(Level.INFO, 0xffffff),
    DEBUG(new MappetLoggerLevel("DEBUG", 699), 0xaaaaaa);

    public final Level value;
    public final int color;

    LoggerLevel(Level level, int color)
    {
        this.value = level;
        this.color = color;
    }
}

class MappetLoggerLevel extends Level
{
    protected MappetLoggerLevel(String name, int value)
    {
        super(name, value);
    }
}
