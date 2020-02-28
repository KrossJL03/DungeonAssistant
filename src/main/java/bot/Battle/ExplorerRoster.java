package bot.Battle;

import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ExplorerRoster<Explorer extends CombatExplorer>
{
    private static int DEFAULT_SIZE = 21;

    private ArrayList<Explorer> explorerRoster;
    private ArrayList<Player>   kickedPlayers;
    private int                 maxPartySize;
    private Tier                tier;

    /**
     * Constructor.
     */
    ExplorerRoster()
    {
        this.explorerRoster = new ArrayList<>();
        this.kickedPlayers = new ArrayList<>();
        this.maxPartySize = DEFAULT_SIZE;
        this.tier = Tier.createDefault();
    }

    /**
     * Get all explorers
     *
     * @return ArrayList<Explorer>
     */
    public @NotNull ArrayList<Explorer> getAllExplorers()
    {
        return new ArrayList<>(explorerRoster);
    }

    /**
     * Get explorer with given name
     *
     * @param name Explorer name
     *
     * @return Explorer
     *
     * @throws ExplorerRosterException Thrown when encountered explorer is not found
     */
    public @NotNull Explorer getExplorer(@NotNull String name) throws ExplorerRosterException
    {
        for (Explorer explorer : explorerRoster) {
            if (explorer.isName(name)) {
                return explorer;
            }
        }

        throw ExplorerRosterException.createExplorerNotFound(name);
    }

    /**
     * Get creature with given name
     *
     * @param player Player
     *
     * @return Explorer
     *
     * @throws ExplorerRosterException Thrown when encountered explorer is not found
     */
    public @NotNull Explorer getExplorer(@NotNull Player player) throws ExplorerRosterException
    {
        for (Explorer explorer : explorerRoster) {
            if (explorer.isOwner(player)) {
                return explorer;
            }
        }

        throw ExplorerRosterException.createExplorerNotFound(player);
    }

    /**
     * Get amount of slots still available for new players
     *
     * @return int
     */
    public int getOpenSlotCount()
    {
        return maxPartySize - getCurrentPartySize();
    }

    /**
     * Get tier
     *
     * @return Tier
     */
    public @NotNull Tier getTier()
    {
        return tier;
    }

    /**
     * Remove explorer belonging to player
     *
     * @param explorer Explorer
     *
     * @throws ExplorerRosterException If explorer is not found
     */
    public void remove(@NotNull Explorer explorer) throws ExplorerRosterException
    {
        if (!containsExplorer(explorer.getName())) {
            throw ExplorerRosterException.createExplorerNotFound(explorer.getName());
        }

        explorerRoster.remove(explorer);
    }

    /**
     * Set tier
     *
     * @param tier Tier
     */
    public void setTier(@NotNull Tier tier)
    {
        this.tier = tier;
    }

    /**
     * Sort roster
     */
    public void sort()
    {
        explorerRoster.sort(new ExplorerInitiativeComparator());
    }

    /**
     * Add explorer to the explorerRoster
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
    void addExplorer(@NotNull Explorer newExplorer) throws ExplorerRosterException
    {
        if (!isMaxPartySizeSet()) {
            throw ExplorerRosterException.createMaxPlayersNotSet();
        }

        Player player = newExplorer.getOwner();

        // exceptions ordered by precedence
        if (kickedPlayers.contains(player)) {
            throw ExplorerRosterException.createKickedPlayerReturns(player);
        } else if (containsPlayer(player)) {
            CombatExplorer explorer = getExplorer(player);
            throw ExplorerRosterException.createMultipleExplorers(player, explorer.getName());
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
     * @return ArrayList<Explorer>
     */
    @NotNull ArrayList<Explorer> getActiveExplorers()
    {
        ArrayList<Explorer> activeExplorers = new ArrayList<>();
        for (Explorer explorer : explorerRoster) {
            if (explorer.isActive()) {
                activeExplorers.add(explorer);
            }
        }

        return activeExplorers;
    }

    /**
     * Get current party size
     *
     * @return int
     */
    int getCurrentPartySize()
    {
        return getPresentExplorers().size();
    }

    /**
     * Get max player count
     *
     * @return int
     */
    int getMaxPartySize()
    {
        return maxPartySize;
    }

    /**
     * Does this roster have 1 or more active explorers
     *
     * @return bool
     */
    boolean hasAtLeastOneActiveExplorer()
    {
        return getActivePlayerCount() > 0;
    }

    /**
     * Does this roster have multiple active explorers
     *
     * @return bool
     */
    boolean hasMultipleActiveExplorers()
    {
        return getActivePlayerCount() > 1;
    }

    /**
     * Is roster full
     *
     * @return bool
     */
    boolean isFull()
    {
        return getCurrentPartySize() >= maxPartySize;
    }

    /**
     * Kick explorer with the given name
     *
     * @param name Name of explorer to kick
     *
     * @throws ExplorerRosterException If explorer is not in the roster
     */
    @NotNull Explorer kick(@NotNull String name) throws ExplorerRosterException
    {
        Explorer explorer = getExplorer(name);

        if (!explorerRoster.contains(explorer)) {
            throw ExplorerRosterException.createExplorerNotFound(explorer.getName());
        }

        explorer.leave();
        explorerRoster.remove(explorer);
        kickedPlayers.add(explorer.getOwner());

        return explorer;
    }

    /**
     * Mark encountered explorer belonging to player as not present
     *
     * @param player Player
     *
     * @return Explorer
     *
     * @throws ExplorerRosterException If encountered explorer has already left
     */
    @NotNull Explorer markAsLeft(@NotNull Player player) throws ExplorerRosterException
    {
        Explorer explorer = getExplorer(player);
        explorer.leave();

        return explorer;
    }

    /**
     * Mark explorer belonging to player as present
     *
     * @param player Player
     *
     * @return Explorer
     *
     * @throws ExplorerRosterException If encountered explorer is present or roster is full
     */
    @NotNull Explorer markAsReturned(@NotNull Player player) throws ExplorerRosterException
    {
        if (kickedPlayers.contains(player)) {
            throw ExplorerRosterException.createKickedPlayerReturns(player);
        }

        Explorer encounteredExplorer = getExplorer(player);
        if (!encounteredExplorer.isPresent() && isFull()) {
            throw ExplorerRosterException.createFullRoster(player);
        }

        encounteredExplorer.markAsPresent();

        return encounteredExplorer;
    }

    /**
     * Set max player count
     *
     * @param maxPartySize Max player count
     *
     * @throws ExplorerRosterException If new max player count is less than 1
     *                                 If present player count exceeds new limit
     */
    void setMaxPartySize(int maxPartySize) throws ExplorerRosterException
    {
        if (maxPartySize < 1) {
            throw ExplorerRosterException.createMaxPlayerCountLessThanOne();
        }

        int presentPlayerCount = getCurrentPartySize();
        if (maxPartySize < presentPlayerCount) {
            throw ExplorerRosterException.createNewPlayerMaxLessThanCurrentPlayerCount(
                maxPartySize,
                presentPlayerCount
            );
        }

        this.maxPartySize = maxPartySize;
    }

    /**
     * Is explorer with given name currently in the roster
     *
     * @param name Explorer name
     *
     * @return bool
     */
    private boolean containsExplorer(@NotNull String name)
    {
        for (Explorer explorer : explorerRoster) {
            if (explorer.isName(name)) {
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
        for (Explorer explorer : explorerRoster) {
            if (explorer.isOwner(player)) {
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
        return getActiveExplorers().size();
    }

    /**
     * Get present explorers
     *
     * @return ArrayList<Explorer>
     */
    private @NotNull ArrayList<Explorer> getPresentExplorers()
    {
        ArrayList<Explorer> presentExplorers = new ArrayList<>();
        for (Explorer explorer : explorerRoster) {
            if (explorer.isPresent()) {
                presentExplorers.add(explorer);
            }
        }

        return presentExplorers;
    }

    /**
     * Has the max player count been set
     *
     * @return boolean
     */
    private boolean isMaxPartySizeSet()
    {
        return maxPartySize > 0;
    }
}
