package bot.Battle.HostileEncounter;

import bot.Battle.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

class AttackCommand extends bot.Battle.AttackCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param holder         Battle holder
     * @param dmChecker      Dungeon master checker
     */
    AttackCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        super(
            processManager,
            holder,
            dmChecker,
            "Attack a hostile during the attack turn.",
            "HostileName"
        );
    }
}
