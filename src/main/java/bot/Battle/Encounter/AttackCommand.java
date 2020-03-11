package bot.Battle.Encounter;

import bot.Battle.DungeonMasterChecker;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

class AttackCommand extends bot.Battle.AttackCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param dmChecker      Dungeon master checker
     */
    AttackCommand(@NotNull ProcessManager processManager, @NotNull DungeonMasterChecker dmChecker)
    {
        super(
            processManager,
            dmChecker,
            "Attack a hostile during the attack turn.",
            "HostileName"
        );
    }
}
