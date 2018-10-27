package bot.Encounter;

import bot.Encounter.EncounterData.EncounterDataInterface;
import bot.Encounter.EncounterData.HostileEncounterData;
import bot.Encounter.EncounterData.PCEncounterData;
import bot.Encounter.Exception.*;
import bot.Player.Player;

import java.util.*;

public class EncounterContext {

    public static  String ATTACK_PHASE = "ATTACK";
    public static  String DODGE_PHASE  = "DODGE";
    public static  String LOOT_PHASE   = "LOOT";
    private static String CREATE_PHASE = "CREATE";
    private static String END_PHASE    = "END";
    private static String JOIN_PHASE   = "JOIN";
    private static String RP_PHASE     = "RP";

    private ArrayList<HostileEncounterData> hostiles;
    private ArrayList<PCEncounterData>      playerCharacters;
    private InitiativeQueue                 initiative;
    private String                          currentPhase;
    private int                             absentPlayerCount;
    private int                             maxPlayerCount;

    public EncounterContext() {
        this.absentPlayerCount = 0;
        this.currentPhase = EncounterContext.CREATE_PHASE;
        this.initiative = new InitiativeQueue();
        this.hostiles = new ArrayList<>();
        this.maxPlayerCount = 0;
        this.playerCharacters = new ArrayList<>();
    }

    void addCharacter(PCEncounterData newPlayerCharacter) {
        Player player = newPlayerCharacter.getOwner();
        for (PCEncounterData character : this.playerCharacters) {
            if (character.isOwner(player.getUserId())) {
                throw new MultiplePlayerCharactersException(player, character.getName());
            }
        }
        if (this.isInitiativePhase()) {
            this.initiative.add(newPlayerCharacter);
            newPlayerCharacter.resetActions(this.isAttackPhase());
        }
        for (PCEncounterData playerCharacter : this.playerCharacters) {
            if (playerCharacter.getAgility() < newPlayerCharacter.getAgility()) {
                this.playerCharacters.add(this.playerCharacters.indexOf(playerCharacter), newPlayerCharacter);
                return;
            }
        }
        this.playerCharacters.add(newPlayerCharacter);
    }

    void addHostile(HostileEncounterData newHostile) {
        this.hostiles.add(newHostile);
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
        ArrayList<PCEncounterData> activePlayers = new ArrayList<>();
        for (PCEncounterData playerCharacter : this.playerCharacters) {
            if (!playerCharacter.isSlain() && playerCharacter.isPresent()) {
                activePlayers.add(playerCharacter);
            }
        }
        return activePlayers;
    }

    ArrayList<HostileEncounterData> getAllHostiles() {
        return this.hostiles;
    }

    ArrayList<PCEncounterData> getAllPlayerCharacters() {
        return this.playerCharacters;
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
        String                            nameLower    = name.toLowerCase();
        ArrayList<EncounterDataInterface> allCreatures = new ArrayList<>();
        allCreatures.addAll(this.playerCharacters);
        allCreatures.addAll(this.hostiles);
        for (EncounterDataInterface creature : allCreatures) {
            if (creature.getName().toLowerCase().equals(nameLower)) {
                return creature;
            }
        }
        throw EncounterDataNotFoundException.createForEncounterData(name);
    }

    HostileEncounterData getHostile(String name) {
        String nameLower = name.toLowerCase();
        for (HostileEncounterData hostile : this.hostiles) {
            if (hostile.getName().toLowerCase().equals(nameLower)) {
                if (hostile.isSlain()) {
                    throw new HostileSlainException(hostile.getName(), hostile.getSlayer().getName());
                }
                return hostile;
            }
        }
        throw EncounterDataNotFoundException.createForHostile(name);
    }

    int getMaxPlayerCount() {
        return this.maxPlayerCount;
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
        String nameLower = name.toLowerCase();
        for (PCEncounterData playerCharacter : this.playerCharacters) {
            if (playerCharacter.getName().toLowerCase().equals(nameLower)) {
                return playerCharacter;
            }
        }
        throw EncounterDataNotFoundException.createForPlayerCharacter(name);
    }

    PCEncounterData getPlayerCharacter(Player player) {
        for (PCEncounterData playerCharacter : this.playerCharacters) {
            if (playerCharacter.isOwner(player.getUserId())) {
                return playerCharacter;
            }
        }
        throw EncounterDataNotFoundException.createForPlayerCharacter(player);
    }

    boolean hasActiveHostiles() {
        return this.getActiveHostiles().size() > 0;
    }

    boolean hasActivePlayers() {
        return this.getActivePlayerCharacters().size() > 0;
    }

    boolean havePlayersJoined() {
        return !this.isJoinPhase() || this.hasActivePlayers();
    }

    boolean isAttackPhase() {
        return this.currentPhase.equals(EncounterContext.ATTACK_PHASE);
    }

    boolean isDodgePhase() {
        return this.currentPhase.equals(EncounterContext.DODGE_PHASE);
    }

    boolean isFullDungeon() {
        return !(this.playerCharacters.size() - this.absentPlayerCount < this.maxPlayerCount);
    }

    boolean isLootPhase() {
        return this.currentPhase.equals(EncounterContext.LOOT_PHASE);
    }

    boolean isOver() {
        return this.isLootPhase() || this.isEndPhase();
    }

    boolean isPhase(String phase) {
        return this.currentPhase.equals(phase);
    }

    boolean isStarted() {
        return !this.currentPhase.equals(EncounterContext.CREATE_PHASE);
    }

    PCEncounterData playerHasLeft(Player player) {
        PCEncounterData playerCharacter = this.getPlayerCharacter(player);
        if (!playerCharacter.isPresent()) {
            throw PlayerCharacterPresentException.createHasAleadyLeft(player.getAsMention());
        }
        playerCharacter.useAllActions();
        playerCharacter.leave();
        this.absentPlayerCount++;
        return playerCharacter;
    }

    PCEncounterData playerHasRejoined(Player player) {
        PCEncounterData playerCharacter = this.getPlayerCharacter(player);
        if (playerCharacter == null) {
            throw EncounterDataNotFoundException.createForPlayerCharacter(player);
        } else if (playerCharacter.isPresent()) {
            throw PlayerCharacterPresentException.createCannotRejoinIfPresent(player.getAsMention());
        } else if (this.isFullDungeon()) {
            throw DungeonException.createFullDungeon(player);
        }
        playerCharacter.rejoin();
        this.absentPlayerCount--;
        return playerCharacter;
    }

    void setMaxPlayerCount(int maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    void startAttackPhase() {
        if (this.isAttackPhase()) {
            throw new StartCurrentPhaseException(EncounterContext.ATTACK_PHASE);
        }
        for (PCEncounterData playerCharacter : this.playerCharacters) {
            playerCharacter.resetActions(true);
        }
        this.currentPhase = EncounterContext.ATTACK_PHASE;
        this.initiative = new InitiativeQueue(this.getAllPlayerCharacters());
    }

    void startDodgePhase() {
        if (this.isDodgePhase()) {
            throw new StartCurrentPhaseException(EncounterContext.DODGE_PHASE);
        }
        for (PCEncounterData playerCharacter : this.playerCharacters) {
            playerCharacter.resetActions(false);
        }
        this.currentPhase = EncounterContext.DODGE_PHASE;
        this.initiative = new InitiativeQueue(this.getAllPlayerCharacters());
    }

    void startEndPhase() {
        this.currentPhase = EncounterContext.END_PHASE;
        this.initiative = new InitiativeQueue();
    }

    void startJoinPhase() {
        this.currentPhase = EncounterContext.JOIN_PHASE;
    }

    void startLootPhase() {
        if (this.isLootPhase()) {
            throw new StartCurrentPhaseException(EncounterContext.LOOT_PHASE);
        }
        this.currentPhase = EncounterContext.LOOT_PHASE;
        this.initiative = new InitiativeQueue();
    }

    void startRpPhase() {
        this.currentPhase = EncounterContext.RP_PHASE;
        this.initiative = new InitiativeQueue();
    }

    void removeHostile(HostileEncounterData hostile) {
        if (!this.hostiles.contains(hostile)) {
            throw EncounterDataNotFoundException.createForHostile(hostile.getName());
        }
        this.hostiles.remove(hostile);
    }

    void removePlayerCharacter(PCEncounterData playerCharacter) {
        if (!this.playerCharacters.contains(playerCharacter)) {
            throw EncounterDataNotFoundException.createForPlayerCharacter(playerCharacter.getName());
        }
        if (this.initiative.contains(playerCharacter)) {
            this.initiative.remove(playerCharacter);
        }
        this.playerCharacters.remove(playerCharacter);
    }

    private boolean isInitiativePhase() {
        return this.isAttackPhase() || this.isDodgePhase();
    }

    private boolean isEndPhase() {
        return this.currentPhase.equals(EncounterContext.END_PHASE);
    }

    private boolean isJoinPhase() {
        return this.currentPhase.equals(EncounterContext.JOIN_PHASE);
    }
}
