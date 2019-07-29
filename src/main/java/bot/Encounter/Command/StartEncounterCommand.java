package bot.Encounter.Command;

import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.Encounter.Logger.Mention;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StartEncounterCommand extends EncounterCommand
{
    /**
     * StartEncounterCommand constructor
     *
     * @param processManager Process manager
     * @param holder         Encounter holder
     * @param logger         Encounter logger
     */
    StartEncounterCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull EncounterLogger logger
    )
    {
        super(
            processManager,
            holder,
            logger,
            "start",
            new ArrayList<>(),
            "Start the encounter.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        EncounterLogger logger = getLogger();
        logger.setChannel(event.getChannel());
        logger.setEveryoneMention(Mention.createForRole(event.getGuild().getPublicRole().getId()));
        getEncounter().startJoinPhase();
    }
}
