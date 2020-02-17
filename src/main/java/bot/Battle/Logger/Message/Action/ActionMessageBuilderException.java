package bot.Battle.Logger.Message.Action;

import bot.CustomException;

class ActionMessageBuilderException extends CustomException
{
    /**
     * PhaseChangeMessageBuilderException constructor
     *
     * @param message Error message
     */
    private ActionMessageBuilderException(String message)
    {
        super(message);
    }

    /**
     * Factory method for action not set exception
     *
     * @return ActionMessageBuilderException
     */
    static ActionMessageBuilderException createActionNotSet()
    {
        return new ActionMessageBuilderException("I don't know how to log that action");
    }

    /**
     * Factory method for no loot exception
     *
     * @param explorerName Name of explorer that is missing loot
     *
     * @return MessageBuilderException
     */
    static ActionMessageBuilderException createNoLoot(String explorerName)
    {
        return new ActionMessageBuilderException(String.format(
            "I can't seem to find %s's loot... That's a problem",
            explorerName
        ));
    }
}
