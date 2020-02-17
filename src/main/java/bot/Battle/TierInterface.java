package bot.Battle;

import org.jetbrains.annotations.NotNull;

public interface TierInterface
{
    int STAT_POINT_TOTAL_MAXIMUM = 100;
    int STAT_POINT_TOTAL_MINIMUM = 0;

    /**
     * Does given explorer fit this tier
     *
     * @param explorer Explorer
     *
     * @return boolean
     */
    boolean fits(@NotNull CombatExplorer explorer);

    /**
     * Get max stat points
     *
     * @return int
     */
    int getMaxStatPointTotal();

    /**
     * Get min stat points
     *
     * @return int
     */
    int getMinStatPointTotal();

    /**
     * Get name
     *
     * @return String
     */
    @NotNull String getName();
}
