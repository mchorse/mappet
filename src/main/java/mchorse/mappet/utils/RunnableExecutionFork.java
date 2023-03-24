package mchorse.mappet.utils;

import mchorse.mappet.api.utils.IExecutable;

/**
 * Runnable execution fork
 *
 * This class is used to execute a runnable after a certain amount of ticks.
 *
 * <pre>{@code
 * CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(60, () ->
 * {
 *     System.out.println("This was called 3 seconds ago!");
 * }));
 * }</pre>
 *
 * @author mchorse
 */
public class RunnableExecutionFork implements IExecutable
{
    public int timer;
    public Runnable runnable;

    public RunnableExecutionFork(int timer, Runnable runnable)
    {
        this.timer = timer;
        this.runnable = runnable;
    }

    @Override
    public String getId()
    {
        return "";
    }

    @Override
    public boolean update()
    {
        if (this.timer <= 0)
        {
            if (this.runnable != null)
            {
                this.runnable.run();
            }

            return true;
        }

        this.timer -= 1;

        return false;
    }
}