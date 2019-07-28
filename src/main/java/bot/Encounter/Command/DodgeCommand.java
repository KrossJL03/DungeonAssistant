package bot.Encounter.Command;

import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.Player.Player;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DodgeCommand extends EncounterCommand
{
    /**
     * DodgeCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    DodgeCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "dodge",
            new ArrayList<>(),
            "Attempt to dodge enemy attacks during the dodge turn.",
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

        getHostileEncounter().dodgeAction(player);
    }
}
