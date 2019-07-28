package bot.Encounter.Command;

import bot.CommandParameter;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RemoveExplorerCommand extends EncounterCommand
{
    /**
     * RemoveExplorerCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    RemoveExplorerCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "removePc",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("ExplorerName", true));
                }
            },
            "Remove a character from an encounter. The player may join again with a new character.",
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

        getEncounter().removeExplorer(explorerName);
    }
}
