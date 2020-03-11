package bot.Battle.Encounter;

import bot.Battle.DungeonMasterChecker;
import bot.Message;
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
        super(
            processManager,
            dmChecker,
            buildDescription()
        );
    }

    /**
     * Build description
     *
     * @return String
     */
    private static @NotNull String buildDescription()
    {
        Message description = new Message();

        description.add("Leave the battle.");
        description.add("When the battle ends you may loot any hostiles that were killed before leaving.");

        return description.getAsString();
    }
}
