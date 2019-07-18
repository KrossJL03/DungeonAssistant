package bot.Encounter.Logger.Message.Action;

import bot.Encounter.Logger.Message.MLCodeBlockFormatter;
import org.jetbrains.annotations.NotNull;

abstract class ActionMessageFactory
{
    protected MLCodeBlockFormatter codeFormatter;

    /**
     * AttackActionMessageFactory constructor
     */
    @NotNull ActionMessageFactory()
    {
        this.codeFormatter = new MLCodeBlockFormatter();
    }
}
