package bot.Battle;

import bot.CustomException;
import bot.MyProperties;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ExplorerRoster<Explorer extends CombatExplorer>
{
    final public static int DEFAULT_SIZE = 21;

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
     * @throws CustomException Thrown when encountered explorer is not found
     */
    public @NotNull Explorer getExplorer(@NotNull Player player) throws CustomException
    {
        for (Explorer explorer : explorerRoster) {
            if (explorer.isOwner(player)) {
                return explorer;
            }
        }

        throw new CustomException(String.format(
            "%s I could not find your explorer in this encounter.",
            player.mention()
        ));
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
     * @throws CustomException         If player already has explorer in the encounter
     * @throws ExplorerRosterException If max player count has been reached
     *                                 If explorer does not fit tier
     * @throws CustomException         If the explorers nickname is currently in use
     */
    void addExplorer(@NotNull Explorer newExplorer) throws ExplorerRosterException, CustomException
    {
        if (!isMaxPartySizeSet()) {
            throw new CustomException(String.format(
                "Wait, I don't know how many players to have. DM could you tell me using `%smaxPlayers`?",
                MyProperties.COMMAND_PREFIX
            ));
        }

        Player player = newExplorer.getOwner();

        // exceptions ordered by precedence
        assertPlayerNotKicked(player);
        assertDoesNotContainPlayer(player);
        assertRosterNotFull(player);
        assertFitsTier(newExplorer);
        assertExplorerNameNotTaken(newExplorer);

        explorerRoster.add(newExplorer);
        sort();
    }

    /**
     * Does this roster contain an explorer owned by this player
     *
     * @param player Player
     *
     * @return boolean
     */
    boolean contains(@NotNull Player player)
    {
        for (Explorer explorer : explorerRoster) {
            if (explorer.isOwner(player)) {
                return true;
            }
        }

        return false;
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
        assertPlayerNotKicked(player);

        Explorer encounteredExplorer = getExplorer(player);
        if (!encounteredExplorer.isPresent()) {
            assertRosterNotFull(player);
        }

        encounteredExplorer.markAsPresent();

        return encounteredExplorer;
    }

    /**
     * Set max player count
     *
     * @param maxPartySize Max player count
     *
     * @throws CustomException If new max player count is less than 1
     *                         If present player count exceeds new limit
     */
    void setMaxPartySize(int maxPartySize) throws CustomException
    {
        if (maxPartySize < 1) {
            throw new CustomException("You can't have less than 1 player... that just doesn't work");
        }

        int presentPlayerCount = getCurrentPartySize();
        if (maxPartySize < presentPlayerCount) {
            throw new CustomException(String.format(
                "It looks like we already have %d present players." +
                "If you want to lower the max player count to %d please remove some players first.",
                presentPlayerCount,
                maxPartySize
            ));
        }

        this.maxPartySize = maxPartySize;
    }

    /**
     * Assert the roster does not contain an explorer owned by this player
     *
     * @param player Player
     *
     * @throws CustomException If an explorer owned by this player is in the roster
     */
    private void assertDoesNotContainPlayer(Player player) throws CustomException
    {
        if (containsPlayer(player)) {
            CombatExplorer explorer = getExplorer(player);
            throw new CustomException(String.format(
                "%s, you have already joined this encounter with %s. " +
                "If you'd like to switch please talk to the DungeonMaster",
                player.mention(),
                explorer.getName()
            ));
        }
    }

    /**
     * Assert explorer's name is not already being used by another explorer
     *
     * @param explorer Explorer
     *
     * @throws CustomException If explorer's name is already in use
     */
    private void assertExplorerNameNotTaken(@NotNull Explorer explorer) throws CustomException
    {
        if (containsExplorer(explorer.getName())) {
            throw new CustomException(String.format(
                "%s Someone named '%s' is already in the battle. Could you use a nickname?",
                explorer.getOwner().mention(),
                explorer.getName()
            ));
        }
    }

    /**
     * Assert explorer fits tier
     *
     * @param explorer Explorer
     *
     * @throws CustomException If explorer does not fit tier
     */
    private void assertFitsTier(@NotNull Explorer explorer) throws CustomException
    {
        if (!tier.fits(explorer)) {
            throw new CustomException(String.format(
                "%s, %s does not fit the %s tier.",
                explorer.getOwner().mention(),
                explorer.getName(),
                tier.getName()
            ));
        }
    }

    /**
     * Assert player is not kicked
     *
     * @param player Player
     *
     * @throws CustomException If player is kicked
     */
    private void assertPlayerNotKicked(@NotNull Player player) throws CustomException
    {
        if (kickedPlayers.contains(player)) {
            throw new CustomException(String.format(
                "Sorry %s, you were kicked. Try again next time.",
                player.mention()
            ));
        }
    }

    /**
     * Assert roster is not full
     *
     * @param player Player
     *
     * @throws CustomException If roster is full
     */
    private void assertRosterNotFull(@NotNull Player player) throws CustomException
    {
        if (isFull()) {
            throw new CustomException(String.format(
                "Uh oh, looks like the dungeon is full. Sorry %s.",
                player.mention()
            ));
        }
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
