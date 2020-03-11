package bot.Battle.Encounter;

import bot.Battle.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
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

public class EncounterServiceProvider implements CommandProviderInterface
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
    public EncounterServiceProvider(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder encounterHolder,
        @NotNull PrivateLogger privateLogger,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        commands = new ArrayList<>();

        // order matters
        commands.add(new StartAttackPhaseCommand(processManager, encounterHolder, dmChecker));
        commands.add(new StartDodgePhaseCommand(processManager, encounterHolder, dmChecker));
        commands.add(new StartBattleCommand(processManager, encounterHolder, dmChecker));
        commands.add(new AddHostileCommand(processManager, encounterHolder, dmChecker));
        commands.add(new AttackCommand(processManager, encounterHolder, dmChecker));
        commands.add(new CreateEncounterCommand(processManager, encounterHolder, dmChecker));
        commands.add(new DodgeCommand(processManager, encounterHolder, dmChecker));
        commands.add(new GuardCommand(processManager, encounterHolder, dmChecker));
        commands.add(new PassCommand(processManager, encounterHolder, dmChecker));
        commands.add(new DodgePassCommand(processManager, encounterHolder, dmChecker));
        commands.add(new EndActionCommand(processManager, encounterHolder, dmChecker));
        commands.add(new EndBattleCommand(processManager, encounterHolder, dmChecker));
        commands.add(new EndTurnCommand(processManager, encounterHolder, dmChecker));
        commands.add(new HealCommand(processManager, encounterHolder, dmChecker));
        commands.add(new HurtCommand(processManager, encounterHolder, dmChecker));
        commands.add(new DmProtectCommand(processManager, encounterHolder, dmChecker));
        commands.add(new JoinCommand(processManager, encounterHolder, dmChecker));
        commands.add(new KickCommand(processManager, encounterHolder, dmChecker));
        commands.add(new LeaveCommand(processManager, encounterHolder, dmChecker));
        commands.add(new LootCommand(processManager, encounterHolder, dmChecker));
        commands.add(new ModifyStatCommand(processManager, encounterHolder, dmChecker));
        commands.add(new ProtectCommand(processManager, encounterHolder, dmChecker));
//        commands.add(new RejoinCommand(processManager, encounterHolder, dmChecker));
        commands.add(new RemoveCommand(processManager, encounterHolder, dmChecker));
        commands.add(new SetPartySizeCommand(processManager, encounterHolder, dmChecker));
        commands.add(new SetTierCommand(processManager, encounterHolder, dmChecker));
        commands.add(new SkipCommand(processManager, encounterHolder, dmChecker));
        commands.add(new ViewSummaryCommand(processManager, encounterHolder, dmChecker));
        commands.add(new ReviveCommand(processManager, encounterHolder, dmChecker));
        commands.add(new UseItemCommand(processManager, encounterHolder, dmChecker));

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
