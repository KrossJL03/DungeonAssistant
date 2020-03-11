package bot.Battle.PlayerVsPlayer;

import bot.Battle.DungeonMasterChecker;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

class LeaveCommand extends bot.Battle.LeaveCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param dmChecker      Dungeon master checker
     */
    LeaveCommand(@NotNull ProcessManager processManager, @NotNull DungeonMasterChecker dmChecker)
    {
        super(processManager, dmChecker, "Leave the battle.");
    }
}
