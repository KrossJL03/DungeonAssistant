package bot.Encounter.Command;

import bot.CommandParameter;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RemoveHostileCommand extends EncounterCommand
{
    /**
     * RemoveHostileCommand constructor
     *
     * @param processManager Process manager
     * @param holder         Encounter holder
     * @param logger         Encounter logger
     */
    RemoveHostileCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull EncounterLogger logger
    )
    {
        super(
            processManager,
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
