package bot.Explorer;

import bot.Player.Player;
import bot.Explorer.Exception.ExplorerNotFoundException;
import bot.Player.PlayerManager;
import bot.Explorer.Exception.NotYourExplorerException;

import java.util.ArrayList;

public class ExplorerManager
{
    public static void createExplorer(
        String userId,
        String name,
        int hitpoints,
        int strength,
        int defense,
        int agility,
        int wisdom
    ) {
        PlayerManager.ensurePlayerExist(userId);
        Player   owner    = PlayerManager.getPlayer(userId);
        Explorer explorer = ExplorerRepository.getExplorer(name);
        if (explorer != null && !explorer.isOwner(owner)) {
            throw NotYourExplorerException.createForNameTaken(
                explorer.getName(),
                explorer.getOwner().getName()
            );
        }
        Explorer.validateStats(name, hitpoints, strength, defense, agility, wisdom);
        ExplorerRepository.insertExplorer(userId, name, strength, defense, agility, wisdom, hitpoints);
    }

    public static void deleteExplorer(String userId, String name) {
        PlayerManager.ensurePlayerExist(userId);
        ExplorerRepository.deleteExplorer(userId, name);
    }

    public static ArrayList<Explorer> getAllMyExplorers(String playerId) {
        return ExplorerRepository.getAllMyExplorers(playerId);
    }

    public static ArrayList<Explorer> getAllExplorers() {
        return ExplorerRepository.getAllExplorers();
    }

    public static Explorer getMyExplorer(String playerId, String name) {
        Explorer explorer = ExplorerRepository.getMyExplorer(playerId, name);
        if (explorer == null) {
            throw ExplorerNotFoundException.createNotInDatabase(name);
        }
        return explorer;
    }

    public static void main(String[] args) {
        ExplorerRepository.createTableIfNotExists();
    }
}
