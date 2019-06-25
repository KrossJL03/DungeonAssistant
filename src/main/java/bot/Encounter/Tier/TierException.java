package bot.Encounter.Tier;

import bot.CustomExceptionInterface;
import org.jetbrains.annotations.NotNull;

class TierException extends RuntimeException implements CustomExceptionInterface
{
    /**
     * TierException constructor
     *
     * @param message Message
     */
    private TierException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory for "max less than min"
     *
     * @param maxStatPointTotal Max stat point total
     * @param minStatPointTotal Min stat point total
     *
     * @return TierException
     */
    static TierException createMaxLessThanMin(int maxStatPointTotal, int minStatPointTotal)
    {
        return new TierException(String.format(
            "The max stat point must be greater than the min, and %d isn't greater than %d!",
            maxStatPointTotal,
            minStatPointTotal
        ));
    }

    /**
     * Factory for "max out of bounds"
     *
     * @param statPointTotalMax Maximum stat point total
     * @param outOfBoundsMax    Out of bounds max stat point total
     *
     * @return TierException
     */
    static TierException createMaxOutOfBounds(int statPointTotalMax, int outOfBoundsMax)
    {
        return new TierException(String.format(
            "The min stat point total must be less than %d, and %d is more!",
            statPointTotalMax,
            outOfBoundsMax
        ));
    }

    /**
     * Factory for "min out of bounds"
     *
     * @param statPointTotalMin Minimum stat point total
     * @param outOfBoundsMin    Out of bounds min stat point total
     *
     * @return TierException
     */
    static TierException createMinOutOfBounds(int statPointTotalMin, int outOfBoundsMin)
    {
        return new TierException(String.format(
            "The min stat point total must be greater than %d, and %d is less!",
            statPointTotalMin,
            outOfBoundsMin
        ));
    }

    /**
     * Factory for "not found"
     *
     * @param name Name of tier that was not found
     *
     * @return TierException
     */
    static @NotNull TierException createNotFound(@NotNull String name)
    {
        return new TierException(String.format("I'm not familiar with the %s tier...", name));
    }
}
