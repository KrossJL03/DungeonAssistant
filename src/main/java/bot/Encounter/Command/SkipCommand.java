package bot.Encounter.Command;

import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SkipCommand extends EncounterCommand
{
    /**
     * DodgePassCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    SkipCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "skip",
            new ArrayList<>(),
            "Skip the current player's turn.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        getEncounter().skipCurrentPlayerTurn();
    }
}
