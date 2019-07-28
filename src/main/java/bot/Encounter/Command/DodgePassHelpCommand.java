package bot.Encounter.Command;

import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.Player.Player;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DodgePassHelpCommand extends EncounterCommand
{
    /**
     * DodgePassHelpCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    DodgePassHelpCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "dodgePassHelp",
            new ArrayList<>(),
            "Auto dodge all attacks. Use for smoke bombs, special abilities, or being protected." +
            " DM will be pinged to confirm.",
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

        getLogger().pingDmDodgePass(player);
    }
}
