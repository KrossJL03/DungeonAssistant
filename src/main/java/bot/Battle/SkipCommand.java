package bot.Battle;

import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

abstract public class SkipCommand extends BattleCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param dmChecker      Dungeon master checker
     * @param description    Description
     */
    protected SkipCommand(
        @NotNull ProcessManager processManager,
        @NotNull DungeonMasterChecker dmChecker,
        @NotNull String description
    )
    {
        super(
            processManager,
            dmChecker,
            "skip",
            new ArrayList<>(),
            description,
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute(@NotNull MessageReceivedEvent event)
    {
        getBattle().skipCurrentPlayerTurn();
    }
}
