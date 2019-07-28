package bot.Encounter.Command;

import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelpCommand extends EncounterCommand
{
    /**
     * HelpCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    HelpCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "help encounter",
            new ArrayList<>(),
            "View all encounter commands.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        event.getChannel().sendMessage("Help").queue();
    }
}
