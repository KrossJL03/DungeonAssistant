package bot.Battle.Pvp;

import bot.Battle.DungeonMasterChecker;
import bot.Battle.JoinCommand;
import bot.Battle.KickCommand;
import bot.Battle.StartAttackPhaseCommand;
import bot.Battle.StartBattleCommand;
import bot.Battle.ViewSummaryCommand;
import bot.Command;
import bot.CommandProviderInterface;
import bot.PrivateLogger;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PvpServiceProvider implements CommandProviderInterface
{
    private ArrayList<Command> commands;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param privateLogger  Private logger
     * @param dmChecker      Dungeon master checker
     */
    public PvpServiceProvider(
        @NotNull ProcessManager processManager,
        @NotNull PrivateLogger privateLogger,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        commands = new ArrayList<>();

        // order matters
        commands.add(new StartAttackPhaseCommand(processManager, dmChecker));
        commands.add(new StartBattleCommand(processManager, dmChecker));
        commands.add(new AttackCommand(processManager, dmChecker));
        commands.add(new CreatePvpCommand(processManager, dmChecker));
        commands.add(new EndBattleCommand(processManager, dmChecker));
        commands.add(new JoinCommand(processManager, dmChecker));
        commands.add(new KickCommand(processManager, dmChecker));
        commands.add(new LeaveCommand(processManager, dmChecker));
        commands.add(new SkipCommand(processManager, dmChecker));
        commands.add(new ViewSummaryCommand(processManager, dmChecker));

        commands.add(new HelpCommand(processManager, privateLogger, new ArrayList<>(commands)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<Command> getCommands()
    {
        return commands;
    }
}
