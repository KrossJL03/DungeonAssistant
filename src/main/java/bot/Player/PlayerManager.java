package bot.Player;

import bot.Player.Exception.PlayerNotFoundException;

public class PlayerManager {

    public static void ensurePlayerExist(String userId) {
        if(!PlayerRepository.doesPlayerExist(userId)) {
            throw PlayerNotFoundException.createNotInDatabase();
        }
    }

    public static Player getPlayer(String userId) {
        return PlayerRepository.getPlayer(userId);
    }

    public static void savePlayer(String userId, String name) {
        if (PlayerRepository.doesPlayerExist(userId)) {
            PlayerRepository.updatePlayer(userId, name);
        } else {
            PlayerRepository.insertPlayer(userId, name);
        }
    }

    public static void main(String[] args) {
        PlayerRepository.createTableIfNotExists();
    }

}
