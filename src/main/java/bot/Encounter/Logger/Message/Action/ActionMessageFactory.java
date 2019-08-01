package bot.Encounter.Logger.Message.Action;

import bot.Encounter.Logger.Message.MLCodeFormatter;
import org.jetbrains.annotations.NotNull;

abstract class ActionMessageFactory
{
    protected MLCodeFormatter codeFormatter;

    /**
     * ActionMessageFactory constructor.
     */
    @NotNull ActionMessageFactory()
    {
        this.codeFormatter = new MLCodeFormatter();
    }
}
