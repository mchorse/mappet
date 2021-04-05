package mchorse.mappet.commands;

import mchorse.mappet.Mappet;
import mchorse.mclib.commands.McCommandBase;
import mchorse.mclib.commands.utils.L10n;

public abstract class MappetCommandBase extends McCommandBase
{
    @Override
    public L10n getL10n()
    {
        return Mappet.l10n;
    }
}
