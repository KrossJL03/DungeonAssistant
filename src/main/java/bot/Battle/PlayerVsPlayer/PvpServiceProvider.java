package bot.Battle.PlayerVsPlayer;

import bot.Battle.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.Battle.EndActionCommand;
import bot.Battle.EndBattleCommand;
import bot.Battle.EndTurnCommand;
import bot.Battle.JoinCommand;
import bot.Battle.KickCommand;
import bot.Battle.SetPartySizeCommand;
import bot.Battle.SetTierCommand;
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
     * @param processManager  Process manager
     * @param encounterHolder Battle holder
     * @param privateLogger   Private logger
     * @param dmChecker       Dungeon master checker
     */
    public PvpServiceProvider(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder encounterHolder,
        @NotNull PrivateLogger privateLogger,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        commands = new ArrayList<>();

        // order matters
        commands.add(new StartAttackPhaseCommand(processManager, encounterHolder, dmChecker));
        commands.add(new StartBattleCommand(processManager, encounterHolder, dmChecker));
        commands.add(new AttackCommand(processManager, encounterHolder, dmChecker));
        commands.add(new CreatePvpCommand(processManager, encounterHolder, dmChecker));
        commands.add(new EndActionCommand(processManager, encounterHolder, dmChecker));
        commands.add(new EndBattleCommand(processManager, encounterHolder, dmChecker));
        commands.add(new EndTurnCommand(processManager, encounterHolder, dmChecker));
        commands.add(new JoinCommand(processManager, encounterHolder, dmChecker));
        commands.add(new KickCommand(processManager, encounterHolder, dmChecker));
        commands.add(new LeaveCommand(processManager, encounterHolder, dmChecker));
        commands.add(new RemoveCommand(processManager, encounterHolder, dmChecker));
        commands.add(new SetPartySizeCommand(processManager, encounterHolder, dmChecker));
        commands.add(new SetTierCommand(processManager, encounterHolder, dmChecker));
        commands.add(new SkipCommand(processManager, encounterHolder, dmChecker));
        commands.add(new ViewSummaryCommand(processManager, encounterHolder, dmChecker));

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
