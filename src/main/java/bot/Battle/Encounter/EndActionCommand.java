package bot.Battle.Encounter;

import bot.Battle.BattleCommand;
import bot.Battle.DungeonMasterChecker;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class EndActionCommand extends BattleCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param dmChecker      Dungeon master checker
     */
    EndActionCommand(@NotNull ProcessManager processManager, @NotNull DungeonMasterChecker dmChecker)
    {
        super(
            processManager,
            dmChecker,
            "endAction",
            new ArrayList<>(),
            "End a single action of a player. No ill side effects.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute(@NotNull MessageReceivedEvent event)
    {
        getBattle().useCurrentExplorerAction();
    }
}
