package bot.Encounter.Command;

import bot.CommandParameter;
import bot.Encounter.EncounterHolder;
import bot.Encounter.EncounteredHostileInterface;
import bot.Encounter.Logger.EncounterLogger;
import bot.Hostile.Hostile;
import bot.Hostile.HostileManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AddHostileCommand extends EncounterCommand
{
    /**
     * AttackCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    AddHostileCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "addHostile",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("Species", true));
                    add(new CommandParameter("Nickname", false));
                }
            },
            "Add a hostile to an encounter.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        String[]                    parameters = getParametersFromEvent(event);
        Hostile                     hostile    = HostileManager.getHostile(parameters[1]);
        String                      name       = parameters.length > 2 ? parameters[2] : parameters[1];
        EncounteredHostileInterface result     = getHostileEncounter().addHostile(hostile, name);
        getLogger().logAddedHostile(result);
    }
}
