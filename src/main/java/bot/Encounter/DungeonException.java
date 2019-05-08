package bot.Encounter;

import org.jetbrains.annotations.NotNull;

class DungeonException extends RuntimeException implements EncounterExceptionInterface
{
    // todo rename/move to Encounter exception

    /**
     * DungeonException constructor
     *
     * @param message Message
     */
    private @NotNull DungeonException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "no players have joined"
     *
     * @return DungeonException
     */
    static @NotNull DungeonException createNoPlayersHaveJoined()
    {
        return new DungeonException("Wait, we can't start yet! No players have joined!");
    }
}
