package bot.Battle;

import bot.Battle.Logger.EncounterLogger;
import bot.Battle.Logger.Mention;
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
        encounter = new HostileEncounter(logger);
    }

    /**
     * Create pvp
     *
     * @param channel Channel to set for logger
     * @param dmId    Dungeon master id
     */
    public void createPvp(
        @NotNull MessageChannel channel,
        @NotNull String dmId
    )
    {
        logger.setChannel(channel);
        logger.setDmMention(Mention.createForRole(dmId));
        encounter = new PlayerVsPlayer(logger);
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
}
