package bot.Encounter;

import bot.Encounter.Tier.DefaultTier;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class ExplorerRoster
{
    private static int DEFAULT_SIZE = 21;

    private ArrayList<EncounteredExplorerInterface> explorerRoster;
    private ArrayList<Player>                       kickedPlayers;
    private int                                     maxPlayerCount;
    private TierInterface                           tier;

    /**
     * ExplorerRosterTest constructor
     */
    ExplorerRoster()
    {
        this.explorerRoster = new ArrayList<>();
        this.kickedPlayers = new ArrayList<>();
        this.maxPlayerCount = DEFAULT_SIZE;
        this.tier = new DefaultTier();
    }

    /**
     * Add EncounteredExplorerInterface to the explorerRoster
     *
     * @param newExplorer Explorer to add
     *
     * @throws ExplorerRosterException If no max player count has been set
     *                                 If player has been kicked
     *                                 If player already has explorer in the encounter
     *                                 If max player count has been reached
     *                                 If explorer does not fit tier
     *                                 If the explorers nickname is currently in use
     */
    void addExplorer(@NotNull EncounteredExplorerInterface newExplorer) throws ExplorerRosterException
    {
        if (!isMaxPlayerCountSet()) {
            throw ExplorerRosterException.createMaxPlayersNotSet();
        }

        Player player = newExplorer.getOwner();

        // exceptions ordered by precedence
        if (kickedPlayers.contains(player)) {
            throw ExplorerRosterException.createKickedPlayerReturns(player);
        } else if (containsPlayer(player)) {
            EncounteredExplorerInterface character = getExplorer(player);
            throw ExplorerRosterException.createMultipleExplorers(player, character.getName());
        } else if (isFull()) {
            throw ExplorerRosterException.createFullRoster(player);
        } else if (!tier.fits(newExplorer)) {
            throw ExplorerRosterException.createDoesNotFitTier(newExplorer, tier);
        } else if (containsExplorer(newExplorer.getName())) {
            throw ExplorerRosterException.createNameTaken(player, newExplorer.getName());
        }

        explorerRoster.add(newExplorer);
        sort();
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
     * Get tier
     *
     * @return TierInterface
     */
    @NotNull TierInterface getTier()
    {
        return tier;
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
     * Kick Player with given encountered explorer
     *
     * @param encounteredExplorer Encountered explorer to kick
     */
    void kick(EncounteredExplorerInterface encounteredExplorer)
    {
        if (!explorerRoster.contains(encounteredExplorer)) {
            throw EncounteredCreatureNotFoundException.createForExplorer(encounteredExplorer.getName());
        }
        explorerRoster.remove(encounteredExplorer);
        kickedPlayers.add(encounteredExplorer.getOwner());
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
        if (kickedPlayers.contains(player)) {
            throw ExplorerRosterException.createKickedPlayerReturns(player);
        }

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
        if (!containsExplorer(encounteredExplorer.getName())) {
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
     * Set tier
     *
     * @param tier Tier
     */
    void setTier(@NotNull TierInterface tier)
    {
        this.tier = tier;
    }

    /**
     * Sort roster
     */
    void sort()
    {
        this.explorerRoster.sort(new ExplorerAgilityComparator());
    }

    /**
     * Is encountered explorer with given name currently in the roster
     *
     * @param name Explorer name
     *
     * @return bool
     */
    private boolean containsExplorer(@NotNull String name)
    {
        for (EncounteredExplorerInterface encounteredExplorer : this.explorerRoster) {
            if (encounteredExplorer.isName(name)) {
                return true;
            }
        }
        return false;
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
