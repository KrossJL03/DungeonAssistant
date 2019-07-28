package bot.Registry.Review.Command;

import bot.CommandFactoryInterface;
import bot.CommandInterface;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.Encounter.Logger.Message.Action.ActionMessageBuilder;
import bot.Encounter.Logger.Message.PhaseChange.PhaseChangeMessageBuilder;
import bot.Encounter.Logger.Message.Summary.SummaryMessageBuilder;
import bot.Registry.RegistryLogger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReviewCommandFactory implements CommandFactoryInterface
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createCommands()
    {
        ArrayList<CommandInterface> commands = new ArrayList<>();
        RegistryLogger              logger   = new RegistryLogger();

        commands.add(new HelpCommand(logger));
        commands.add(new ViewExplorersCommand(logger));
        commands.add(new ViewHostileLootCommand(logger));
        commands.add(new ViewHostilesCommand(logger));
        commands.add(new ViewItemCommand(logger));
        commands.add(new ViewItemsCommand(logger));

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
