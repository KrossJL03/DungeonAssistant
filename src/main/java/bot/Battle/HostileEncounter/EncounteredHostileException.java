package bot.Battle.HostileEncounter;

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
