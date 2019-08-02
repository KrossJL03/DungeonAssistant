package bot.Encounter;

import org.jetbrains.annotations.NotNull;

class EncounterException extends RuntimeException implements EncounterExceptionInterface
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

    /**
     * Factory method for "revive non-slain explorer"
     *
     * @param name Name of explorer
     *
     * @return EncounterException
     */
    static @NotNull EncounterException createReviveNonSlainExplorer(String name)
    {
        return new EncounterException("%s looks fine to me, I don't think they need any phoenix down");
    }

    /**
     * Factory method for "used phoenix down"
     *
     * @param name Name of slain explorer
     *
     * @return EncounterException
     */
    static @NotNull EncounterException createUsedPhoenixDown(String name)
    {
        return new EncounterException("The guild leaders do not have any more phoenix down, %s cannot be revived");
    }
}
