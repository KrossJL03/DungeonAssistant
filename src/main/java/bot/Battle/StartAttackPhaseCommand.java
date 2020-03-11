package bot.Battle;

import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StartAttackPhaseCommand extends BattleCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param dmChecker      Dungeon master checker
     */
    public StartAttackPhaseCommand(@NotNull ProcessManager processManager, @NotNull DungeonMasterChecker dmChecker)
    {
        super(
            processManager,
            dmChecker,
            "attackTurn",
            new ArrayList<>(),
            "Start the attack turn.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute(@NotNull MessageReceivedEvent event)
    {
        getBattle().startAttackPhase();
    }
}
