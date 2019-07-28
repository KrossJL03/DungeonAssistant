package bot.Encounter;

import org.jetbrains.annotations.NotNull;

public interface EncounterHolderInterface
{
    /**
     * Create hostile encounter
     */
    void createHostileEncounter();

    /**
     * Get encounter
     *
     * @return EncounterInterface
     */
    @NotNull EncounterInterface getEncounter();

    /**
     * Is an active encounter currently in progress
     *
     * @return bool
     */
    boolean hasActiveEncounter();
}
