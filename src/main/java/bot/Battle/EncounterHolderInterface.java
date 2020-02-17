package bot.Battle;

import org.jetbrains.annotations.NotNull;

public interface EncounterHolderInterface
{
    /**
     * Create hostile encounter
     */
    void createHostileEncounter();

    /**
     * Get battle
     *
     * @return Battle
     */
    @NotNull Battle getBattle();

    /**
     * Is an active encounter currently in progress
     *
     * @return bool
     */
    boolean hasActiveEncounter();
}
