package bot.Battle;

import org.jetbrains.annotations.NotNull;

public interface InitiativeTrackerInterface
{
    /**
     * Add encountered explorer
     *
     * @param encounteredExplorer Encountered explorer to add
     */
    void add(@NotNull EncounteredExplorerInterface encounteredExplorer);

    /**
     * Get current explorer
     *
     * @return EncounteredExplorerInterface
     *
     * @throws InitiativeTrackerException If the queue is empty
     */
    @NotNull EncounteredExplorerInterface getCurrentExplorer() throws InitiativeTrackerException;

    /**
     * Get next explorer
     *
     * @return EncounteredExplorerInterface
     *
     * @throws InitiativeTrackerException If no next explorer exists
     */
    @NotNull EncounteredExplorerInterface getNextExplorer() throws InitiativeTrackerException;

    /**
     * Remove explorer
     *
     * @param encounteredExplorer Encountered explorer to remove
     */
    void remove(@NotNull EncounteredExplorerInterface encounteredExplorer);
}
