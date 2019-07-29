package bot.Encounter.Command;

import bot.CommandParameter;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SetMaxPlayerCountCommand extends EncounterCommand
{
    /**
     * SetMaxPlayerCountCommand constructor
     *
     * @param processManager Process manager
     * @param holder         Encounter holder
     * @param logger         Encounter logger
     */
    SetMaxPlayerCountCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull EncounterLogger logger
    )
    {
        super(
            processManager,
            holder,
            logger,
            "maxPlayers",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("PlayerCount", true));
                }
            },
            "Set number of players permitted for this encounter.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        String[] parameters = getParametersFromEvent(event);
        int      maxPlayers = Integer.parseInt(parameters[0]);

        getEncounter().setMaxPlayerCount(maxPlayers);
    }
}
