package bot.Encounter.Command;

import bot.CommandParameter;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StatDropCommand extends EncounterCommand
{
    /**
     * StatBoostCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    StatDropCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "statDrop",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("ExplorerName", true));
                    add(new CommandParameter("StatName", true));
                    add(new CommandParameter("DropAmount", true));
                }
            },
            "Temporarily decrease an explorer's stat by the boost amount for the duration of the encounter.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        String[] parameters  = getParametersFromEvent(event);
        String   targetName  = parameters[0];
        String   statName    = parameters[1];
        int      dropAmount  = 0 - Integer.parseInt(parameters[2]);
        getEncounter().modifyStat(targetName, statName, dropAmount);
    }
}
