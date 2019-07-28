package bot.Encounter.Command;

import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.Encounter.Logger.Mention;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CreateHostileEncounterCommand extends EncounterCommand
{
    private EncounterHolder holder;

    /**
     * AttackCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    CreateHostileEncounterCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "create encounter",
            new ArrayList<>(),
            "Begin creating a new encounter.",
            true
        );
        this.holder = holder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        EncounterLogger logger = getLogger();
        logger.setChannel(event.getChannel());
        logger.setDmMention(Mention.createForRole(getDungeonMaster(event).getId()));
        holder.createHostileEncounter();
    }
}
