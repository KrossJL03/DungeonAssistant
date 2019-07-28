package bot.Encounter.Command;

import bot.Encounter.EncounterHolder;
import bot.CommandParameter;
import bot.Encounter.Logger.EncounterLogger;
import bot.Player.Player;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AttackCommand extends EncounterCommand
{
    /**
     * AttackCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    AttackCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "attack",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("CreatureName", true));
                }
            },
            "Attack a creature during the attack turn",
            false
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        Player   player     = getPlayerFromEvent(event);
        String[] parameters = getParametersFromEvent(event);
        String   targetName = parameters[0];

        getEncounter().attackAction(player, targetName);
    }
}
