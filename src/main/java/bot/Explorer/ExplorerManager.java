package bot.Explorer;

import bot.Player.Player;
import bot.Explorer.Exception.ExplorerNotFoundException;
import bot.Player.PlayerManager;
import bot.Explorer.Exception.NotYourExplorerException;
import bot.Player.PlayerRepository;

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
        int wisdom,
        String appLink,
        String statsLink
    )
    {
        PlayerManager.ensurePlayerExist(userId);
        Explorer.validateStats(name, hitpoints, strength, defense, agility, wisdom);
        ExplorerRepository.insertExplorer(
            userId,
            name,
            strength,
            defense,
            agility,
            wisdom,
            hitpoints,
            appLink,
            statsLink
        );
    }

    public static void deleteExplorer(String userId, String name)
    {
        PlayerManager.ensurePlayerExist(userId);
        ExplorerRepository.deleteExplorer(userId, name);
    }

    public static ArrayList<Explorer> getAllMyExplorers(String playerId)
    {
        return ExplorerRepository.getAllMyExplorers(playerId);
    }

    public static ArrayList<Explorer> getAllExplorers()
    {
        return ExplorerRepository.getAllExplorers();
    }

    public static Explorer getMyExplorer(String playerId, String name)
    {
        Explorer explorer = ExplorerRepository.getMyExplorer(playerId, name);
        if (explorer == null) {
            ArrayList<Explorer> alternativeExplorers = ExplorerManager.getAllMyExplorers(playerId);
            if (!alternativeExplorers.isEmpty()) {
                throw ExplorerNotFoundException.createNotInDatabaseWithAlternatives(name, alternativeExplorers);
            } else {
                throw ExplorerNotFoundException.createNotInDatabase(name);
            }
        }
        return explorer;
    }

    public static void main(String[] args)
    {
        ExplorerRepository.createTableIfNotExists();
    }
}
