package bot.Encounter;

import bot.Encounter.EncounterData.EncounterDataInterface;
import bot.Encounter.EncounterData.HostileEncounterData;
import bot.Encounter.EncounterData.PCEncounterData;
import bot.Encounter.Exception.*;
import bot.Hostile.Hostile;
import bot.Player.Player;

import java.util.*;

public class Encounter {

    public static  String ATTACK_PHASE = "ATTACK";
    public static  String DODGE_PHASE  = "DODGE";
    public static  String LOOT_PHASE   = "LOOT";
    private static String CREATE_PHASE = "CREATE";
    private static String END_PHASE    = "END";
    private static String JOIN_PHASE   = "JOIN";
    private static String RP_PHASE     = "RP";

    private ArrayList<HostileEncounterData> hostiles;
    private PCRoster                        pcRoster;
    private InitiativeQueue                 initiative;
    private String                          currentPhase;
    private boolean                         hasPhoenixDown;

    public Encounter() {
        this.currentPhase = Encounter.CREATE_PHASE;
        this.initiative = new InitiativeQueue();
        this.hasPhoenixDown = true;
        this.hostiles = new ArrayList<>();
        this.pcRoster = new PCRoster();
    }

    void addCharacter(PCEncounterData newPlayerCharacter) {
        this.pcRoster.addPC(newPlayerCharacter);
        if (this.isInitiativePhase()) {
            this.initiative.add(newPlayerCharacter);
            newPlayerCharacter.resetActions(this.isAttackPhase());
        }
    }

    HostileEncounterData addHostile(Hostile hostile, String nickname) {
        String               capitalNickname = nickname.substring(0, 1).toUpperCase() + nickname.substring(1);
        HostileEncounterData newHostileData  = new HostileEncounterData(hostile, capitalNickname);
        String               hostileSpecies  = hostile.getSpecies();
        String               nicknameToLower = nickname.toLowerCase();
        if (hostileSpecies.toLowerCase().equals(nicknameToLower)) {
            int speciesCount = 0;
            for (HostileEncounterData hostileData : this.hostiles) {
                if (hostileData.getSpecies().equals(hostileData.getSpecies())) {
                    if (hostileData.getSpecies().equals(hostileData.getName())) {
                        hostileData.setName(hostileData.getName() + "A");
                    }
                    speciesCount++;
                }
            }
            if (speciesCount > 0) {
                char letter = (char) (65 + speciesCount);
                newHostileData = new HostileEncounterData(hostile, hostile.getSpecies() + letter);
            }
        } else {
            for (HostileEncounterData hostileData : this.hostiles) {
                if (hostileData.getName().toLowerCase().equals(nicknameToLower)) {
                    throw new HostileNicknameInUseException(nickname);
                }
            }
        }
        this.hostiles.add(newHostileData);
        return newHostileData;
    }

    ArrayList<HostileEncounterData> getActiveHostiles() {
        ArrayList<HostileEncounterData> activeHostiles = new ArrayList<>();
        for (HostileEncounterData hostile : this.hostiles) {
            if (!hostile.isSlain()) {
                activeHostiles.add(hostile);
            }
        }
        return activeHostiles;
    }

    ArrayList<PCEncounterData> getActivePlayerCharacters() {
        return this.pcRoster.getActive();
    }

    ArrayList<HostileEncounterData> getAllHostiles() {
        return this.hostiles;
    }

    ArrayList<PCEncounterData> getAllPlayerCharacters() {
        return this.pcRoster.getAll();
    }

    PCEncounterData getCurrentPlayerCharacter() {
        if (!this.isInitiativePhase()) {
            throw new NotInInitiativeException();
        }
        PCEncounterData currentPlayerCharacter = this.initiative.getCurrentPlayerCharacter();
        if (currentPlayerCharacter == null) {
            throw EncounterDataNotFoundException.createForCurrentPlayer();
        }
        return currentPlayerCharacter;
    }

    EncounterDataInterface getEncounterData(String name) {
        ArrayList<EncounterDataInterface> allCreatures = new ArrayList<>();
        allCreatures.addAll(this.pcRoster.getAll());
        allCreatures.addAll(this.hostiles);
        for (EncounterDataInterface creature : allCreatures) {
            if (creature.isName(name)) {
                return creature;
            }
        }
        throw EncounterDataNotFoundException.createForEncounterData(name);
    }

    HostileEncounterData getHostile(String name) {
        for (HostileEncounterData hostile : this.hostiles) {
            if (hostile.isName(name)) {
                if (hostile.isSlain()) {
                    throw new HostileSlainException(hostile.getName(), hostile.getSlayer().getName());
                }
                return hostile;
            }
        }
        throw EncounterDataNotFoundException.createForHostile(name);
    }

    int getMaxPlayerCount() {
        return this.pcRoster.getMaxPlayerCount();
    }

    PCEncounterData getNextPlayerCharacter() {
        if (!this.isInitiativePhase()) {
            throw new NotInInitiativeException();
        }
        PCEncounterData nextPlayerCharacter = this.initiative.getNextPlayerCharacter();
        if (nextPlayerCharacter == null) {
            throw EncounterDataNotFoundException.createForNextPlayer();
        }
        return nextPlayerCharacter;
    }

    PCEncounterData getPlayerCharacter(String name) {
        return this.pcRoster.getPC(name);
    }

    PCEncounterData getPlayerCharacter(Player player) {
        return this.pcRoster.getPC(player);
    }

    boolean hasActiveHostiles() {
        return this.getActiveHostiles().size() > 0;
    }

    boolean hasActivePCs() {
        return this.pcRoster.hasActive();
    }

    boolean hasPhoenixDown() {
        return this.hasPhoenixDown;
    }

    boolean havePlayersJoined() {
        return !this.isJoinPhase() || this.pcRoster.hasActive();
    }

    boolean isAttackPhase() {
        return this.currentPhase.equals(Encounter.ATTACK_PHASE);
    }

    boolean isDodgePhase() {
        return this.currentPhase.equals(Encounter.DODGE_PHASE);
    }

    boolean isFullDungeon() {
        return this.pcRoster.isRosterFull();
    }

    boolean isInEncounter(String name) {
        ArrayList<EncounterDataInterface> allCreatures = new ArrayList<>();
        allCreatures.addAll(this.playerCharacters);
        allCreatures.addAll(this.hostiles);
        for (EncounterDataInterface creature : allCreatures) {
            if (creature.isName(name)) {
                return true;
            }
        }
        return false;
    }

    boolean isInitiativePhase() {
        return this.isAttackPhase() || this.isDodgePhase();
    }

    boolean isLootPhase() {
        return this.currentPhase.equals(Encounter.LOOT_PHASE);
    }

    boolean isOver() {
        return this.isLootPhase() || this.isEndPhase();
    }

    boolean isPhase(String phase) {
        return this.currentPhase.equals(phase);
    }

    boolean isPCInEncounter(String name) {
        return this.pcRoster.isPCInEncounter(name);
    }

    boolean isStarted() {
        return !this.currentPhase.equals(Encounter.CREATE_PHASE);
    }

    PCEncounterData playerHasLeft(Player player) {
        return this.pcRoster.playerHasLeft(player);
    }

    PCEncounterData playerHasRejoined(Player player) {
        return this.pcRoster.playerHasRejoined(player);
    }

    void setMaxPlayerCount(int maxPlayerCount) {
        this.pcRoster.setMaxPlayerCount(maxPlayerCount);
    }

    void startAttackPhase() {
        if (this.isAttackPhase()) {
            throw new StartCurrentPhaseException(Encounter.ATTACK_PHASE);
        }
        for (PCEncounterData playerCharacter : this.pcRoster.getActive()) {
            playerCharacter.resetActions(true);
        }
        this.currentPhase = Encounter.ATTACK_PHASE;
        this.initiative = new InitiativeQueue(this.getAllPlayerCharacters());
    }

    void startDodgePhase() {
        if (this.isDodgePhase()) {
            throw new StartCurrentPhaseException(Encounter.DODGE_PHASE);
        }
        for (PCEncounterData playerCharacter : this.pcRoster.getActive()) {
            playerCharacter.resetActions(false);
        }
        this.currentPhase = Encounter.DODGE_PHASE;
        this.initiative = new InitiativeQueue(this.getAllPlayerCharacters());
    }

    void startEndPhase() {
        this.currentPhase = Encounter.END_PHASE;
        this.initiative = new InitiativeQueue();
    }

    void startJoinPhase() {
        this.currentPhase = Encounter.JOIN_PHASE;
    }

    void startLootPhase() {
        if (this.isLootPhase()) {
            throw new StartCurrentPhaseException(Encounter.LOOT_PHASE);
        }
        this.currentPhase = Encounter.LOOT_PHASE;
        this.initiative = new InitiativeQueue();
    }

    void startRpPhase() {
        this.currentPhase = Encounter.RP_PHASE;
        this.initiative = new InitiativeQueue();
    }

    void removeHostile(HostileEncounterData hostile) {
        if (!this.hostiles.contains(hostile)) {
            throw EncounterDataNotFoundException.createForHostile(hostile.getName());
        }
        this.hostiles.remove(hostile);
    }

    void removePlayerCharacter(PCEncounterData playerCharacter) {
        this.pcRoster.remove(playerCharacter);
        if (this.initiative.contains(playerCharacter)) {
            this.initiative.remove(playerCharacter);
        }
    }

    void usePhoenixDown() {
        this.hasPhoenixDown = false;
    }

    private boolean isEndPhase() {
        return this.currentPhase.equals(Encounter.END_PHASE);
    }

    private boolean isJoinPhase() {
        return this.currentPhase.equals(Encounter.JOIN_PHASE);
    }
}
