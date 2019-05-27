package bot.Encounter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;

class InitiativeQueue
{
    private LinkedList<EncounteredExplorerInterface> queue;

    /**
     * InitiativeQueue constructor (empty)
     */
    @NotNull InitiativeQueue()
    {
        queue = new LinkedList<>();
    }

    /**
     * InitiativeQueue constructor
     *
     * @param encounteredExplorers Encountered explorers
     */
    @NotNull InitiativeQueue(@NotNull ArrayList<EncounteredExplorerInterface> encounteredExplorers)
    {
        queue = new LinkedList<>();
        queue.addAll(encounteredExplorers);
    }

    /**
     * Add encountered explorer
     *
     * @param encounteredExplorer Encountered explorer to add
     */
    void add(@NotNull EncounteredExplorerInterface encounteredExplorer)
    {
        queue.add(encounteredExplorer);
    }

    /**
     * Get current explorer
     *
     * @return EncounteredExplorerInterface
     *
     * @throws InitiativeQueueException If the queue is empty
     */
    @NotNull EncounteredExplorerInterface getCurrentExplorer() throws InitiativeQueueException
    {
        if (queue.isEmpty()) {
            throw InitiativeQueueException.createEmptyQueue();
        }
        return queue.peek();
    }

    /**
     * Get next explorer
     *
     * @return EncounteredExplorerInterface
     *
     * @throws InitiativeQueueException If no next explorer exists
     */
    @NotNull EncounteredExplorerInterface getNextExplorer() throws InitiativeQueueException
    {
        EncounteredExplorerInterface nextExplorer;
        do {
            queue.pop();
            nextExplorer = queue.peek();
            if (nextExplorer == null) {
                throw InitiativeQueueException.createEmptyQueue();
            }
        } while (!nextExplorer.isActive() || !nextExplorer.hasActions());

        return nextExplorer;
    }

    /**
     * Remove explorer
     *
     * @param encounteredExplorer Encountered explorer to remove
     */
    void remove(@NotNull EncounteredExplorerInterface encounteredExplorer)
    {
        queue.remove(encounteredExplorer);
    }
}
