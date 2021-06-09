package mchorse.mappet.api.scripts;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.IExecutable;

public class ScriptExecutionFork implements IExecutable
{
    public DataContext context;
    public String script;
    public String function;
    public int timer;

    public ScriptExecutionFork(DataContext context, String script, String function, int timer)
    {
        this.context = context;
        this.script = script;
        this.function = function;
        this.timer = timer;
    }

    @Override
    public boolean update()
    {
        if (this.timer <= 0)
        {
            try
            {
                Mappet.scripts.execute(this.script, this.function, this.context);
            }
            catch (Exception e)
            {}

            return true;
        }

        this.timer -= 1;

        return false;
    }
}