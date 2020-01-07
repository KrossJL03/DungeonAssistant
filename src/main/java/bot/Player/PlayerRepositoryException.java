package bot.Player;

import bot.CommandListener;
import bot.CustomExceptionInterface;
import bot.MyProperties;
import org.jetbrains.annotations.NotNull;

class PlayerRepositoryException extends RuntimeException implements CustomExceptionInterface
{
    /**
     * PlayerRepositoryException constructor
     *
     * @param message Message
     */
    private PlayerRepositoryException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "not found"
     *
     * @return PlayerRepositoryException
     */
    static @NotNull PlayerRepositoryException createNotFound()
    {
        return new PlayerRepositoryException(
            String.format(
                "Um... I'm sorry, do I know you? Try introducing yourself by saying `%shello` first.",
                MyProperties.COMMAND_PREFIX
            )
        );
    }

    /**
     * Factory method for "not found by name"
     *
     * @return PlayerRepositoryException
     */
    static @NotNull PlayerRepositoryException createNotFoundByName(String playerName)
    {
        return new PlayerRepositoryException(String.format("Um... I'm sorry, I don't think I know %s", playerName));
    }

}
