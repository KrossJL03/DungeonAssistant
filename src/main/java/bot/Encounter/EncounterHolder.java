package bot.Encounter;

import bot.Encounter.Logger.EncounterLogger;
import org.jetbrains.annotations.NotNull;

public class EncounterHolder
{
    private EncounterInterface encounter;
    private EncounterLogger    logger;
    private Encounter          hostileEncounter; // todo remove

    /**
     * EncounterHolder constructor
     *
     * @param logger Encounter logger
     */
    public EncounterHolder(EncounterLogger logger)
    {
        this.encounter = new NullEncounter();
        this.hostileEncounter = new Encounter(new ActionListener(logger));
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
     * Is an active encounter currently in progress
     *
     * @return bool
     */
    public boolean hasActiveEncounter()
    {
        return !encounter.isNull() && !encounter.isOver();
    }

    /**
     * Create hostile encounter
     */
    public void createHostileEncounter()
    {
        Encounter newEncounter = new Encounter(new ActionListener(logger));
        encounter = newEncounter;
        hostileEncounter = newEncounter;
    }
}
