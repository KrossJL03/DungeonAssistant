package bot.Encounter.Command;

import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StartDodgePhaseCommand extends EncounterCommand
{
    /**
     * StartDodgePhaseCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    StartDodgePhaseCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "dodgeTurn",
            new ArrayList<>(),
            "Start dodge turn.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        getHostileEncounter().startDodgePhase();
    }
}
