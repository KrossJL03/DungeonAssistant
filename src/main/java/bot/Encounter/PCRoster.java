package bot.Encounter;

import bot.Encounter.EncounterData.PCEncounterData;
import bot.Encounter.Exception.*;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class PCRoster {

    private ArrayList<PCEncounterData> playerCharacters;
    private int                        maxPlayerCount;

    /**
     * PCRosterTest constructor
     */
    PCRoster() {
        this.maxPlayerCount = 0;
        this.playerCharacters = new ArrayList<>();
    }

    /**
     * Add PCEncounterData to the playerCharacters
     *
     * @param newPlayerCharacter Player character to add
     *
     * @throws MultiplePlayerCharactersException If player already has character in the encounter
     * @throws PCRosterException                 If max player count has been reached
     */
    void addPC(@NotNull PCEncounterData newPlayerCharacter) {
        // todo consider building PCEncounterData in roster
        Player player = newPlayerCharacter.getOwner();
        if (this.containsPlayer(player)) {
            PCEncounterData character = this.getPC(player);
            throw new MultiplePlayerCharactersException(player, character.getName());
        } else if (!this.isMaxPlayerCountSet()) {
            throw new MaxZeroPlayersException();
        } else if (this.isFull()) {
            // this error is thrown last because multiple pc exception takes precedence
            throw PCRosterException.createFullRoster(player);
        }
        this.playerCharacters.add(newPlayerCharacter);
        this.sort();
    }

    /**
     * Is EncounterData with given name currently in the playerCharacters
     *
     * @param name PlayerCharacter name
     *
     * @return bool
     */
    boolean containsPC(@NotNull String name) {
        for (PCEncounterData playerCharacter : this.playerCharacters) {
            if (playerCharacter.isName(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get active player characters
     *
     * @return ArrayList<PCEncounterData>
     */
    @NotNull ArrayList<PCEncounterData> getActivePCs() {
        ArrayList<PCEncounterData> activeEncounterData = new ArrayList<>();
        for (PCEncounterData pcEncounterData : this.playerCharacters) {
            if (pcEncounterData.isActive()) {
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
    @NotNull ArrayList<PCEncounterData> getAllPCs() {
        return new ArrayList<>(this.playerCharacters);
    }

    /**
     * Get encounter data with given name
     *
     * @param name PlayerCharacter name
     *
     * @return EncounterDataInterface
     *
     * @throws EncounterDataNotFoundException Thrown when EncounterData is not found
     */
    @NotNull PCEncounterData getPC(@NotNull String name) throws EncounterDataNotFoundException {
        for (PCEncounterData playerCharacter : this.playerCharacters) {
            if (playerCharacter.isName(name)) {
                return playerCharacter;
            }
        }
        throw EncounterDataNotFoundException.createForPlayerCharacter(name);
    }

    /**
     * Get encounter data with given name
     *
     * @param player Player
     *
     * @return EncounterDataInterface
     *
     * @throws EncounterDataNotFoundException Thrown when EncounterData is not found
     */
    @NotNull PCEncounterData getPC(@NotNull Player player) throws EncounterDataNotFoundException {
        for (PCEncounterData playerCharacter : this.playerCharacters) {
            if (playerCharacter.isOwner(player)) {
                return playerCharacter;
            }
        }
        throw EncounterDataNotFoundException.createForPlayerCharacter(player);
    }

    /**
     * Get max player count
     *
     * @return int
     */
    int getMaxPlayerCount() {
        return this.maxPlayerCount;
    }

    /**
     * Does this roster have active PCEncounterData
     *
     * @return bool
     */
    boolean hasActivePCs() {
        return this.getActivePlayerCount() > 0;
    }

    /**
     * Is roster full
     *
     * @return bool
     */
    boolean isFull() {
        return this.getPresentPlayerCount() >= this.maxPlayerCount;
    }

    /**
     * Mark Player's PCEncounterData as not present
     *
     * @param player Player
     *
     * @return PCEncounterData
     *
     * @throws PlayerCharacterPresentException If player has already left
     */
    PCEncounterData playerHasLeft(@NotNull Player player) {
        // todo rename
        PCEncounterData playerCharacter = this.getPC(player);
        if (!playerCharacter.isPresent()) {
            throw PlayerCharacterPresentException.createHasAleadyLeft(player.getAsMention());
        }
        playerCharacter.leave();
        return playerCharacter;
    }

    /**
     * Mark Player's PCEncounterData as present
     *
     * @param player Player
     *
     * @return PCEncounterData
     *
     * @throws PlayerCharacterPresentException If player is already present
     * @throws PCRosterException               If roster is full
     */
    PCEncounterData playerHasRejoined(@NotNull Player player) {
        // todo rename
        PCEncounterData playerCharacter = this.getPC(player);
        if (playerCharacter.isPresent()) {
            throw PlayerCharacterPresentException.createCannotRejoinIfPresent(player.getAsMention());
        } else if (this.isFull()) {
            // this error is thrown last because already present message takes precedence
            throw PCRosterException.createFullRoster(player);
        }
        playerCharacter.rejoin();
        return playerCharacter;
    }

    /**
     * Remove Player's PCEncounterData
     *
     * @param playerCharacter PCEncounterData
     *
     * @throws EncounterDataNotFoundException If player character is not found
     */
    void remove(@NotNull PCEncounterData playerCharacter) {
        if (!this.containsPC(playerCharacter.getName())) {
            throw EncounterDataNotFoundException.createForPlayerCharacter(playerCharacter.getName());
        }
        this.playerCharacters.remove(playerCharacter);
    }

    /**
     * Set max player count
     *
     * @param maxPlayerCount Max player count
     *
     * @throws PCRosterException If present character count exceeds new limit
     */
    void setMaxPlayerCount(int maxPlayerCount) {
        if (maxPlayerCount < 1) {
            throw PCRosterException.createMaxPlayerCountLessThanOne();
        }
        int presentPlayerCount = this.getPresentPlayerCount();
        if (maxPlayerCount < presentPlayerCount) {
            throw PCRosterException.createNewMaxPlayerCountGreaterThanCurrentPlayerCount(
                maxPlayerCount,
                presentPlayerCount
            );
        }
        this.maxPlayerCount = maxPlayerCount;
    }

    /**
     * Sort roster
     */
    void sort() {
        this.playerCharacters.sort(new PCAgilityComparator());
    }

    /**
     * Is Player currently in the roster
     *
     * @param player Player
     *
     * @return bool
     */
    private boolean containsPlayer(@NotNull Player player) {
        for (PCEncounterData playerCharacter : this.playerCharacters) {
            if (playerCharacter.isOwner(player)) {
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
    private int getActivePlayerCount() {
        return this.getActivePCs().size();
    }

    /**
     * Get active player characters
     *
     * @return ArrayList<PCEncounterData>
     */
    @NotNull private ArrayList<PCEncounterData> getPresentPCs() {
        ArrayList<PCEncounterData> presentEncounterData = new ArrayList<>();
        for (PCEncounterData pcEncounterData : this.playerCharacters) {
            if (pcEncounterData.isPresent()) {
                presentEncounterData.add(pcEncounterData);
            }
        }
        return presentEncounterData;
    }

    /**
     * Get number of present players
     *
     * @return int
     */
    private int getPresentPlayerCount() {
        return this.getPresentPCs().size();
    }

    /**
     * Has the max player count been set
     *
     * @return boolean
     */
    private boolean isMaxPlayerCountSet() {
        return this.maxPlayerCount > 0;
    }
}
