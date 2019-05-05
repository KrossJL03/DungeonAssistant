package bot.Encounter;

import bot.Encounter.EncounteredCreature.EncounteredHostile;
import bot.Encounter.Exception.*;
import bot.Hostile.Hostile;
import bot.Player.Player;
import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Encounter
{
    public static  String ATTACK_PHASE = "ATTACK";
    public static  String DODGE_PHASE  = "DODGE";
    public static  String LOOT_PHASE   = "LOOT";
    private static String CREATE_PHASE = "CREATE";
    private static String END_PHASE    = "END";
    private static String JOIN_PHASE   = "JOIN";
    private static String RP_PHASE     = "RP";

    private ArrayList<EncounteredHostileInterface> hostiles;
    private ExplorerRoster                         explorerRoster;
    private InitiativeQueue                        initiative;
    private String                                 currentPhase;
    private boolean                                hasPhoenixDown;

    /**
     * Encounter constructor
     */
    public Encounter()
    {
        this.currentPhase = Encounter.CREATE_PHASE;
        this.initiative = new InitiativeQueue();
        this.hasPhoenixDown = true;
        this.hostiles = new ArrayList<>();
        this.explorerRoster = new ExplorerRoster();
    }

    /**
     * Add explorer
     *
     * @param newExplorer New explorer
     */
    void addExplorer(@NotNull EncounteredExplorerInterface newExplorer)
    {
        // todo start building EncounteredExplorerInterface here
        this.explorerRoster.addExplorer(newExplorer);
        if (this.isInitiativePhase()) {
            this.initiative.add(newExplorer);
            newExplorer.resetActions(this.isAttackPhase());
        }
    }

    /**
     * Add hostile
     *
     * @param hostile  Hostile
     * @param nickname Nickname
     *
     * @return EncounteredHostileInterface
     */
    @NotNull EncounteredHostileInterface addHostile(@NotNull Hostile hostile, @NotNull String nickname)
    {
        String                      capitalNickname       = WordUtils.capitalizeFully(nickname);
        EncounteredHostileInterface newEncounteredHostile = new EncounteredHostile(hostile, capitalNickname);
        String                      hostileSpecies        = hostile.getSpecies();
        String                      nicknameToLower       = nickname.toLowerCase();
        if (hostileSpecies.toLowerCase().equals(nicknameToLower)) {
            int speciesCount = 0;
            for (EncounteredHostileInterface encounteredHostile : this.hostiles) {
                if (encounteredHostile.getSpecies().equals(newEncounteredHostile.getSpecies())) {
                    if (encounteredHostile.getSpecies().equals(newEncounteredHostile.getName())) {
                        encounteredHostile.setName(encounteredHostile.getName() + "A");
                    }
                    speciesCount++;
                }
            }
            if (speciesCount > 0) {
                char letter = (char) (65 + speciesCount);
                newEncounteredHostile = new EncounteredHostile(hostile, hostile.getSpecies() + letter);
            }
        } else {
            for (EncounteredHostileInterface hostileData : this.hostiles) {
                if (hostileData.getName().toLowerCase().equals(nicknameToLower)) {
                    throw new HostileNicknameInUseException(nickname);
                }
            }
        }
        this.hostiles.add(newEncounteredHostile);
        return newEncounteredHostile;
    }

    /**
     * Get active explorers
     *
     * @return ArrayList<EncounteredExplorerInterface>
     */
    @NotNull ArrayList<EncounteredExplorerInterface> getActiveExplorers()
    {
        return this.explorerRoster.getActiveExplorers();
    }

    /**
     * Get active hostiles
     *
     * @return ArrayList<EncounteredHostileInterface>
     */
    @NotNull ArrayList<EncounteredHostileInterface> getActiveHostiles()
    {
        ArrayList<EncounteredHostileInterface> activeHostiles = new ArrayList<>();
        for (EncounteredHostileInterface encounteredHostile : this.hostiles) {
            if (!encounteredHostile.isSlain()) {
                activeHostiles.add(encounteredHostile);
            }
        }
        return activeHostiles;
    }

    /**
     * Get alive explorers
     *
     * @return ArrayList<EncounteredExplorerInterface>
     */
    @NotNull ArrayList<EncounteredExplorerInterface> getAliveExplorers()
    {
        return this.explorerRoster.getAliveExplores();
    }

    /**
     * Get all explorers
     *
     * @return ArrayList<EncounteredHostileInterface>
     */
    @NotNull ArrayList<EncounteredExplorerInterface> getAllExplorers()
    {
        return this.explorerRoster.getAllExplorers();
    }

    /**
     * Get all hostiles
     *
     * @return ArrayList<EncounteredHostileInterface>
     */
    @NotNull ArrayList<EncounteredHostileInterface> getAllHostiles()
    {
        return new ArrayList<>(this.hostiles);
    }

    /**
     * Get current explorer
     *
     * @return EncounteredExplorerInterface
     */
    @NotNull EncounteredExplorerInterface getCurrentExplorer()
    {
        if (!this.isInitiativePhase()) {
            throw new NotInInitiativeException();
        }
        EncounteredExplorerInterface currentExplorer = this.initiative.getCurrentExplorer();
        if (currentExplorer == null) {
            throw EncounteredCreatureNotFoundException.createForCurrentPlayer();
        }
        return currentExplorer;
    }

    /**
     * Get creature
     *
     * @param name Name of creature to find
     *
     * @return EncounterCreatureInterface
     *
     * @throws EncounteredCreatureNotFoundException If creature with name not found
     */
    @NotNull EncounterCreatureInterface getCreature(@NotNull String name)
    {
        ArrayList<EncounterCreatureInterface> allCreatures = new ArrayList<>();
        allCreatures.addAll(this.explorerRoster.getAllExplorers());
        allCreatures.addAll(this.hostiles);
        for (EncounterCreatureInterface creature : allCreatures) {
            if (creature.isName(name)) {
                return creature;
            }
        }
        throw EncounteredCreatureNotFoundException.createForCreature(name);
    }

    /**
     * Get explorer by name
     *
     * @param name Name of explorer to search for
     *
     * @return EncounteredExplorerInterface
     */
    @NotNull EncounteredExplorerInterface getExplorer(@NotNull String name)
    {
        return this.explorerRoster.getExplorer(name);
    }

    /**
     * Get explorer by player
     *
     * @param player Owner of explorer to search for
     *
     * @return EncounteredExplorerInterface
     */
    @NotNull EncounteredExplorerInterface getExplorer(@NotNull Player player)
    {
        return this.explorerRoster.getExplorer(player);
    }

    /**
     * Get hostile
     *
     * @param name Name of hostile to find
     *
     * @return EncounteredHostileInterface
     *
     * @throws EncounteredCreatureNotFoundException If hostile with name not found
     */
    @NotNull EncounteredHostileInterface getHostile(@NotNull String name)
    {
        for (EncounteredHostileInterface encounteredHostile : this.hostiles) {
            if (encounteredHostile.isName(name)) {
                if (encounteredHostile.isSlain()) {
                    throw new HostileSlainException(
                        encounteredHostile.getName(),
                        encounteredHostile.getSlayer().getName()
                    );
                }
                return encounteredHostile;
            }
        }
        throw EncounteredCreatureNotFoundException.createForHostile(name);
    }

    /**
     * Get max player count
     *
     * @return int
     */
    int getMaxPlayerCount()
    {
        return this.explorerRoster.getMaxPlayerCount();
    }

    /**
     * Get next explorer
     *
     * @return EncounteredExplorerInterface
     *
     * @throws EncounteredCreatureNotFoundException When next explorer does not exist
     */
    @NotNull EncounteredExplorerInterface getNextExplorer()
    {
        if (!this.isInitiativePhase()) {
            throw new NotInInitiativeException();
        }
        EncounteredExplorerInterface nextExplorer = this.initiative.getNextExplorer();
        if (nextExplorer == null) {
            throw EncounteredCreatureNotFoundException.createForNextPlayer();
        }
        return nextExplorer;
    }

    /**
     * Has active explorers
     *
     * @return boolean
     */
    boolean hasActiveExplorers()
    {
        return this.explorerRoster.hasActiveExplorers();
    }

    /**
     * Has active hostiles
     *
     * @return boolean
     */
    boolean hasActiveHostiles()
    {
        return this.getActiveHostiles().size() > 0;
    }

    /**
     * Has phoenix down
     *
     * @return boolean
     */
    boolean hasPhoenixDown()
    {
        return this.hasPhoenixDown;
    }

    /**
     * Have players joined
     *
     * @return boolean
     */
    boolean havePlayersJoined()
    {
        return !this.isJoinPhase() || this.explorerRoster.hasActiveExplorers();
    }

    /**
     * Is attack phase
     *
     * @return boolean
     */
    boolean isAttackPhase()
    {
        return this.currentPhase.equals(Encounter.ATTACK_PHASE);
    }

    /**
     * Is dodge phase
     *
     * @return boolean
     */
    boolean isDodgePhase()
    {
        return this.currentPhase.equals(Encounter.DODGE_PHASE);
    }

    /**
     * Is dungeon full
     *
     * @return boolean
     */
    boolean isFullDungeon()
    {
        return this.explorerRoster.isFull();
    }

    /**
     * Is initiative phase
     *
     * @return boolean
     */
    boolean isInitiativePhase()
    {
        return this.isAttackPhase() || this.isDodgePhase();
    }

    /**
     * Is loot phase
     *
     * @return boolean
     */
    boolean isLootPhase()
    {
        return this.currentPhase.equals(Encounter.LOOT_PHASE);
    }

    /**
     * Is encounter over
     *
     * @return boolean
     */
    boolean isOver()
    {
        return this.isLootPhase() || this.isEndPhase();
    }

    /**
     * Has encounter started
     *
     * @return boolean
     */
    boolean isStarted()
    {
        return !this.currentPhase.equals(Encounter.CREATE_PHASE);
    }

    /**
     * Player has left
     *
     * @param player Player
     *
     * @return EncounteredExplorerInterface
     */
    @NotNull EncounteredExplorerInterface playerHasLeft(@NotNull Player player)
    {
        return this.explorerRoster.playerHasLeft(player);
    }

    /**
     * Player has rejoined
     *
     * @param player Player
     *
     * @return EncounteredExplorerInterface
     */
    @NotNull EncounteredExplorerInterface playerHasRejoined(@NotNull Player player)
    {
        return this.explorerRoster.playerHasRejoined(player);
    }

    /**
     * Set max player count
     *
     * @param maxPlayerCount Max amount of players allowed for this encounter
     */
    void setMaxPlayerCount(int maxPlayerCount)
    {
        this.explorerRoster.setMaxPlayerCount(maxPlayerCount);
    }

    /**
     * Sort roster
     */
    void sortRoster()
    {
        this.explorerRoster.sort();
    }

    /**
     * Start attack phase
     */
    void startAttackPhase()
    {
        if (this.isAttackPhase()) {
            throw new StartCurrentPhaseException(Encounter.ATTACK_PHASE);
        }
        for (EncounteredExplorerInterface encounteredExplorer : this.explorerRoster.getActiveExplorers()) {
            encounteredExplorer.resetActions(true);
        }
        this.currentPhase = Encounter.ATTACK_PHASE;
        this.initiative = new InitiativeQueue(this.getAllExplorers());
    }

    /**
     * Start dodge phase
     */
    void startDodgePhase()
    {
        if (this.isDodgePhase()) {
            throw new StartCurrentPhaseException(Encounter.DODGE_PHASE);
        }
        for (EncounteredExplorerInterface encounteredExplorer : this.explorerRoster.getActiveExplorers()) {
            encounteredExplorer.resetActions(false);
        }
        this.currentPhase = Encounter.DODGE_PHASE;
        this.initiative = new InitiativeQueue(this.getAllExplorers());
    }

    /**
     * Start end phase
     */
    void startEndPhase()
    {
        this.currentPhase = Encounter.END_PHASE;
        this.initiative = new InitiativeQueue();
    }

    /**
     * Start join phase
     */
    void startJoinPhase()
    {
        this.currentPhase = Encounter.JOIN_PHASE;
    }

    /**
     * Start loot phase
     */
    void startLootPhase()
    {
        if (this.isLootPhase()) {
            throw new StartCurrentPhaseException(Encounter.LOOT_PHASE);
        }
        this.currentPhase = Encounter.LOOT_PHASE;
        this.initiative = new InitiativeQueue();
    }

    /**
     * Start RP phase
     */
    void startRpPhase()
    {
        this.currentPhase = Encounter.RP_PHASE;
        this.initiative = new InitiativeQueue();
    }

    /**
     * Remove encountered explorer from encounter
     *
     * @param encounteredExplorer Encountered explorer to remove
     */
    void removeExplorer(@NotNull EncounteredExplorerInterface encounteredExplorer)
    {
        this.explorerRoster.remove(encounteredExplorer);
        if (this.initiative.contains(encounteredExplorer)) {
            this.initiative.remove(encounteredExplorer);
        }
    }

    /**
     * Remove encountered hostile from encounter
     *
     * @param encounteredHostile Encountered hostile to remove
     */
    void removeHostile(@NotNull EncounteredHostileInterface encounteredHostile)
    {
        if (!this.hostiles.contains(encounteredHostile)) {
            throw EncounteredCreatureNotFoundException.createForHostile(encounteredHostile.getName());
        }
        this.hostiles.remove(encounteredHostile);
    }

    /**
     * Use phoenix down
     */
    void usePhoenixDown()
    {
        this.hasPhoenixDown = false;
    }

    /**
     * Is end phase
     *
     * @return bool
     */
    private boolean isEndPhase()
    {
        return this.currentPhase.equals(Encounter.END_PHASE);
    }

    /**
     * Is join phase
     *
     * @return bool
     */
    private boolean isJoinPhase()
    {
        return this.currentPhase.equals(Encounter.JOIN_PHASE);
    }
}
