package bot.Battle.HostileEncounter;

import bot.Battle.CombatExplorer;
import bot.Battle.InitiativeTrackerException;
import bot.Battle.InitiativeTrackerInterface;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;

class InitiativeQueue implements InitiativeTrackerInterface
{
    private LinkedList<CombatExplorer> queue;

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
     * @param explorers Explorers
     */
    @NotNull InitiativeQueue(@NotNull ArrayList<CombatExplorer> explorers)
    {
        queue = new LinkedList<>();
        queue.addAll(explorers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(@NotNull CombatExplorer explorer)
    {
        queue.add(explorer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CombatExplorer getCurrentExplorer() throws InitiativeTrackerException
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
    public @NotNull CombatExplorer getNextExplorer() throws InitiativeTrackerException
    {
        CombatExplorer nextExplorer;
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
    public void remove(@NotNull CombatExplorer explorer)
    {
        queue.remove(explorer);
    }
}
