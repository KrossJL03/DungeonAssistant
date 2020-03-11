package bot.Battle.Encounter;

import bot.CustomException;
import org.jetbrains.annotations.NotNull;

class EncounteredHostileException extends CustomException
{
    /**
     * EncounteredExplorerException constructor
     *
     * @param message Message
     */
    private @NotNull EncounteredHostileException(@NotNull String message)
    {
        super(message);
    }
}
