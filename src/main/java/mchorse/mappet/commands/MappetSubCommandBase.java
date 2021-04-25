package mchorse.mappet.commands;

import mchorse.mappet.Mappet;
import mchorse.mclib.commands.McCommandBase;
import mchorse.mclib.commands.SubCommandBase;
import mchorse.mclib.commands.utils.L10n;

public abstract class MappetSubCommandBase extends SubCommandBase
{
    @Override
    public L10n getL10n()
    {
        return Mappet.l10n;
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}" + this.getName() + "{r} {7}...{r}";
    }
}
