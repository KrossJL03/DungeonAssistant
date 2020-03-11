package bot.Battle.Encounter;

import bot.Battle.DungeonMasterChecker;
import bot.Message;
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

        description.add("Skip the current player's turn.");
        description.add("Automatically fail all dodge attempts.");

        return description.getAsString();
    }
}
