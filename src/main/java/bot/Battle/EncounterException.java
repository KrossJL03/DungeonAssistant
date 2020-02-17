package bot.Battle;

import bot.CustomException;
import bot.MyProperties;
import org.jetbrains.annotations.NotNull;

public class EncounterException extends CustomException
{

    /**
     * EncounterException constructor
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
     * Factory method for "no players have joined"
     *
     * @return EncounterException
     */
    static @NotNull EncounterException createNoPlayersHaveJoined()
    {
        return new EncounterException("Wait, we can't start yet! No players have joined!");
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

    /**
     * Factory method for "set tier after create phase"
     *
     * @return EncounterException
     */
    static @NotNull EncounterException createSetTierAfterCreatePhase()
    {
        return new EncounterException("Tier must be set before the encounter has started");
    }
}
