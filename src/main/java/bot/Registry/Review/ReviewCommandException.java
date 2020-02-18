package bot.Registry.Review;

import bot.CommandException;
import org.jetbrains.annotations.NotNull;

class ReviewCommandException extends CommandException
{
    /**
     * ReviewCommandException constructor
     *
     * @param message Message
     */
    private ReviewCommandException(@NotNull String message)
    {
        super(message);
    }
}
