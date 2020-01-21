package bot.Battle.Tier;

import bot.Battle.EncounteredExplorerInterface;
import bot.Battle.TierInterface;
import org.jetbrains.annotations.NotNull;

public class DefaultTier implements TierInterface
{
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean fits(@NotNull EncounteredExplorerInterface encounteredExplorer)
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxStatPointTotal()
    {
        return STAT_POINT_TOTAL_MAXIMUM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinStatPointTotal()
    {
        return STAT_POINT_TOTAL_MINIMUM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getName()
    {
        return "All";
    }
}
