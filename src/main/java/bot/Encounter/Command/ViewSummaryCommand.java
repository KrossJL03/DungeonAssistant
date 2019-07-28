package bot.Encounter.Command;

import bot.Encounter.Encounter;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewSummaryCommand extends EncounterCommand
{
    /**
     * HelpCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    ViewSummaryCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "view summary",
            new ArrayList<>(),
            "View encounter summary.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        Encounter encounter = getHostileEncounter();
        getLogger().logSummary(encounter.getAllExplorers(), encounter.getAllHostiles());
    }
}
