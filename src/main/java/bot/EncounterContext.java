package bot;

import bot.Entity.HostileEncounterData;
import bot.Entity.PCEncounterData;
import bot.Exception.*;
import net.dv8tion.jda.core.entities.User;

import java.util.*;

public class EncounterContext {

    private static String ATTACK_PHASE = "ATTACK";
    private static String DODGE_PHASE  = "DODGE";
    private static String JOIN_PHASE   = "JOIN";
    private static String LOOT_PHASE   = "LOOT";
    private static String RP_PHASE     = "RP";

    private ArrayList<HostileEncounterData> hostiles;
    private ArrayList<PCEncounterData>      playerCharacters;
    private InitiativeQueue                 initiative;
    private String                          currentPhase;
    private int                             maxPlayerCount;
    private boolean                         isStarted;

    public EncounterContext() {
        this.currentPhase = "";
        this.initiative = new InitiativeQueue();
        this.hostiles = new ArrayList<>();
        this.isStarted = false;
        this.maxPlayerCount = 0;
        this.playerCharacters = new ArrayList<>();
    }

    void addCharacter(PCEncounterData newPlayerCharacter) {
        User player = newPlayerCharacter.getOwner();
        for (PCEncounterData character : this.playerCharacters) {
            if (character.getOwner().equals(player)) {
                throw new MultiplePlayerCharactersException(player, character.getName());
            }
        }
        if (this.playerCharacters.size() == this.maxPlayerCount) {
            throw new FullDungeonException();
        }

        if (this.isInitiativePhase()) {
            this.initiative.add(newPlayerCharacter);
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

    void endCurrentPhase() {
        this.currentPhase = EncounterContext.RP_PHASE;
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
            if (!playerCharacter.isSlain() && !playerCharacter.hasLeft()) {
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
        return this.initiative.getCurrentPlayerCharacter();
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
        throw new NoHostileInEncounterException(name);
    }

    int getMaxPlayerCount() {
        return this.maxPlayerCount;
    }

    PCEncounterData getNextPlayerCharacter() {
        if (!this.isInitiativePhase()) {
            throw new NotInInitiativeException();
        }
        return this.initiative.getNextPlayerCharacter();
    }

    PCEncounterData getPlayerCharacter(String name) {
        String nameLower = name.toLowerCase();
        for (PCEncounterData playerCharacter : this.playerCharacters) {
            if (playerCharacter.getName().toLowerCase().equals(nameLower)) {
                return playerCharacter;
            }
        }
        throw new NoCharacterInEncounterException(name);
    }

    PCEncounterData getPlayerCharacter(User player) {
        for (PCEncounterData playerCharacter : this.playerCharacters) {
            if (playerCharacter.getOwner().equals(player)) {
                return playerCharacter;
            }
        }
        throw new NoCharacterInEncounterException(player);
    }

    boolean hasPlayerCharacterGone(PCEncounterData playerCharacter) {
        return !this.initiative.contains(playerCharacter);
    }

    boolean isAttackPhase() {
        return this.currentPhase.equals(EncounterContext.ATTACK_PHASE);
    }

    boolean isDodgePhase() {
        return this.currentPhase.equals(EncounterContext.DODGE_PHASE);
    }

    boolean isFullDungeon() {
        return !(this.playerCharacters.size() < this.maxPlayerCount);
    }

    boolean isOver() {
        return this.isStarted && (this.getActiveHostiles().size() == 0 || this.getActivePlayerCharacters().size() == 0);
    }

    boolean isStarted() {
        return this.isStarted;
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

    void startEncounter() {
        this.currentPhase = EncounterContext.JOIN_PHASE;
        this.isStarted = true;
    }

    void removeHostile(String name) {
        String nameLower = name.toLowerCase();
        for (HostileEncounterData hostile : this.hostiles) {
            if (hostile.getName().toLowerCase().equals(nameLower)) {
                this.hostiles.remove(hostile);
                return;
            }
        }
        throw new NoHostileInEncounterException(name);
    }

    void removePlayerCharacter(String name) {
        String nameLower = name.toLowerCase();
        for (PCEncounterData playerCharacter : this.playerCharacters) {
            if (playerCharacter.getName().toLowerCase().equals(nameLower)) {
                if (this.isInitiativePhase()) {
                    this.initiative.remove(playerCharacter);
                }
                this.playerCharacters.remove(playerCharacter);
                return;
            }
        }
        throw new NoCharacterInEncounterException(name);
    }

    private boolean isInitiativePhase() {
        return this.isAttackPhase() || this.isDodgePhase();
    }
}
