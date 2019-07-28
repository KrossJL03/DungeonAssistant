package bot.Encounter.Command;

import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StartAttackPhaseCommand extends EncounterCommand
{
    /**
     * StartAttackPhaseCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    StartAttackPhaseCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
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
