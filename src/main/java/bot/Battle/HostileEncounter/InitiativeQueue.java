package bot.Battle.HostileEncounter;

import bot.Battle.EncounteredExplorerInterface;
import bot.Battle.InitiativeTrackerException;
import bot.Battle.InitiativeTrackerInterface;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;

class InitiativeQueue implements InitiativeTrackerInterface
{
    private LinkedList<EncounteredExplorerInterface> queue;

    /**
     * Constructor (empty).
     */
    @NotNull InitiativeQueue()
    {
        queue = new LinkedList<>();
    }

    /**
     * Constructor.
     *
     * @param encounteredExplorers Encountered explorers
     */
    @NotNull InitiativeQueue(@NotNull ArrayList<EncounteredExplorerInterface> encounteredExplorers)
    {
        queue = new LinkedList<>();
        queue.addAll(encounteredExplorers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(@NotNull EncounteredExplorerInterface encounteredExplorer)
    {
        queue.add(encounteredExplorer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull EncounteredExplorerInterface getCurrentExplorer() throws InitiativeTrackerException
    {
        if (queue.isEmpty()) {
            throw InitiativeTrackerException.createEmptyQueue();
        }

        return queue.peek();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull EncounteredExplorerInterface getNextExplorer() throws InitiativeTrackerException
    {
        EncounteredExplorerInterface nextExplorer;
        do {
            queue.pop();
            nextExplorer = queue.peek();
            if (nextExplorer == null) {
                throw InitiativeTrackerException.createEmptyQueue();
            }
        } while (!nextExplorer.isActive() || !nextExplorer.hasActions());

        return nextExplorer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(@NotNull EncounteredExplorerInterface encounteredExplorer)
    {
        queue.remove(encounteredExplorer);
    }
}
