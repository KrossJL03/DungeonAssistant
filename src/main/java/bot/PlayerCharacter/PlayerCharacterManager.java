package bot.PlayerCharacter;

import bot.Exception.NoPlayerCharacterFoundException;
import bot.Player.PlayerManager;

import java.util.ArrayList;

public class PlayerCharacterManager {

    public static void createPlayerCharacter(
        String userId,
        String name,
        int strength,
        int defense,
        int agility,
        int wisdom,
        int hitpoints
    ) {
        PlayerManager.ensurePlayerExist(userId);
        PlayerCharacterRepository.insertPlayerCharacter(userId, name, strength, defense, agility, wisdom, hitpoints);
    }

    public static void deletePlayerCharacter(String userId, String name) {
        PlayerManager.ensurePlayerExist(userId);
        PlayerCharacterRepository.deletePlayerCharacter(userId, name);
    }

    public static ArrayList<PlayerCharacter> getAllMyPCs(String playerId) {
        return PlayerCharacterRepository.getAllMyPCs(playerId);
    }

    public static PlayerCharacter getMyPC(String playerId, String name) {
        PlayerCharacter playerCharacter = PlayerCharacterRepository.getMyPC(playerId, name);
        if (playerCharacter == null) {
            throw NoPlayerCharacterFoundException.create(name);
        }
        return playerCharacter;
    }

    public static void main(String[] args) {
        PlayerCharacterRepository.createTableIfNotExists();
    }

}
