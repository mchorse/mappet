package mchorse.mappet.api.scripts;

import com.caoccao.javet.values.reference.V8ValueFunction;
import com.caoccao.javet.values.reference.V8ValueObject;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.scripts.code.ScriptEvent;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.IExecutable;

public class ScriptExecutionFork implements IExecutable
{
    public DataContext context;
    public V8ValueObject object;
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

    @SuppressWarnings("unused")
    public ScriptExecutionFork(DataContext context, V8ValueObject object, int timer)
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
//                    this.object.call(null, new ScriptEvent(this.context, null, null));
                    ((V8ValueFunction) object).call(null, new ScriptEvent(this.context, null, null));
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