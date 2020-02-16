package bot.Battle;

import com.google.common.collect.Iterables;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public class InitiativeCycle implements InitiativeTrackerInterface
{
    private EncounteredExplorerInterface           currentExplorer;
    private Iterator<EncounteredExplorerInterface> cycle;

    /**
     * Constructor.
     *
     * @param explorers Encountered explorers
     */
    @NotNull InitiativeCycle(@NotNull ArrayList<EncounteredExplorerInterface> explorers)
    {
        cycle = Iterables.cycle(explorers).iterator();
        currentExplorer = cycle.next();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(@NotNull EncounteredExplorerInterface explorer)
    {
        throw InitiativeTrackerException.createNotSupported();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull EncounteredExplorerInterface getCurrentExplorer() throws InitiativeTrackerException
    {
        return currentExplorer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull EncounteredExplorerInterface getNextExplorer() throws InitiativeTrackerException
    {
        do {
            currentExplorer = cycle.next();
        } while (!currentExplorer.isActive());

        currentExplorer.resetActions(false);

        return currentExplorer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(@NotNull EncounteredExplorerInterface explorer)
    {
        throw InitiativeTrackerException.createNotSupported();
    }
}
