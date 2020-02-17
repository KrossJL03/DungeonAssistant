package bot.Battle;

import bot.CustomException;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface ExplorerRosterInterface
{
    /**
     * Add EncounteredExplorerInterface to the explorerRoster
     *
     * @param newExplorer Explorer to add
     *
     * @throws CustomException If no max player count has been set
     *                         If player has been kicked
     *                         If player already has explorer in the encounter
     *                         If max player count has been reached
     *                         If explorer does not fit tier
     *                         If the explorers nickname is currently in use
     */
    void addExplorer(@NotNull EncounteredExplorerInterface newExplorer) throws CustomException;

    /**
     * Get active explorers
     *
     * @return ArrayList<EncounteredExplorerInterface>
     */
    @NotNull ArrayList<EncounteredExplorerInterface> getActiveExplorers();

    /**
     * Get all explorers
     *
     * @return ArrayList<EncounteredExplorerInterface>
     */
    @NotNull ArrayList<EncounteredExplorerInterface> getAllExplorers();

    /**
     * Get encountered explorer with given name
     *
     * @param name Explorer name
     *
     * @return EncounterCreatureInterface
     *
     * @throws EncounteredCreatureNotFoundException Thrown when encountered explorer is not found
     */
    @NotNull EncounteredExplorerInterface getExplorer(@NotNull String name) throws EncounteredCreatureNotFoundException;

    /**
     * Get encountered creature with given name
     *
     * @param player Player
     *
     * @return EncounterCreatureInterface
     *
     * @throws EncounteredCreatureNotFoundException Thrown when encountered explorer is not found
     */
    @NotNull EncounteredExplorerInterface getExplorer(@NotNull Player player)
        throws EncounteredCreatureNotFoundException;

    /**
     * Get max player count
     *
     * @return int
     */
    int getMaxPlayerCount();

    /**
     * Get amount of slots still available for new players
     *
     * @return int
     */
    int getOpenSlotCount();

    /**
     * Get tier
     *
     * @return TierInterface
     */
    @NotNull TierInterface getTier();

    /**
     * Does this roster have 1 or more active explorers
     *
     * @return bool
     */
    boolean hasAtLeastOneActiveExplorer();

    /**
     * Does this roster have multiple active explorers
     *
     * @return bool
     */
    boolean hasMultipleActiveExplorers();

    /**
     * Is roster full
     *
     * @return bool
     */
    boolean isFull();

    /**
     * Kick explorer with the given name
     *
     * @param name Name of explorer to kick
     *
     * @throws EncounteredCreatureNotFoundException If explorer is not in the roster
     */
    @NotNull EncounteredExplorerInterface kick(@NotNull String name) throws EncounteredCreatureNotFoundException;

    /**
     * Mark encountered explorer belonging to player as not present
     *
     * @param player Player
     *
     * @return EncounteredExplorerInterface
     *
     * @throws CustomException If encountered explorer has already left
     */
    @NotNull EncounteredExplorerInterface markAsLeft(@NotNull Player player) throws CustomException;

    /**
     * Mark encountered explorer belonging to player as present
     *
     * @param player Player
     *
     * @return EncounteredExplorerInterface
     *
     * @throws CustomException If encountered explorer is present or roster is full
     */
    @NotNull EncounteredExplorerInterface markAsReturned(@NotNull Player player) throws CustomException;

    /**
     * Remove encountered explorer belonging to player
     *
     * @param encounteredExplorer Encountered explorer
     *
     * @throws EncounteredCreatureNotFoundException If encountered explorer is not found
     */
    void remove(@NotNull EncounteredExplorerInterface encounteredExplorer) throws EncounteredCreatureNotFoundException;

    /**
     * Set max player count
     *
     * @param maxPlayerCount Max player count
     *
     * @throws CustomException If present player count exceeds new limit
     */
    void setMaxPlayerCount(int maxPlayerCount) throws CustomException;

    /**
     * Set tier
     *
     * @param tier Tier
     */
    void setTier(@NotNull TierInterface tier);

    /**
     * Sort roster
     */
    void sort();
}
