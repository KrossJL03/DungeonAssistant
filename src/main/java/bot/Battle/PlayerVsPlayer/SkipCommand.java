package bot.Battle.PlayerVsPlayer;

import bot.Battle.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

class SkipCommand extends bot.Battle.SkipCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param holder         Battle holder
     * @param dmChecker      Dungeon master checker
     */
    SkipCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        super(
            processManager,
            holder,
            dmChecker,
            "Skip the current player's turn."
        );
    }
}
