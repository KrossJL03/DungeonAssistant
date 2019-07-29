package bot.Encounter.Command;

import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StartDodgePhaseCommand extends EncounterCommand
{
    /**
     * StartDodgePhaseCommand constructor
     *
     * @param processManager Process manager
     * @param holder         Encounter holder
     * @param logger         Encounter logger
     */
    StartDodgePhaseCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull EncounterLogger logger
    )
    {
        super(
            processManager,
            holder,
            logger,
            "dodgeTurn",
            new ArrayList<>(),
            "Start dodge turn.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        getHostileEncounter().startDodgePhase();
    }
}
