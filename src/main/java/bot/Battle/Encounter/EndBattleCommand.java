package bot.Battle.Encounter;

import bot.Battle.DungeonMasterChecker;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

class EndBattleCommand extends bot.Battle.EndBattleCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param dmChecker      Dungeon master checker
     */
    EndBattleCommand(@NotNull ProcessManager processManager, @NotNull DungeonMasterChecker dmChecker)
    {
        super(processManager, dmChecker, true);
    }
}
