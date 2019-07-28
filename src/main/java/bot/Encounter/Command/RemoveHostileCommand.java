package bot.Encounter.Command;

import bot.CommandParameter;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RemoveHostileCommand extends EncounterCommand
{
    /**
     * RemoveExplorerCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    RemoveHostileCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "removeHostile",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("HostileName", true));
                }
            },
            "Remove a hostile from an encounter.",
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
        String   hostileName = parameters[0];

        getHostileEncounter().removeHostile(hostileName);
    }
}
