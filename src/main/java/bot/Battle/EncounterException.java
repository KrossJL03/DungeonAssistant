package bot.Battle;

import bot.CustomException;
import bot.MyProperties;
import org.jetbrains.annotations.NotNull;

public class EncounterException extends CustomException
{
    /**
     * Constructor.
     *
     * @param message Message
     */
    private @NotNull EncounterException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "wrong phase"
     *
     * @return EncounterException
     */
    public static @NotNull EncounterException createWrongPhase(@NotNull String command, @NotNull String phase)
    {
        return new EncounterException(String.format(
            "You can only `%s%s` during the %s turn",
            MyProperties.COMMAND_PREFIX,
            command,
            phase
        ));
    }

    /**
     * Factory method for "null encounter"
     *
     * @return EncounterException
     */
    static @NotNull EncounterException createNullEncounter()
    {
        return new EncounterException("There is no encounter being created, use `?create encounter` to start one.");
    }
}
