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
}
