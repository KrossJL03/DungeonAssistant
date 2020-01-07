package bot.Item;

import bot.CommandException;
import org.jetbrains.annotations.NotNull;

class ItemException extends CommandException
{
    /**
     * Constructor.
     *
     * @param message Message
     */
    private ItemException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method.
     *
     * @param commandName Command name
     *
     * @return ItemException
     */
    static @NotNull ItemException createMultipleAttachments(String commandName)
    {
        return new ItemException(String.format(
            "The %s command can only accept 1 attachment at a time.",
            commandName
        ));
    }

    /**
     * Factory method.
     *
     * @param commandName Command name
     *
     * @return ItemException
     */
    static @NotNull ItemException createNoAttachment(String commandName)
    {
        return new ItemException(String.format(
            "An attachment is required for the %s command, please try again.",
            commandName
        ));
    }
}
