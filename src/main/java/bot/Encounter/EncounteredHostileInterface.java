package bot.Encounter;

import bot.Hostile.Hostile;
import bot.Hostile.Loot;
import org.jetbrains.annotations.NotNull;

public interface EncounteredHostileInterface extends EncounterCreatureInterface
{
    /**
     * Attack
     */
    void attack();

    /**
     * Get attack roll
     *
     * @return int
     */
    int getAttackRoll();

    /**
     * Get hostile
     *
     * @return Hostile
     */
    @NotNull Hostile getHostile();

    /**
     * Get loot
     *
     * @param roll Loot roll
     *
     * @return Loot
     */
    @NotNull Loot getLoot(int roll);

    /**
     * Get species
     *
     * @return String
     */
    @NotNull String getSpecies();

    /**
     * Set name
     *
     * @param name Name
     */
    void setName(@NotNull String name);
}
