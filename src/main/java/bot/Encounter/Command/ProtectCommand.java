package bot.Encounter.Command;

import bot.CommandParameter;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.Player.Player;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProtectCommand extends EncounterCommand
{
    /**
     * AttackCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    ProtectCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "protect",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("ExplorerName", true));
                }
            },
            "Protect a teammate during the dodge turn.",
            false
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        Player   player       = getPlayerFromEvent(event);
        String[] parameters   = getParametersFromEvent(event);
        String   explorerName = parameters[0];

        getHostileEncounter().protectAction(player, explorerName);
    }
}
