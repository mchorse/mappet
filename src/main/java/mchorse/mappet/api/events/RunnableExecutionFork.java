package mchorse.mappet.api.events;

import mchorse.mappet.api.utils.IExecutable;

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