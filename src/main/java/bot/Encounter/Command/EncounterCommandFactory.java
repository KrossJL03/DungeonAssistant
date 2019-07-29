package bot.Encounter.Command;

import bot.CommandFactoryInterface;
import bot.CommandInterface;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EncounterCommandFactory implements CommandFactoryInterface
{
    private EncounterHolder holder;
    private EncounterLogger logger;
    private ProcessManager  processManager;

    /**
     * EncounterCommandFactory constructor.
     *
     * @param processManager Process manager
     * @param holder         Encounter holder
     * @param logger         Encounter logger
     */
    public EncounterCommandFactory(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull EncounterLogger logger
    )
    {
        this.holder = holder;
        this.logger = logger;
        this.processManager = processManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createCommands()
    {
        ArrayList<CommandInterface> commands = new ArrayList<>();

        commands.add(new AddHostileCommand(processManager, holder, logger));
        commands.add(new AttackCommand(processManager, holder, logger));
        commands.add(new CreateHostileEncounterCommand(processManager, holder, logger));
        commands.add(new DodgeCommand(processManager, holder, logger));
        commands.add(new DodgePassCommand(processManager, holder, logger));
        commands.add(new DodgePassHelpCommand(processManager, holder, logger));
        commands.add(new EndActionCommand(processManager, holder, logger));
        commands.add(new EndEncounterCommand(processManager, holder, logger));
        commands.add(new EndTurnCommand(processManager, holder, logger));
        commands.add(new HealCommand(processManager, holder, logger));
        commands.add(new HelpCommand(processManager, holder, logger));
        commands.add(new HurtCommand(processManager, holder, logger));
        commands.add(new JoinCommand(processManager, holder, logger));
        commands.add(new KickCommand(processManager, holder, logger));
        commands.add(new LeaveCommand(processManager, holder, logger));
        commands.add(new LootCommand(processManager, holder, logger));
        commands.add(new ProtectCommand(processManager, holder, logger));
        commands.add(new RejoinCommand(processManager, holder, logger));
        commands.add(new RemoveExplorerCommand(processManager, holder, logger));
        commands.add(new RemoveHostileCommand(processManager, holder, logger));
        commands.add(new SetMaxPlayerCountCommand(processManager, holder, logger));
        commands.add(new SetTierCommand(processManager, holder, logger));
        commands.add(new SkipCommand(processManager, holder, logger));
        commands.add(new StartAttackPhaseCommand(processManager, holder, logger));
        commands.add(new StartDodgePhaseCommand(processManager, holder, logger));
        commands.add(new StartEncounterCommand(processManager, holder, logger));
        commands.add(new StatBoostCommand(processManager, holder, logger));
        commands.add(new StatDropCommand(processManager, holder, logger));
        commands.add(new ViewSummaryCommand(processManager, holder, logger));

        return commands;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createAdditionalCommands()
    {
        ArrayList<CommandInterface> commands = new ArrayList<>();

        commands.add(new UseItemCommand(processManager, holder, logger));

        return commands;
    }
}
