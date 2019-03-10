package bot.PlayerCharacter;

import bot.PlayerCharacter.Exception.PlayerCharacterNotFoundException;
import bot.Player.PlayerManager;
import bot.PlayerCharacter.Exception.NotYourCharacterException;

import java.util.ArrayList;

public class PlayerCharacterManager {

    public static void createPlayerCharacter(
        String userId,
        String name,
        int hitpoints,
        int strength,
        int defense,
        int agility,
        int wisdom,
        String appLink,
        String statsLink
    ) {
        PlayerManager.ensurePlayerExist(userId);
        PlayerCharacter playerCharacter = PlayerCharacterRepository.getPlayerCharacter(name);
        if (playerCharacter != null && !playerCharacter.isOwner(userId)) {
            throw NotYourCharacterException.createForNameTaken(
                playerCharacter.getName(),
                playerCharacter.getOwner().getName()
            );
        }
        PlayerCharacter.validateStats(name, hitpoints, strength, defense, agility, wisdom);
        PlayerCharacterRepository.insertPlayerCharacter(userId, name, strength, defense, agility, wisdom, hitpoints, appLink, statsLink);
    }

    public static void deletePlayerCharacter(String userId, String name) {
        PlayerManager.ensurePlayerExist(userId);
        PlayerCharacterRepository.deletePlayerCharacter(userId, name);
    }

    public static ArrayList<PlayerCharacter> getAllMyPCs(String playerId) {
        return PlayerCharacterRepository.getAllMyPCs(playerId);
    }

    public static ArrayList<PlayerCharacter> getAllPCs() {
        return PlayerCharacterRepository.getAllPCs();
    }

    public static PlayerCharacter getMyPC(String playerId, String name) {
        PlayerCharacter playerCharacter = PlayerCharacterRepository.getMyPlayerCharacter(playerId, name);
        if (playerCharacter == null) {
            throw PlayerCharacterNotFoundException.createNotInDatabase(name);
        }
        return playerCharacter;
    }

    public static void main(String[] args) {
        PlayerCharacterRepository.createTableIfNotExists();
    }

}
