package bot.Battle.Encounter;

import bot.Battle.DungeonMasterChecker;
import bot.Battle.JoinCommand;
import bot.Battle.KickCommand;
import bot.Battle.StartAttackPhaseCommand;
import bot.Battle.StartBattleCommand;
import bot.Battle.ViewSummaryCommand;
import bot.Command;
import bot.CommandProviderInterface;
import bot.MyProperties;
import bot.PrivateLogger;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EncounterServiceProvider implements CommandProviderInterface
{
    private ArrayList<Command> commands;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param privateLogger  Private logger
     * @param dmChecker      Dungeon master checker
     */
    public EncounterServiceProvider(
        @NotNull ProcessManager processManager,
        @NotNull PrivateLogger privateLogger,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        commands = new ArrayList<>();

        // order matters
        commands.add(new StartAttackPhaseCommand(processManager, dmChecker));
        commands.add(new StartDodgePhaseCommand(processManager, dmChecker));
        commands.add(new StartBattleCommand(processManager, dmChecker));
        commands.add(new AddHostileCommand(processManager, dmChecker));
        commands.add(new AttackCommand(processManager, dmChecker));
        commands.add(new CreateEncounterCommand(processManager, dmChecker));
        commands.add(new DodgeCommand(processManager, dmChecker));
        commands.add(new GuardCommand(processManager, dmChecker));
        commands.add(new PassCommand(processManager, dmChecker));
        commands.add(new DodgePassCommand(processManager, dmChecker));
        commands.add(new EndActionCommand(processManager, dmChecker));
        commands.add(new EndBattleCommand(processManager, dmChecker));
        commands.add(new EndTurnCommand(processManager, dmChecker));
        commands.add(new HealCommand(processManager, dmChecker));
        commands.add(new HurtCommand(processManager, dmChecker));
        commands.add(new DmProtectCommand(processManager, dmChecker));
        commands.add(new JoinCommand(processManager, dmChecker));
        commands.add(new KickCommand(processManager, dmChecker));
        commands.add(new LeaveCommand(processManager, dmChecker));
        commands.add(new LootCommand(processManager, dmChecker));
        commands.add(new ModifyStatCommand(processManager, dmChecker));
        commands.add(new ProtectCommand(processManager, dmChecker));
        commands.add(new RemoveCommand(processManager, dmChecker));
        commands.add(new SetPartySizeCommand(processManager, dmChecker));
        commands.add(new SetTierCommand(processManager, dmChecker));
        commands.add(new SkipCommand(processManager, dmChecker));
        commands.add(new ViewSummaryCommand(processManager, dmChecker));
        commands.add(new ReviveCommand(processManager, dmChecker));
        commands.add(new UseItemCommand(processManager, dmChecker));

        if (MyProperties.ENABLE_COMMAND_REJOIN) {
            commands.add(new RejoinCommand(processManager, dmChecker));
        }

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
