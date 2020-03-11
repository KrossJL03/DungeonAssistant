package bot.Battle.Pvp;

import bot.Battle.DungeonMasterChecker;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

class SkipCommand extends bot.Battle.SkipCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param dmChecker      Dungeon master checker
     */
    SkipCommand(@NotNull ProcessManager processManager, @NotNull DungeonMasterChecker dmChecker)
    {
        super(processManager, dmChecker, "Skip the current player's turn.");
    }
}
