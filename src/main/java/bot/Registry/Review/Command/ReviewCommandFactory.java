package bot.Registry.Review.Command;

import bot.CommandFactoryInterface;
import bot.CommandInterface;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.Encounter.Logger.Message.Action.ActionMessageBuilder;
import bot.Encounter.Logger.Message.PhaseChange.PhaseChangeMessageBuilder;
import bot.Encounter.Logger.Message.Summary.SummaryMessageBuilder;
import bot.ProcessManager;
import bot.Registry.RegistryLogger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReviewCommandFactory implements CommandFactoryInterface
{
    private RegistryLogger logger;
    private ProcessManager processManager;

    /**
     * ReviewCommandFactory constructor.
     *
     * @param processManager Process manager
     * @param logger         Registry logger
     */
    public ReviewCommandFactory(
        @NotNull ProcessManager processManager,
        @NotNull RegistryLogger logger
    )
    {
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

        commands.add(new HelpCommand(processManager, logger));
        commands.add(new ViewExplorersCommand(processManager, logger));
        commands.add(new ViewHostileLootCommand(processManager, logger));
        commands.add(new ViewHostilesCommand(processManager, logger));
        commands.add(new ViewItemCommand(processManager, logger));
        commands.add(new ViewItemsCommand(processManager, logger));

        return commands;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createAdditionalCommands()
    {
        return new ArrayList<>();
    }

    private EncounterHolder getHolder()
    {
        // todo remove method
        EncounterLogger logger = new EncounterLogger(
            new ActionMessageBuilder(),
            new PhaseChangeMessageBuilder(),
            new SummaryMessageBuilder()
        );
        return new EncounterHolder(logger);
    }
}
