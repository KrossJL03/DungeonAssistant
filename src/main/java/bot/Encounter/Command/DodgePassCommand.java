package bot.Encounter.Command;

import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DodgePassCommand extends EncounterCommand
{
    /**
     * DodgePassCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    DodgePassCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "dodgePass",
            new ArrayList<>(),
            "The current character successfully dodges all attacks this round",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        getHostileEncounter().dodgePassAction();
    }
}
