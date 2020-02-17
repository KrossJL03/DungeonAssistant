package bot.Battle;

import org.jetbrains.annotations.NotNull;

public interface InitiativeTrackerInterface
{
    /**
     * Add explorer
     *
     * @param explorer Encountered explorer to add
     */
    void add(@NotNull CombatExplorer explorer);

    /**
     * Get current explorer
     *
     * @return EncounteredExplorer
     *
     * @throws InitiativeTrackerException If the queue is empty
     */
    @NotNull CombatExplorer getCurrentExplorer() throws InitiativeTrackerException;

    /**
     * Get next explorer
     *
     * @return EncounteredExplorer
     *
     * @throws InitiativeTrackerException If no next explorer exists
     */
    @NotNull CombatExplorer getNextExplorer() throws InitiativeTrackerException;

    /**
     * Remove explorer
     *
     * @param encounteredExplorer Encountered explorer to remove
     */
    void remove(@NotNull CombatExplorer encounteredExplorer);
}
