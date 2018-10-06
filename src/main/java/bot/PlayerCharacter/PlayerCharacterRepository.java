package bot.PlayerCharacter;

import bot.Player.Player;
import bot.Player.PlayerRepository;
import bot.Repository.RepositoryPaths;

import java.sql.*;
import java.util.ArrayList;

public class PlayerCharacterRepository {

    private static String TABLE_NAME = "player_character";

    public static void createPlayerCharacter(
        String ownerId,
        String name,
        int strength,
        int defense,
        int agility,
        int wisdom,
        int hitpoints
    ) {
        PlayerCharacter.validateStats(name, strength, defense, agility, wisdom, hitpoints);
        String sql =
            "INSERT INTO player_character(owner_id, name, strength, defense, agility, wisdom, hitpoints)" +
            String.format(
                "VALUES('%s','%s',%d,%d,%d,%d,%d)",
                ownerId,
                name,
                strength,
                defense,
                agility,
                wisdom,
                hitpoints
            );
        PlayerCharacterRepository.executeUpdate(sql);
    }

    public static ArrayList<PlayerCharacter> getAllPC() {
        String sql = "SELECT rowid, * FROM player_character;";
        return PlayerCharacterRepository.executeQuery(sql);
    }

    public static ArrayList<PlayerCharacter> getAllMyPCs(String ownerId) {
        String sql = String.format("SELECT rowid, * FROM %s WHERE owner_id = '%s'", TABLE_NAME, ownerId);
        return PlayerCharacterRepository.executeQuery(sql);
    }

    public static PlayerCharacter getMyPC(String ownerId, String name) {
        String sql = String.format(
            "SELECT rowid, * FROM %s WHERE owner_id = '%s' AND name = '%s'",
            TABLE_NAME,
            ownerId,
            name
        );
        ArrayList<PlayerCharacter> playerCharacters = PlayerCharacterRepository.executeQuery(sql);
        if (playerCharacters.isEmpty()) {
            return null;
        } else if (playerCharacters.size() == 1) {
            return playerCharacters.get(0);
        }
        // todo throw multiple characters with same name exception
        return null;
    }

    public static void removePlayerCharacterIfExists(String ownerId, String name) {
        String sql = String.format(
            "DELETE FROM %s WHERE owner_id = '%s' AND name = '%s'",
            PlayerCharacterRepository.TABLE_NAME,
            ownerId,
            name
        );
        PlayerCharacterRepository.executeUpdate(sql);
    }

    public static void main(String[] args) {
    }

    private static void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS player_character (" +
                     " owner_id    TEXT            NOT NULL, " +
                     " name        TEXT            NOT NULL, " +
                     " strength    INT             NOT NULL, " +
                     " defense     INT             NOT NULL, " +
                     " agility     INT             NOT NULL, " +
                     " wisdom      INT             NOT NULL, " +
                     " hitpoints   INT             NOT NULL " +
                     ")";
        PlayerCharacterRepository.executeUpdate(sql);
    }

    private static ArrayList<PlayerCharacter> executeQuery(String sql) {
        Connection                 connection       = null;
        Statement                  statement        = null;
        ArrayList<PlayerCharacter> playerCharacters = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(RepositoryPaths.getDatabasePath("database"));
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    Player player = PlayerRepository.getPlayer(resultSet.getString("owner_id"));
                    PlayerCharacter playerCharacter = new PlayerCharacter(
                        resultSet.getInt("rowid"),
                        resultSet.getString("name"),
                        player,
                        resultSet.getInt("strength"),
                        resultSet.getInt("defense"),
                        resultSet.getInt("agility"),
                        resultSet.getInt("wisdom"),
                        resultSet.getInt("hitpoints")
                    );
                    playerCharacters.add(playerCharacter);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Throwable e) {
                System.out.println("Failed to close");
            }
        }
        return playerCharacters;
    }

    private static void executeUpdate(String sql) {
        Connection connection = null;
        Statement  statement  = null;
        try {
            connection = DriverManager.getConnection(RepositoryPaths.getDatabasePath("database"));
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Throwable e) {
                System.out.println("Failed to close");
            }
        }
    }
}
