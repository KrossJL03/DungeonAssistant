package bot.Encounter.Command;

import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EndEncounterCommand extends EncounterCommand
{
    /**
     * EndEncounterCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    EndEncounterCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "endEncounter",
            new ArrayList<>(),
            "End the encounter.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        getEncounter().startEndPhaseForced();
    }
}
