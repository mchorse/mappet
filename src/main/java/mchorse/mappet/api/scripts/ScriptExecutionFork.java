package mchorse.mappet.api.scripts;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.scripts.code.ScriptEvent;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.IExecutable;

public class ScriptExecutionFork implements IExecutable
{
    public DataContext context;
    public ScriptObjectMirror object;
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

    public ScriptExecutionFork(DataContext context, ScriptObjectMirror object, int timer)
    {
        this.context = context;
        this.object = object;
        this.timer = timer;
    }

    @Override
    public boolean update()
    {
        if (this.timer <= 0)
        {
            try
            {
                if (this.object != null)
                {
                    this.object.call(null, new ScriptEvent(this.context, null, null));
                }
                else
                {
                    Mappet.scripts.execute(this.script, this.function, this.context);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return true;
        }

        this.timer -= 1;

        return false;
    }
}