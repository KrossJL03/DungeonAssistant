package bot;

import org.jetbrains.annotations.NotNull;

public class CustomException extends RuntimeException implements CustomExceptionInterface
{
    /**
     * Constructor.
     *
     * @param message Message
     */
    public @NotNull CustomException(@NotNull String message)
    {
        super(message);
    }
}
