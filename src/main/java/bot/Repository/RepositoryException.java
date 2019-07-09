package bot.Repository;

import bot.CustomExceptionInterface;
import org.jetbrains.annotations.NotNull;

/**
 * Repository exception
 */
public class RepositoryException extends RuntimeException implements CustomExceptionInterface
{
    /**
     * RepositoryException constructor
     *
     * @param message Message
     */
    private RepositoryException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "command locked"
     *
     * @return RepositoryException
     */
    public static @NotNull RepositoryException createCommandLocked()
    {
        // todo make message dynamic
        return new RepositoryException(
            "Characters cannot be created or updated while an encounter is in progress, please try again later."
        );
    }

    /**
     * Factory method for "failed to close connection"
     *
     * @return RepositoryException
     */
    public static @NotNull RepositoryException createFailedToCloseConnection()
    {
        return new RepositoryException("Uh oh, I couldn't close my connection...");
    }

    /**
     * Factory method for "failed to retrieve"
     *
     * @return RepositoryException
     */
    public static @NotNull RepositoryException createFailedToRetrieve() {
        return new RepositoryException("Uh, couldn't find what you're looking for...");
    }

    /**
     * Factory method for "failed to update"
     *
     * @return RepositoryException
     */
    public static @NotNull RepositoryException createFailedToUpdate()
    {
        return new RepositoryException("Uh, could you say that again? I'm having trouble saving it");
    }

}
