package bot.Battle.DungeonMasterChecker;

import bot.CustomException;
import org.jetbrains.annotations.NotNull;

class DungeonMasterCheckerException extends CustomException
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
