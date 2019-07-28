package bot.Encounter.Command;

import bot.CommandFactoryInterface;
import bot.CommandInterface;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.Encounter.Logger.Message.Action.ActionMessageBuilder;
import bot.Encounter.Logger.Message.PhaseChange.PhaseChangeMessageBuilder;
import bot.Encounter.Logger.Message.Summary.SummaryMessageBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EncounterCommandFactory implements CommandFactoryInterface
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createCommands()
    {
        ArrayList<CommandInterface> commands = new ArrayList<>();
        EncounterLogger logger = new EncounterLogger(
            new ActionMessageBuilder(),
            new PhaseChangeMessageBuilder(),
            new SummaryMessageBuilder()
        );
        EncounterHolder holder = new EncounterHolder(logger);

        commands.add(new AddHostileCommand(holder, logger));
        commands.add(new AttackCommand(holder, logger));
        commands.add(new CreateHostileEncounterCommand(holder, logger));
        commands.add(new DodgeCommand(holder, logger));
        commands.add(new DodgePassCommand(holder, logger));
        commands.add(new DodgePassHelpCommand(holder, logger));
        commands.add(new EndActionCommand(holder, logger));
        commands.add(new EndEncounterCommand(holder, logger));
        commands.add(new EndTurnCommand(holder, logger));
        commands.add(new HealCommand(holder, logger));
        commands.add(new HurtCommand(holder, logger));
        commands.add(new JoinCommand(holder, logger));
        commands.add(new KickCommand(holder, logger));
        commands.add(new LeaveCommand(holder, logger));
        commands.add(new LootCommand(holder, logger));
        commands.add(new ProtectCommand(holder, logger));
        commands.add(new RejoinCommand(holder, logger));
        commands.add(new RemoveExplorerCommand(holder, logger));
        commands.add(new RemoveHostileCommand(holder, logger));
        commands.add(new SetMaxPlayerCountCommand(holder, logger));
        commands.add(new SetTierCommand(holder, logger));
        commands.add(new SkipCommand(holder, logger));
        commands.add(new StartAttackPhaseCommand(holder, logger));
        commands.add(new StartDodgePhaseCommand(holder, logger));
        commands.add(new StartEncounterCommand(holder, logger));
        commands.add(new StatBoostCommand(holder, logger));
        commands.add(new StatDropCommand(holder, logger));
        commands.add(new ViewSummaryCommand(holder, logger));

        return commands;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createAdditionalCommands()
    {
        ArrayList<CommandInterface> commands = new ArrayList<>();
        EncounterLogger logger = new EncounterLogger(
            new ActionMessageBuilder(),
            new PhaseChangeMessageBuilder(),
            new SummaryMessageBuilder()
        );
        EncounterHolder holder = new EncounterHolder(logger);

        commands.add(new UseItemCommand(holder, logger));

        return commands;
    }
}
