package bot.Encounter.Tier;

import bot.Encounter.EncounteredExplorerInterface;
import bot.Encounter.TierInterface;
import org.jetbrains.annotations.NotNull;

class Tier implements TierInterface
{
    private String name;
    private int    maxStatPointTotal;
    private int    minStatPointTotal;

    /**
     * Tier constructor
     *
     * @param name              Tier name
     * @param minStatPointTotal Min stat point total an explorer must have to fit this tier
     * @param maxStatPointTotal Max stat point total an explorer can have to fit this tier
     *
     * @throws TierException If min stat point total is out of bounds
     *                       If max stat point total is out of bounds
     *                       If max stat point total is less than the min stat point total
     */
    Tier(String name, int minStatPointTotal, int maxStatPointTotal) throws TierException
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
     * {@inheritDoc}
     */
    @Override
    public boolean fits(@NotNull EncounteredExplorerInterface encounteredExplorer)
    {
        int statPoints = encounteredExplorer.getStatPoints();
        return statPoints >= minStatPointTotal && statPoints <= maxStatPointTotal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxStatPointTotal()
    {
        return maxStatPointTotal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinStatPointTotal()
    {
        return minStatPointTotal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getName()
    {
        return name;
    }
}
