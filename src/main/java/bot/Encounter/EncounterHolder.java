package bot.Encounter;

import bot.Encounter.Logger.EncounterLogger;
import bot.Encounter.Logger.Mention;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

public class EncounterHolder
{
    private EncounterInterface encounter;
    private EncounterLogger    logger;

    /**
     * EncounterHolder constructor
     *
     * @param logger Encounter logger
     */
    public EncounterHolder(EncounterLogger logger)
    {
        this.encounter = new NullEncounter();
        this.logger = logger;
    }

    /**
     * Get encounter
     *
     * @return EncounterInterface
     */
    public @NotNull EncounterInterface getEncounter()
    {
        return encounter;
    }

    /**
     * Create hostile encounter
     *
     * @param channel Channel to set for logger
     * @param dmId    Dungeon master id
     */
    public void createHostileEncounter(
        @NotNull MessageChannel channel,
        @NotNull String dmId
    )
    {
        logger.setChannel(channel);
        logger.setDmMention(Mention.createForRole(dmId));
        encounter = new Encounter(new ActionListener(logger));
    }
}
