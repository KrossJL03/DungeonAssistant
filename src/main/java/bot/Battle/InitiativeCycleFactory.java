package bot.Battle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class InitiativeCycleFactory implements InitiativeTrackerFactoryInterface
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull InitiativeTrackerInterface create(@NotNull ArrayList<EncounteredExplorerInterface> explorers)
    {
        return new InitiativeCycle(explorers);
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
