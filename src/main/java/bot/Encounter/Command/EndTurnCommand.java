package bot.Encounter.Command;

import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EndTurnCommand extends EncounterCommand
{
    /**
     * EndTurnCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    EndTurnCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "endTurn",
            new ArrayList<>(),
            "End a players turn. No ill side effects.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        getEncounter().useAllCurrentExplorerActions();
    }
}
