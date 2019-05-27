package bot.Encounter;

import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class ExplorerRoster
{
    private ArrayList<EncounteredExplorerInterface> explorerRoster;
    private int                                     maxPlayerCount;

    /**
     * ExplorerRosterTest constructor
     */
    ExplorerRoster()
    {
        this.maxPlayerCount = 0;
        this.explorerRoster = new ArrayList<>();
    }

    /**
     * Add EncounteredExplorerInterface to the explorerRoster
     * todo consider building EncounteredExplorerInterface in roster
     *
     * @param newExplorer Explorer to add
     *
     * @throws ExplorerRosterException If no max player count has been set
     *                                 If player already has explorer in the encounter
     *                                 If max player count has been reached
     */
    void addExplorer(@NotNull EncounteredExplorerInterface newExplorer) throws ExplorerRosterException
    {
        if (!isMaxPlayerCountSet()) {
            throw ExplorerRosterException.createMaxPlayersNotSet();
        }

        Player player = newExplorer.getOwner();
        if (containsPlayer(player)) {
            EncounteredExplorerInterface character = getExplorer(player);
            throw ExplorerRosterException.createMultipleExplorers(player, character.getName());
        } else if (isFull()) {
            // this error is thrown last because multiple explorers exception takes precedence
            throw ExplorerRosterException.createFullRoster(player);
        }
        explorerRoster.add(newExplorer);
        sort();
    }

    /**
     * Is encountered explorer with given name currently in the roster
     *
     * @param name Explorer name
     *
     * @return bool
     */
    boolean containsExplorer(@NotNull String name)
    {
        for (EncounteredExplorerInterface encounteredExplorer : this.explorerRoster) {
            if (encounteredExplorer.isName(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get active explorers
     *
     * @return ArrayList<EncounteredExplorerInterface>
     */
    @NotNull ArrayList<EncounteredExplorerInterface> getActiveExplorers()
    {
        ArrayList<EncounteredExplorerInterface> activeExplorers = new ArrayList<>();
        for (EncounteredExplorerInterface encounteredExplorer : this.explorerRoster) {
            if (encounteredExplorer.isActive()) {
                activeExplorers.add(encounteredExplorer);
            }
        }
        return activeExplorers;
    }

    /**
     * Get active explorers
     *
     * @return ArrayList<EncounteredExplorerInterface>
     */
    @NotNull ArrayList<EncounteredExplorerInterface> getAliveExplores()
    {
        ArrayList<EncounteredExplorerInterface> aliveExplorers = new ArrayList<>();
        for (EncounteredExplorerInterface encounteredExplorer : this.explorerRoster) {
            if (!encounteredExplorer.isSlain()) {
                aliveExplorers.add(encounteredExplorer);
            }
        }
        return aliveExplorers;
    }

    /**
     * Get all explorers
     *
     * @return ArrayList<EncounteredExplorerInterface>
     */
    @NotNull ArrayList<EncounteredExplorerInterface> getAllExplorers()
    {
        return new ArrayList<>(explorerRoster);
    }

    /**
     * Get encountered explorer with given name
     *
     * @param name Explorer name
     *
     * @return EncounterCreatureInterface
     *
     * @throws EncounteredCreatureNotFoundException Thrown when encountered explorer is not found
     */
    @NotNull EncounteredExplorerInterface getExplorer(@NotNull String name) throws EncounteredCreatureNotFoundException
    {
        for (EncounteredExplorerInterface encounteredExplorer : explorerRoster) {
            if (encounteredExplorer.isName(name)) {
                return encounteredExplorer;
            }
        }
        throw EncounteredCreatureNotFoundException.createForExplorer(name);
    }

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
        throws EncounteredCreatureNotFoundException
    {
        for (EncounteredExplorerInterface encounteredExplorer : this.explorerRoster) {
            if (encounteredExplorer.isOwner(player)) {
                return encounteredExplorer;
            }
        }
        throw EncounteredCreatureNotFoundException.createForExplorer(player);
    }

    /**
     * Get max player count
     *
     * @return int
     */
    int getMaxPlayerCount()
    {
        return maxPlayerCount;
    }

    /**
     * Get amount of slots still available for new players
     *
     * @return int
     */
    int getOpenSlotCount()
    {
        return maxPlayerCount - getPresentPlayerCount();
    }

    /**
     * Does this roster have active explorers
     *
     * @return bool
     */
    boolean hasActiveExplorers()
    {
        return this.getActivePlayerCount() > 0;
    }

    /**
     * Is roster full
     *
     * @return bool
     */
    boolean isFull()
    {
        return getPresentPlayerCount() >= maxPlayerCount;
    }

    /**
     * Mark encountered explorer belonging to player as not present
     *
     * @param player Player
     *
     * @return EncounteredExplorerInterface
     *
     * @throws ExplorerRosterException If encountered explorer has already left
     */
    @NotNull EncounteredExplorerInterface leave(@NotNull Player player) throws ExplorerRosterException
    {
        // todo rename method
        EncounteredExplorerInterface encounteredExplorer = getExplorer(player);
        if (!encounteredExplorer.isPresent()) {
            throw ExplorerRosterException.createHasAleadyLeft(player);
        }
        encounteredExplorer.leave();
        return encounteredExplorer;
    }

    /**
     * Mark encountered explorer belonging to player as present
     *
     * @param player Player
     *
     * @return EncounteredExplorerInterface
     *
     * @throws ExplorerRosterException If encountered explorer is present or roster is full
     */
    @NotNull EncounteredExplorerInterface rejoin(@NotNull Player player) throws ExplorerRosterException
    {
        // todo rename method
        EncounteredExplorerInterface encounteredExplorer = this.getExplorer(player);
        if (encounteredExplorer.isPresent()) {
            throw ExplorerRosterException.createCannotRejoinIfPresent(player);
        } else if (this.isFull()) {
            // this error is thrown last because already present message takes precedence
            throw ExplorerRosterException.createFullRoster(player);
        }
        encounteredExplorer.rejoin();
        return encounteredExplorer;
    }

    /**
     * Remove encountered explorer belonging to player
     *
     * @param encounteredExplorer Encountered explorer
     *
     * @throws EncounteredCreatureNotFoundException If encountered explorer is not found
     */
    void remove(@NotNull EncounteredExplorerInterface encounteredExplorer) throws EncounteredCreatureNotFoundException
    {
        if (!this.containsExplorer(encounteredExplorer.getName())) {
            throw EncounteredCreatureNotFoundException.createForExplorer(encounteredExplorer.getName());
        }
        this.explorerRoster.remove(encounteredExplorer);
    }

    /**
     * Set max player count
     *
     * @param maxPlayerCount Max player count
     *
     * @throws ExplorerRosterException If present player count exceeds new limit
     */
    void setMaxPlayerCount(int maxPlayerCount) throws ExplorerRosterException
    {
        if (maxPlayerCount < 1) {
            throw ExplorerRosterException.createMaxPlayerCountLessThanOne();
        }
        int presentPlayerCount = this.getPresentPlayerCount();
        if (maxPlayerCount < presentPlayerCount) {
            throw ExplorerRosterException.createNewPlayerMaxLessThanCurrentPlayerCount(
                maxPlayerCount,
                presentPlayerCount
            );
        }
        this.maxPlayerCount = maxPlayerCount;
    }

    /**
     * Sort roster
     */
    void sort()
    {
        this.explorerRoster.sort(new ExplorerAgilityComparator());
    }

    /**
     * Does player have a character in the roster
     *
     * @param player Player
     *
     * @return bool
     */
    private boolean containsPlayer(@NotNull Player player)
    {
        for (EncounteredExplorerInterface encounteredExplorer : this.explorerRoster) {
            if (encounteredExplorer.isOwner(player)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get number of active players
     *
     * @return int
     */
    private int getActivePlayerCount()
    {
        return this.getActiveExplorers().size();
    }

    /**
     * Get present explorers
     *
     * @return ArrayList<EncounteredExplorerInterface>
     */
    private @NotNull ArrayList<EncounteredExplorerInterface> getPresentExplorers()
    {
        ArrayList<EncounteredExplorerInterface> presentExplorers = new ArrayList<>();
        for (EncounteredExplorerInterface encounteredExplorer : this.explorerRoster) {
            if (encounteredExplorer.isPresent()) {
                presentExplorers.add(encounteredExplorer);
            }
        }
        return presentExplorers;
    }

    /**
     * Get number of present players
     *
     * @return int
     */
    private int getPresentPlayerCount()
    {
        return this.getPresentExplorers().size();
    }

    /**
     * Has the max player count been set
     *
     * @return boolean
     */
    private boolean isMaxPlayerCountSet()
    {
        return this.maxPlayerCount > 0;
    }
}
