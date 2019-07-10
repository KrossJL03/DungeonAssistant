package bot.Encounter;

import org.jetbrains.annotations.NotNull;

public interface TierInterface
{
    /**
     * Does given encountered explorer fit this tier
     *
     * @param encounteredExplorer Encountered explorer
     *
     * @return boolean
     */
    boolean fits(@NotNull EncounteredExplorerInterface encounteredExplorer);

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
