package bot.Battle;

import bot.CustomException;
import org.jetbrains.annotations.NotNull;

class ExplorerRosterException extends CustomException
{
    /**
     * ExplorerRosterException constructor
     *
     * @param message Message
     */
    private @NotNull ExplorerRosterException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "explorer not found"
     *
     * @param name Explorer name
     *
     * @return ExplorerRosterException
     */
    static @NotNull ExplorerRosterException createExplorerNotFound(@NotNull String name)
    {
        return new ExplorerRosterException(
            String.format("I can't find %s, are you sure they are part of this encounter?", name)
        );
    }
}
