package bot.Encounter;

import bot.Encounter.EncounterData.PCEncounterData;
import bot.Encounter.Exception.EncounterDataNotFoundException;
import bot.Encounter.Exception.MultiplePlayerCharactersException;
import bot.Encounter.Exception.PlayerCharacterPresentException;
import bot.Player.Player;

import java.util.ArrayList;

class PCRoster {

    private ArrayList<PCEncounterData> roster;
    private int                        absentPlayerCount;
    private int                        maxPlayerCount;

    /**
     * PCRoster constructor
     */
    PCRoster() {
        this.absentPlayerCount = 0;
        this.maxPlayerCount = 0;
        this.roster = new ArrayList<>();
    }

    /**
     * Add PCEncounterData to the roster
     *
     * @param newPlayerCharacter Player character to add
     */
    void addPC(PCEncounterData newPlayerCharacter) {
        Player player = newPlayerCharacter.getOwner();
        if (this.isRosterFull()) {
            throw RosterException.createFullRoster(player);
        }
        for (PCEncounterData character : this.roster) {
            if (character.isOwner(player.getUserId())) {
                throw new MultiplePlayerCharactersException(player, character.getName());
            }
        }
        for (PCEncounterData playerCharacter : this.roster) {
            if (playerCharacter.getAgility() < newPlayerCharacter.getAgility()) {
                this.roster.add(this.roster.indexOf(playerCharacter), newPlayerCharacter);
                return;
            }
        }
        this.roster.add(newPlayerCharacter);
    }

    /**
     * Get active player characters
     *
     * @return ArrayList<PCEncounterData>
     */
    ArrayList<PCEncounterData> getActive() {
        ArrayList<PCEncounterData> activeEncounterData = new ArrayList<>();
        for (PCEncounterData pcEncounterData : this.roster) {
            if (!pcEncounterData.isSlain() && pcEncounterData.isPresent()) {
                activeEncounterData.add(pcEncounterData);
            }
        }
        return activeEncounterData;
    }

    /**
     * Get all encounter data
     *
     * @return ArrayList<PCEncounterData>
     */
    ArrayList<PCEncounterData> getAll() {
        return this.roster;
    }

    /**
     * Get encounter data with given name
     *
     * @param name Encounter data name
     *
     * @return EncounterDataInterface
     *
     * @throws EncounterDataNotFoundException Thrown when EncounterData is not found
     */
    PCEncounterData getPC(String name) throws EncounterDataNotFoundException {
        String nameLower = name.toLowerCase();
        for (PCEncounterData playerCharacter : this.roster) {
            if (playerCharacter.isName(nameLower)) {
                return playerCharacter;
            }
        }
        throw EncounterDataNotFoundException.createForPlayerCharacter(name);
    }

    /**
     * Get encounter data with given name
     *
     * @param player Encounter data player
     *
     * @return EncounterDataInterface
     *
     * @throws EncounterDataNotFoundException Thrown when EncounterData is not found
     */
    PCEncounterData getPC(Player player) throws EncounterDataNotFoundException {
        for (PCEncounterData playerCharacter : this.roster) {
            if (playerCharacter.isOwner(player.getUserId())) {
                return playerCharacter;
            }
        }
        throw EncounterDataNotFoundException.createForPlayerCharacter(player);
    }

    int getMaxPlayerCount() {
        return this.maxPlayerCount;
    }

    /**
     * Does this roster have active EncounterData
     *
     * @return bool
     */
    boolean hasActive() {
        return this.getActive().size() > 0;
    }

    /**
     * Is EncounterData with given name currently in the roster
     *
     * @param name EncounterData name
     *
     * @return bool
     */
    boolean isPCInEncounter(String name) {
        for (PCEncounterData playerCharacter : this.roster) {
            if (playerCharacter.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    boolean isRosterFull() {
        return !((this.getAll().size() - this.absentPlayerCount) < this.maxPlayerCount);
    }

    /**
     * Marks Player's PCEncounterData as "left"
     *
     * @param player Player
     *
     * @return PCEncounterData
     */
    PCEncounterData playerHasLeft(Player player) {
        PCEncounterData playerCharacter = this.getPC(player);
        if (!playerCharacter.isPresent()) {
            throw PlayerCharacterPresentException.createHasAleadyLeft(player.getAsMention());
        }
        playerCharacter.useAllActions();
        playerCharacter.leave();
        this.absentPlayerCount++;
        return playerCharacter;
    }

    /**
     * Unmarks Player's PCEncounterData as "left"
     *
     * @param player Player
     *
     * @return PCEncounterData
     */
    PCEncounterData playerHasRejoined(Player player) {
        PCEncounterData playerCharacter = this.getPC(player);
        if (playerCharacter == null) {
            throw EncounterDataNotFoundException.createForPlayerCharacter(player);
        } else if (playerCharacter.isPresent()) {
            throw PlayerCharacterPresentException.createCannotRejoinIfPresent(player.getAsMention());
        } else if (this.isRosterFull()) {
            throw RosterException.createFullRoster(player);
        }
        playerCharacter.rejoin();
        this.absentPlayerCount--;
        return playerCharacter;
    }

    /**
     * Removes Player's PCEncounterData from roster
     *
     * @param playerCharacter PCEncounterData
     */
    void remove(PCEncounterData playerCharacter) {
        if (!this.roster.contains(playerCharacter)) {
            throw EncounterDataNotFoundException.createForPlayerCharacter(playerCharacter.getName());
        }
        this.roster.remove(playerCharacter);
    }

    /**
     * Set max player count
     *
     * @param maxPlayerCount Max player counter
     */
    void setMaxPlayerCount(int maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }
}
