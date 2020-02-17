package bot.Battle;

import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ExplorerRoster implements ExplorerRosterInterface
{
    private static int DEFAULT_SIZE = 21;

    private ArrayList<CombatExplorer> explorerRoster;
    private ArrayList<Player>         kickedPlayers;
    private int                       maxPlayerCount;
    private Tier                      tier;

    /**
     * Constructor.
     */
    ExplorerRoster()
    {
        this.explorerRoster = new ArrayList<>();
        this.kickedPlayers = new ArrayList<>();
        this.maxPlayerCount = DEFAULT_SIZE;
        this.tier = Tier.createDefault();
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
    public void addExplorer(@NotNull CombatExplorer newExplorer) throws ExplorerRosterException
    {
        if (!isMaxPlayerCountSet()) {
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
     * @return ArrayList<CombatExplorer>
     */
    public @NotNull ArrayList<CombatExplorer> getActiveExplorers()
    {
        ArrayList<CombatExplorer> activeExplorers = new ArrayList<>();
        for (CombatExplorer explorer : explorerRoster) {
            if (explorer.isActive()) {
                activeExplorers.add(explorer);
            }
        }

        return activeExplorers;
    }

    /**
     * Get all explorers
     *
     * @return ArrayList<CombatExplorer>
     */
    public @NotNull ArrayList<CombatExplorer> getAllExplorers()
    {
        return new ArrayList<>(explorerRoster);
    }

    /**
     * Get explorer with given name
     *
     * @param name Explorer name
     *
     * @return CombatExplorer
     *
     * @throws ExplorerRosterException Thrown when encountered explorer is not found
     */
    public @NotNull CombatExplorer getExplorer(@NotNull String name) throws ExplorerRosterException
    {
        for (CombatExplorer explorer : explorerRoster) {
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
     * @return CombatExplorer
     *
     * @throws ExplorerRosterException Thrown when encountered explorer is not found
     */
    public @NotNull CombatExplorer getExplorer(@NotNull Player player) throws ExplorerRosterException
    {
        for (CombatExplorer explorer : explorerRoster) {
            if (explorer.isOwner(player)) {
                return explorer;
            }
        }

        throw ExplorerRosterException.createExplorerNotFound(player);
    }

    /**
     * Get max player count
     *
     * @return int
     */
    public int getMaxPlayerCount()
    {
        return maxPlayerCount;
    }

    /**
     * Get amount of slots still available for new players
     *
     * @return int
     */
    public int getOpenSlotCount()
    {
        return maxPlayerCount - getPresentPlayerCount();
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
     * Does this roster have 1 or more active explorers
     *
     * @return bool
     */
    public boolean hasAtLeastOneActiveExplorer()
    {
        return getActivePlayerCount() > 0;
    }

    /**
     * Does this roster have multiple active explorers
     *
     * @return bool
     */
    public boolean hasMultipleActiveExplorers()
    {
        return getActivePlayerCount() > 1;
    }

    /**
     * Is roster full
     *
     * @return bool
     */
    public boolean isFull()
    {
        return getPresentPlayerCount() >= maxPlayerCount;
    }

    /**
     * Kick explorer with the given name
     *
     * @param name Name of explorer to kick
     *
     * @throws ExplorerRosterException If explorer is not in the roster
     */
    public @NotNull CombatExplorer kick(@NotNull String name) throws ExplorerRosterException
    {
        CombatExplorer explorer = getExplorer(name);

        if (!explorerRoster.contains(explorer)) {
            throw ExplorerRosterException.createExplorerNotFound(explorer.getName());
        }

        explorer.markAsNotPresent();
        explorerRoster.remove(explorer);
        kickedPlayers.add(explorer.getOwner());

        return explorer;
    }

    /**
     * Mark encountered explorer belonging to player as not present
     *
     * @param player Player
     *
     * @return CombatExplorer
     *
     * @throws ExplorerRosterException If encountered explorer has already left
     */
    public @NotNull CombatExplorer markAsLeft(@NotNull Player player) throws ExplorerRosterException
    {
        CombatExplorer explorer = getExplorer(player);
        explorer.markAsNotPresent();

        return explorer;
    }

    /**
     * Mark explorer belonging to player as present
     *
     * @param player Player
     *
     * @return CombatExplorer
     *
     * @throws ExplorerRosterException If encountered explorer is present or roster is full
     */
    public @NotNull CombatExplorer markAsReturned(@NotNull Player player) throws ExplorerRosterException
    {
        if (kickedPlayers.contains(player)) {
            throw ExplorerRosterException.createKickedPlayerReturns(player);
        }

        CombatExplorer encounteredExplorer = getExplorer(player);
        if (!encounteredExplorer.isPresent() && isFull()) {
            throw ExplorerRosterException.createFullRoster(player);
        }

        encounteredExplorer.markAsPresent();

        return encounteredExplorer;
    }

    /**
     * Remove explorer belonging to player
     *
     * @param explorer Explorer
     *
     * @throws ExplorerRosterException If explorer is not found
     */
    public void remove(@NotNull CombatExplorer explorer) throws ExplorerRosterException
    {
        if (!containsExplorer(explorer.getName())) {
            throw ExplorerRosterException.createExplorerNotFound(explorer.getName());
        }

        explorerRoster.remove(explorer);
    }

    /**
     * Set max player count
     *
     * @param maxPlayerCount Max player count
     *
     * @throws ExplorerRosterException If new max player count is less than 1
     *                                 If present player count exceeds new limit
     */
    public void setMaxPlayerCount(int maxPlayerCount) throws ExplorerRosterException
    {
        if (maxPlayerCount < 1) {
            throw ExplorerRosterException.createMaxPlayerCountLessThanOne();
        }

        int presentPlayerCount = getPresentPlayerCount();
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
    public void setTier(@NotNull Tier tier)
    {
        this.tier = tier;
    }

    /**
     * Sort roster
     */
    public void sort()
    {
        explorerRoster.sort(new ExplorerAgilityComparator());
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
        for (CombatExplorer explorer : explorerRoster) {
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
        for (CombatExplorer explorer : explorerRoster) {
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
     * @return ArrayList<CombatExplorer>
     */
    private @NotNull ArrayList<CombatExplorer> getPresentExplorers()
    {
        ArrayList<CombatExplorer> presentExplorers = new ArrayList<>();
        for (CombatExplorer explorer : explorerRoster) {
            if (explorer.isPresent()) {
                presentExplorers.add(explorer);
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
        return getPresentExplorers().size();
    }

    /**
     * Has the max player count been set
     *
     * @return boolean
     */
    private boolean isMaxPlayerCountSet()
    {
        return maxPlayerCount > 0;
    }
}
