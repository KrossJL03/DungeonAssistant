package bot.Encounter.Command;

import bot.CommandParameter;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class KickCommand extends EncounterCommand
{
    /**
     * KickCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    KickCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "kick",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("ExplorerName", true));
                }
            },
            "Forcibly remove a player from an encounter. " +
            "The rejoin command cannot be used by the player to return.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        String[] parameters   = getParametersFromEvent(event);
        String   explorerName = parameters[0];

        getEncounter().kick(explorerName);
    }
}
