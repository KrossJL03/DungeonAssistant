package bot.Encounter.DungeonMasterChecker;

import bot.Encounter.EncounterExceptionInterface;
import org.jetbrains.annotations.NotNull;

class DungeonMasterCheckerException extends RuntimeException implements EncounterExceptionInterface
{
    /**
     * DungeonMasterCheckerException constructor
     *
     * @param message Message
     */
    private @NotNull DungeonMasterCheckerException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "dm not found"
     *
     * @return EncounterException
     */
    static @NotNull DungeonMasterCheckerException createDmNotFound()
    {
        return new DungeonMasterCheckerException(
            "How can we play without a DungeonMaster? I don't see that role anywhere..."
        );
    }
}
