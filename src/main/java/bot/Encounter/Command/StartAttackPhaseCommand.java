package bot.Encounter.Command;

import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StartAttackPhaseCommand extends EncounterCommand
{
    /**
     * StartAttackPhaseCommand constructor
     *
     * @param processManager Process manager
     * @param holder         Encounter holder
     * @param logger         Encounter logger
     */
    StartAttackPhaseCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull EncounterLogger logger
    )
    {
        super(
            processManager,
            holder,
            logger,
            "attackTurn",
            new ArrayList<>(),
            "Start the attack turn.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        getEncounter().startAttackPhase();
    }
}
