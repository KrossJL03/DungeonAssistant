package bot.Encounter.Command;

import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.Player.Player;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DodgeCommand extends EncounterCommand
{
    /**
     * DodgeCommand constructor
     *
     * @param processManager Process manager
     * @param holder         Encounter holder
     * @param logger         Encounter logger
     */
    DodgeCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull EncounterLogger logger
    )
    {
        super(
            processManager,
            holder,
            logger,
            "dodge",
            new ArrayList<>(),
            "Attempt to dodge enemy attacks during the dodge turn.",
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

        getHostileEncounter().dodgeAction(player);
    }
}
