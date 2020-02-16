package bot.Battle;

import bot.Hostile.Hostile;
import org.jetbrains.annotations.NotNull;

public interface EncounteredHostileInterface extends EncounteredCreatureInterface
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
     * Get species
     *
     * @return String
     */
    @NotNull String getSpecies();

    /**
     * Does this hostile have a nickname
     *
     * @return boolean
     */
    boolean hasNickname();

    /**
     * Set name
     *
     * @param name Name
     */
    void setName(@NotNull String name);
}
