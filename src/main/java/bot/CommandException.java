package bot;

import bot.Encounter.EncounterExceptionInterface;
import org.jetbrains.annotations.NotNull;

public class CommandException extends RuntimeException implements EncounterExceptionInterface
{
    /**
     * CommandException constructor
     *
     * @param message Message
     */
    protected @NotNull CommandException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "missing parameters"
     *
     * @param formattedCommand HelpCommand with parameters
     *
     * @return CommandException
     */
    static @NotNull CommandException createMissingParameters(String formattedCommand)
    {
        return new CommandException(
            String.format(
                "I think I'm missing something... could you say the command again like this: `%s`",
                formattedCommand
            )
        );
    }

    /**
     * Factory method for "not a mod"
     *
     * @param formattedCommand Formatted command
     *
     * @return CommandException
     */
    static @NotNull CommandException createNotMod(String formattedCommand)
    {
        return new CommandException(String.format("Only mods can use the `%s` command", formattedCommand));
    }
}
