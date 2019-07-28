package bot.Registry.Record.Command;

import bot.CommandException;
import org.jetbrains.annotations.NotNull;

class RecordCommandException extends CommandException
{
    /**
     * RecordCommandException constructor
     *
     * @param message Message
     */
    private RecordCommandException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "command locked"
     *
     * @return RecordCommandException
     */
    static @NotNull RecordCommandException createCommandLocked()
    {
        // todo make message dynamic
        return new RecordCommandException(
            "Characters cannot be created or updated while an encounter is in progress, please try again later."
        );
    }
}
