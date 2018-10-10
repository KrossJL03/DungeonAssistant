package bot.PlayerCharacter;

import bot.Player.Player;
import bot.Player.PlayerRepository;
import bot.Repository.RepositoryException;
import bot.Repository.RepositoryPaths;

import java.sql.*;
import java.util.ArrayList;

class PlayerCharacterRepository {

    private static String TABLE_NAME = "playerCharacter";

    static void deletePlayerCharacter(String playerId, String name) {
        String sql = String.format(
            "DELETE FROM %s WHERE playerId = '%s' AND name = '%s' COLLATE NOCASE",
            PlayerCharacterRepository.TABLE_NAME,
            playerId,
            name
        );
        PlayerCharacterRepository.executeUpdate(sql);
    }

    static void insertPlayerCharacter(
        String playerId,
        String name,
        int strength,
        int defense,
        int agility,
        int wisdom,
        int hitpoints
    ) {
        PlayerCharacter.validateStats(name, strength, defense, agility, wisdom, hitpoints);
        String sql =
            String.format(
                "INSERT OR REPLACE INTO %s(playerId, name, strength, defense, agility, wisdom, hitpoints)",
                PlayerCharacterRepository.TABLE_NAME
            ) +
            String.format(
                "VALUES('%s','%s',%d,%d,%d,%d,%d)",
                playerId,
                name,
                strength,
                defense,
                agility,
                wisdom,
                hitpoints
            );
        PlayerCharacterRepository.executeUpdate(sql);
    }

    static ArrayList<PlayerCharacter> getAllMyPCs(String playerId) {
        String sql = String.format(
            "SELECT rowid, * FROM %s WHERE playerId = '%s';",
            PlayerCharacterRepository.TABLE_NAME,
            playerId
        );
        Connection                 connection       = null;
        Statement                  statement        = null;
        RepositoryException        exception        = null;
        ArrayList<PlayerCharacter> playerCharacters = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(RepositoryPaths.getDatabasePath("database"));
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    Player player = PlayerRepository.getPlayer(resultSet.getString("playerId"));
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
                exception = RepositoryException.createFailedToCloseConnection();
            }
        }
        if (exception != null) {
            throw exception;
        }
        return playerCharacters;
    }

    static PlayerCharacter getMyPC(String playerId, String name) {
        Connection connection = null;
        Statement  statement  = null;
        String sql = String.format(
            "SELECT rowid, * FROM %s WHERE playerId = '%s' AND name = '%s'",
            PlayerCharacterRepository.TABLE_NAME,
            playerId,
            name
        );
        try {
            connection = DriverManager.getConnection(RepositoryPaths.getDatabasePath("database"));
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                Player player = PlayerRepository.getPlayer(resultSet.getString("playerId"));
                return new PlayerCharacter(
                    resultSet.getInt("rowid"),
                    resultSet.getString("name"),
                    player,
                    resultSet.getInt("strength"),
                    resultSet.getInt("defense"),
                    resultSet.getInt("agility"),
                    resultSet.getInt("wisdom"),
                    resultSet.getInt("hitpoints")
                );
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
        return null;
    }

    static void createTableIfNotExists() {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s", PlayerCharacterRepository.TABLE_NAME) +
                     "(" +
                     " playerId   TEXT NOT NULL, " +
                     " name      TEXT NOT NULL, " +
                     " strength  INT  NOT NULL, " +
                     " defense   INT  NOT NULL, " +
                     " agility   INT  NOT NULL, " +
                     " wisdom    INT  NOT NULL, " +
                     " hitpoints INT  NOT NULL, " +
                     " PRIMARY KEY (playerId, name COLLATE NOCASE)" +
                     ")";
        PlayerCharacterRepository.executeUpdate(sql);
    }

    private static void executeUpdate(String sql) {
        Connection          connection = null;
        Statement           statement  = null;
        RepositoryException exception  = null;
        try {
            connection = DriverManager.getConnection(RepositoryPaths.getDatabasePath("database"));
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (Throwable e) {
            exception = RepositoryException.createFailedToUpdate();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Throwable e) {
                exception = RepositoryException.createFailedToCloseConnection();
            }
        }
        if (exception != null) {
            throw exception;
        }
    }
}
