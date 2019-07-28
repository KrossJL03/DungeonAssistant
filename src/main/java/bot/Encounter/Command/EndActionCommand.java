package bot.Encounter.Command;

import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EndActionCommand extends EncounterCommand
{
    /**
     * EndActionCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    EndActionCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "endAction",
            new ArrayList<>(),
            "End a single action of a player. No ill side effects.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        getEncounter().useCurrentExplorerAction();
    }
}
