package bot.Battle;

import org.jetbrains.annotations.NotNull;

public class Tier
{
    private static int STAT_POINT_TOTAL_MAXIMUM = 100;
    private static int STAT_POINT_TOTAL_MINIMUM = 0;

    private int    maxStatPointTotal;
    private int    minStatPointTotal;
    private String name;

    /**
     * Constructor.
     *
     * @param name              Tier name
     * @param minStatPointTotal Min stat point total an explorer must have to fit this tier
     * @param maxStatPointTotal Max stat point total an explorer can have to fit this tier
     *
     * @throws TierException If min stat point total is out of bounds
     *                       If max stat point total is out of bounds
     *                       If max stat point total is less than the min stat point total
     */
    Tier(@NotNull String name, int minStatPointTotal, int maxStatPointTotal) throws TierException
    {
        if (minStatPointTotal < STAT_POINT_TOTAL_MINIMUM) {
            throw TierException.createMinOutOfBounds(STAT_POINT_TOTAL_MINIMUM, minStatPointTotal);
        } else if (maxStatPointTotal > STAT_POINT_TOTAL_MAXIMUM) {
            throw TierException.createMaxOutOfBounds(STAT_POINT_TOTAL_MAXIMUM, maxStatPointTotal);
        } else if (maxStatPointTotal <= minStatPointTotal) {
            throw TierException.createMaxLessThanMin(maxStatPointTotal, minStatPointTotal);
        }

        this.maxStatPointTotal = maxStatPointTotal;
        this.minStatPointTotal = minStatPointTotal;
        this.name = name;
    }

    /**
     * Create default tier
     *
     * @return Tier
     */
    static @NotNull Tier createDefault()
    {
        return new Tier("All", STAT_POINT_TOTAL_MINIMUM, STAT_POINT_TOTAL_MAXIMUM);
    }

    /**
     * Get name
     *
     * @return String
     */
    public @NotNull String getName()
    {
        return name;
    }

    /**
     * Does given explorer fit this tier
     *
     * @param explorer Explorer
     *
     * @return boolean
     */
    boolean fits(@NotNull CombatExplorer explorer)
    {
        int statPoints = explorer.getStatPoints();
        return statPoints >= minStatPointTotal && statPoints <= maxStatPointTotal;
    }

    /**
     * Get max stat points
     *
     * @return int
     */
    int getMaxStatPointTotal()
    {
        return maxStatPointTotal;
    }

    /**
     * Get min stat points
     *
     * @return int
     */
    int getMinStatPointTotal()
    {
        return minStatPointTotal;
    }
}
