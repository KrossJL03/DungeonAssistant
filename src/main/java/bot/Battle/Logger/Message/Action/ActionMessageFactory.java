package bot.Battle.Logger.Message.Action;

import bot.Battle.Logger.Message.MLCodeFormatter;
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
