package bot.Encounter.Command;

import bot.CommandFactoryInterface;
import bot.CommandInterface;
import bot.Encounter.DungeonMasterChecker.DungeonMasterChecker;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.PrivateLogger;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EncounterCommandFactory implements CommandFactoryInterface
{
    private DungeonMasterChecker dmChecker;
    private EncounterHolder      encounterHolder;
    private EncounterLogger      encounterLogger;
    private PrivateLogger        privateLogger;
    private ProcessManager       processManager;

    /**
     * Constructor.
     *
     * @param processManager  Process manager
     * @param encounterHolder Encounter holder
     * @param encounterLogger Encounter logger
     * @param privateLogger   Private logger
     * @param dmChecker       Dungeon master checker
     */
    public EncounterCommandFactory(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder encounterHolder,
        @NotNull EncounterLogger encounterLogger,
        @NotNull PrivateLogger privateLogger,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        this.dmChecker = dmChecker;
        this.encounterHolder = encounterHolder;
        this.encounterLogger = encounterLogger;
        this.privateLogger = privateLogger;
        this.processManager = processManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createAdditionalCommands()
    {
        ArrayList<CommandInterface> commands = new ArrayList<>();

        commands.add(new UseItemCommand(processManager, encounterHolder, encounterLogger, dmChecker));

        return commands;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createCommands()
    {
        ArrayList<EncounterCommand> encounterCommands = new ArrayList<>();

        // order matters
        encounterCommands.add(new StartAttackPhaseCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new StartDodgePhaseCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new StartEncounterCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new AddHostileCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new AttackCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new CreateHostileEncounterCommand(
            processManager,
            encounterHolder,
            encounterLogger,
            dmChecker
        ));
        encounterCommands.add(new DodgeCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new GuardCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new PassCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new DodgePassCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new EndActionCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new EndEncounterCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new EndTurnCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new HealCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new HurtCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new DmProtectCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new JoinCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new KickCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new LeaveCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new LootCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new ProtectCommand(processManager, encounterHolder, encounterLogger, dmChecker));
//        encounterCommands.add(new RejoinCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new RemoveCreatureCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new SetMaxPlayerCountCommand(
            processManager,
            encounterHolder,
            encounterLogger,
            dmChecker
        ));
        encounterCommands.add(new SetTierCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new SkipCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new ModifyStatCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new ViewSummaryCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        encounterCommands.add(new ReviveCommand(processManager, encounterHolder, encounterLogger, dmChecker));

        ArrayList<CommandInterface> commands = new ArrayList<>(encounterCommands);
        encounterCommands.add(new UseItemCommand(processManager, encounterHolder, encounterLogger, dmChecker));
        commands.add(new HelpEncounterCommand(processManager, dmChecker, privateLogger, encounterCommands));

        return commands;
    }
}
