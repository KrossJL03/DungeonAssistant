package bot.Encounter;

import org.jetbrains.annotations.NotNull;

public class EncounterHolder
{
    private EncounterInterface encounter;
    private Encounter          hostileEncounter; // todo remove

    /**
     * EncounterHolder constructor
     */
    public EncounterHolder()
    {
        encounter = new NullEncounter();
        hostileEncounter = new Encounter();
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
    void createHostileEncounter()
    {
        Encounter newEncounter = new Encounter();
        encounter = newEncounter;
        hostileEncounter = newEncounter;
    }

    /**
     * Get hostile encounter
     *
     * @return EncounterInterface
     */
    @NotNull Encounter getHostileEncounter()
    {
        // todo remove method
        return hostileEncounter;
    }
}
