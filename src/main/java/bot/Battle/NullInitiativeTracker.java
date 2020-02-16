package bot.Battle;

import org.jetbrains.annotations.NotNull;

public class NullInitiativeTracker implements InitiativeTrackerInterface
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void add(@NotNull EncounteredExplorerInterface encounteredExplorer)
    {
        throw InitiativeTrackerException.createEmptyQueue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull EncounteredExplorerInterface getCurrentExplorer() throws InitiativeTrackerException
    {
        throw InitiativeTrackerException.createEmptyQueue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull EncounteredExplorerInterface getNextExplorer() throws InitiativeTrackerException
    {
        throw InitiativeTrackerException.createEmptyQueue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(@NotNull EncounteredExplorerInterface encounteredExplorer)
    {
        throw InitiativeTrackerException.createEmptyQueue();
    }
}
