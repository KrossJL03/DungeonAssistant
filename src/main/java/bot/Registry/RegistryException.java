package bot.Registry;

import bot.CustomExceptionInterface;
import org.jetbrains.annotations.NotNull;

/**
 * Registry exception
 */
public class RegistryException extends RuntimeException implements CustomExceptionInterface
{
    /**
     * RegistryException constructor
     *
     * @param message Message
     */
    private RegistryException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "command locked"
     *
     * @return RegistryException
     */
    public static @NotNull RegistryException createCommandLocked()
    {
        // todo make message dynamic
        return new RegistryException(
            "Characters cannot be created or updated while an encounter is in progress, please try again later."
        );
    }

    /**
     * Factory method for "failed to close connection"
     *
     * @return RegistryException
     */
    public static @NotNull RegistryException createFailedToCloseConnection()
    {
        return new RegistryException("Uh oh, I couldn't close my connection...");
    }

    /**
     * Factory method for "failed to retrieve"
     *
     * @return RegistryException
     */
    public static @NotNull RegistryException createFailedToRetrieve() {
        return new RegistryException("Uh, couldn't find what you're looking for...");
    }

    /**
     * Factory method for "failed to update"
     *
     * @return RegistryException
     */
    public static @NotNull RegistryException createFailedToUpdate()
    {
        return new RegistryException("Uh, could you say that again? I'm having trouble saving it");
    }

}
