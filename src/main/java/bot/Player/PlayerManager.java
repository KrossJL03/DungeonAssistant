package bot.Player;

public class PlayerManager {

    public static void ensurePlayerExist(String userId) {
        if(!PlayerRepository.doesPlayerExist(userId)) {
            throw PlayerRepositoryException.createNotFound();
        }
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
