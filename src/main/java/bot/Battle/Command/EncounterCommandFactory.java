package bot.Battle.Command;

import bot.Battle.DungeonMasterChecker.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.CommandFactoryInterface;
import bot.CommandInterface;
import bot.PrivateLogger;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EncounterCommandFactory implements CommandFactoryInterface
{
    private DungeonMasterChecker dmChecker;
    private EncounterHolder      encounterHolder;
    private PrivateLogger        privateLogger;
    private ProcessManager       processManager;

    /**
     * Constructor.
     *
     * @param processManager  Process manager
     * @param encounterHolder Battle holder
     * @param privateLogger   Private logger
     * @param dmChecker       Dungeon master checker
     */
    public EncounterCommandFactory(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder encounterHolder,
        @NotNull PrivateLogger privateLogger,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        this.dmChecker = dmChecker;
        this.encounterHolder = encounterHolder;
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

        commands.add(new UseItemCommand(processManager, encounterHolder, dmChecker));

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
        encounterCommands.add(new StartAttackPhaseCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new StartDodgePhaseCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new StartEncounterCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new AddHostileCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new AttackCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new CreateHostileEncounterCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new CreatePvpCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new DodgeCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new GuardCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new PassCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new DodgePassCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new EndActionCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new EndEncounterCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new EndTurnCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new HealCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new HurtCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new DmProtectCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new JoinCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new KickCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new LeaveCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new LootCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new ProtectCommand(processManager, encounterHolder, dmChecker));
//        encounterCommands.add(new RejoinCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new RemoveCreatureCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new SetMaxPlayerCountCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new SetTierCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new SkipCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new ModifyStatCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new ViewSummaryCommand(processManager, encounterHolder, dmChecker));
        encounterCommands.add(new ReviveCommand(processManager, encounterHolder, dmChecker));

        ArrayList<CommandInterface> commands = new ArrayList<>(encounterCommands);
        encounterCommands.add(new UseItemCommand(processManager, encounterHolder, dmChecker));
        commands.add(new HelpEncounterCommand(processManager, dmChecker, privateLogger, encounterCommands));

        return commands;
    }
}
