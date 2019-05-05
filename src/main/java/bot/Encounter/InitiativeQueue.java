package bot.Encounter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;

class InitiativeQueue
{

    private LinkedList<EncounteredExplorerInterface> initiative;

    /**
     * InitiativeQueue constructor (empty)
     */
    InitiativeQueue()
    {
        this.initiative = new LinkedList<>();
    }

    /**
     * InitiativeQueue constructor
     *
     * @param encounteredExplorers Encountered explorers
     */
    @NotNull InitiativeQueue(@NotNull ArrayList<EncounteredExplorerInterface> encounteredExplorers)
    {
        this.initiative = new LinkedList<>();
        this.initiative.addAll(encounteredExplorers);
    }

    /**
     * Add encountered explorer
     *
     * @param encounteredExplorer Encountered explorer to add
     */
    void add(@NotNull EncounteredExplorerInterface encounteredExplorer)
    {
        this.initiative.add(encounteredExplorer);
    }

    /**
     * Contains encountered explorer
     *
     * @param encounteredExplorer Encountered explorer to check for
     *
     * @return bool
     */
    boolean contains(EncounteredExplorerInterface encounteredExplorer)
    {
        return this.initiative.contains(encounteredExplorer);
    }

    /**
     * Get current explorer
     *
     * @return EncounteredExplorerInterface
     */
    @Nullable EncounteredExplorerInterface getCurrentExplorer()
    {
        return this.initiative.peek();
    }

    /**
     * Get next explorer
     *
     * @return EncounteredExplorerInterface
     */
    @Nullable EncounteredExplorerInterface getNextExplorer()
    {
        EncounteredExplorerInterface nextExplorer = this.initiative.peek();
        while (nextExplorer != null && (nextExplorer.isSlain() || !nextExplorer.isPresent() || !nextExplorer.hasActions())) {
            this.initiative.pop();
            nextExplorer = this.initiative.peek();
        }
        return nextExplorer;
    }

    /**
     * Remove explorer
     *
     * @param encounteredExplorer Encountered explorer to remove
     */
    void remove(@NotNull EncounteredExplorerInterface encounteredExplorer)
    {
        this.initiative.remove(encounteredExplorer);
    }
}
