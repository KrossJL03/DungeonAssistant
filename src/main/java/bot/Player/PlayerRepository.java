package bot.Player;

import bot.Repository.RepositoryException;
import bot.Repository.RepositoryPaths;

import java.sql.*;

public class PlayerRepository {

    private static String TABLE_NAME = "player";

    static void createTableIfNotExists() {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s ", PlayerRepository.TABLE_NAME) +
                     "(" +
                     " userId  TEXT PRIMARY KEY NOT NULL, " +
                     " name    TEXT             NOT NULL, " +
                     " cumulus INT              DEFAULT 0 " +
                     ")";
        PlayerRepository.executeUpdate(sql);
    }

    static boolean doesPlayerExist(String userId) {
        Connection connection = null;
        Statement  statement  = null;
        String sql = String.format(
            "SELECT COUNT(*) FROM %s WHERE userId = '%s'",
            PlayerRepository.TABLE_NAME,
            userId
        );
        try {
            connection = DriverManager.getConnection(RepositoryPaths.getDatabasePath("database"));
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet != null && resultSet.getBoolean("COUNT(*)");
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
        // todo throw exception
        return false;
    }

    public static Player getPlayer(String userId) {
        Connection connection = null;
        Statement  statement  = null;
        String sql = String.format(
            "SELECT * FROM %s WHERE userId = '%s'",
            PlayerRepository.TABLE_NAME,
            userId
        );
        try {
            connection = DriverManager.getConnection(RepositoryPaths.getDatabasePath("database"));
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                return new Player(
                    resultSet.getRow(),
                    resultSet.getString("userId"),
                    resultSet.getString("name"),
                    resultSet.getInt("cumulus")
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

    static void insertPlayer(String userId, String name) {
        String sql = String.format(
            "INSERT INTO %s(userId, name) VALUES('%s','%s')",
            PlayerRepository.TABLE_NAME,
            userId,
            name
        );
        PlayerRepository.executeUpdate(sql);
    }

    static void updatePlayer(String userId, String name) {
        String sql = String.format(
            "UPDATE %s SET name = '%s' WHERE userId = '%s'",
            PlayerRepository.TABLE_NAME,
            userId,
            name
        );
        PlayerRepository.executeUpdate(sql);
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
