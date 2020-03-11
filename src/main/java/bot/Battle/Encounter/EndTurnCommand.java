package bot.Battle.Encounter;

import bot.Battle.BattleCommand;
import bot.Battle.DungeonMasterChecker;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class EndTurnCommand extends BattleCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param dmChecker      Dungeon master checker
     */
    EndTurnCommand(@NotNull ProcessManager processManager, @NotNull DungeonMasterChecker dmChecker)
    {
        super(
            processManager,
            dmChecker,
            "endTurn",
            new ArrayList<>(),
            "End a players turn. No ill side effects.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute(@NotNull MessageReceivedEvent event)
    {
        getBattle().useAllCurrentExplorerActions();
    }
}
