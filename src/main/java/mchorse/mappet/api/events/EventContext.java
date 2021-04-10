package mchorse.mappet.api.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.StringUtils;

public class EventContext
{
    public MinecraftServer server;
    public EntityPlayer player;

    public boolean debug;
    public StringBuilder log = new StringBuilder();
    public int nesting = 0;

    public EventContext(MinecraftServer server, EntityPlayer player, boolean debug)
    {
        this.server = server;
        this.player = player;
        this.debug = debug;
    }

    public void log(String message)
    {
        if (this.debug)
        {
            if (this.nesting > 0)
            {
                message = StringUtils.repeat("-", this.nesting) + " " + message;
            }

            this.log.append(message + "\n");
        }
    }
}