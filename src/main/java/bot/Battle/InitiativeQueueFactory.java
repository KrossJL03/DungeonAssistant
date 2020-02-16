package bot.Battle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class InitiativeQueueFactory implements InitiativeTrackerFactoryInterface
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull InitiativeTrackerInterface create(@NotNull ArrayList<EncounteredExplorerInterface> explorers)
    {
        return new InitiativeQueue(explorers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull InitiativeTrackerInterface createNull()
    {
        return new NullInitiativeTracker();
    }
}
