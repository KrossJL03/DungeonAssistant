package bot.Encounter.Command;

import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.Player.Player;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RejoinCommand extends EncounterCommand
{
    /**
     * RejoinCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    RejoinCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "rejoin",
            new ArrayList<>(),
            "Rejoin the encounter with the same character.",
            false
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        Player player = getPlayerFromEvent(event);

        getEncounter().rejoin(player);
    }
}
