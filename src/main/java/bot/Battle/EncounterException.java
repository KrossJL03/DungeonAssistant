package bot.Battle;

import bot.CustomException;
import org.jetbrains.annotations.NotNull;

class EncounterException extends CustomException
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
}
