package bot.Battle;

import org.jetbrains.annotations.NotNull;

public class NullInitiativeTracker implements InitiativeTrackerInterface
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void add(@NotNull CombatExplorer explorer)
    {
        throw InitiativeTrackerException.createEmptyQueue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CombatExplorer getCurrentExplorer() throws InitiativeTrackerException
    {
        throw InitiativeTrackerException.createEmptyQueue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CombatExplorer getNextExplorer() throws InitiativeTrackerException
    {
        throw InitiativeTrackerException.createEmptyQueue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(@NotNull CombatExplorer explorer)
    {
        throw InitiativeTrackerException.createEmptyQueue();
    }
}
