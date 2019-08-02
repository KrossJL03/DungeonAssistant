package bot.Player;

import bot.Registry.RegistryException;
import bot.Registry.RegistryPaths;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

public class PlayerRepository
{

    private static String TABLE_NAME = "player";

    static void createTableIfNotExists()
    {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s ", PlayerRepository.TABLE_NAME) +
                     "(" +
                     " userId  TEXT PRIMARY KEY NOT NULL, " +
                     " name    TEXT             NOT NULL, " +
                     " cumulus INT              DEFAULT 0 " +
                     ")";
        PlayerRepository.executeUpdate(sql);
    }

    static boolean doesPlayerExist(String userId)
    {
        Connection        connection = null;
        Statement         statement  = null;
        RegistryException exception;
        String sql = String.format(
            "SELECT COUNT(*) FROM %s WHERE userId = '%s'",
            PlayerRepository.TABLE_NAME,
            userId
        );
        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath("database"));
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet != null && resultSet.getBoolean("COUNT(*)");
        } catch (Throwable e) {
            exception = RegistryException.createFailedToUpdate();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Throwable e) {
                exception = RegistryException.createFailedToCloseConnection();
            }
        }
        if (exception != null) {
            throw exception;
        }
        return false;
    }

    /**
     * Get player
     *
     * @param userId User id
     *
     * @return Player
     *
     * @throws RegistryException         If failure during retrieval
     *                                   If failed to close statement
     * @throws PlayerRepositoryException If player does not exist
     */
    public static @NotNull Player getPlayer(@NotNull String userId)
    {
        Player            player     = null;
        Connection        connection = null;
        Statement         statement  = null;
        RegistryException exception  = null;
        String sql = String.format(
            "SELECT * FROM %s WHERE userId = '%s'",
            PlayerRepository.TABLE_NAME,
            userId
        );

        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath("database"));
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                player = new Player(
                    resultSet.getString("userId"),
                    resultSet.getString("name"),
                    resultSet.getInt("cumulus")
                );
            }
        } catch (Throwable e) {
            exception = RegistryException.createFailedToRetrieve();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Throwable e) {
                exception = RegistryException.createFailedToCloseConnection();
            }
        }
        if (exception != null) {
            throw exception;
        }

        if (player == null) {
            throw PlayerRepositoryException.createNotFound();
        }
        return player;
    }

    /**
     * Get player id by name
     *
     * @param playerName Name
     *
     * @return String
     *
     * @throws RegistryException         If failure during retrieval
     *                                   If failed to close statement
     * @throws PlayerRepositoryException If player does not exist
     */
    public static @NotNull String getPlayerIdByName(@NotNull String playerName)
    {
        String            playerId   = null;
        Connection        connection = null;
        Statement         statement  = null;
        RegistryException exception  = null;
        String sql = String.format(
            "SELECT userId FROM %s WHERE lower(name) = '%s'",
            PlayerRepository.TABLE_NAME,
            playerName
        );

        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath("database"));
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                playerId = resultSet.getString("userId");
            }
        } catch (Throwable e) {
            exception = RegistryException.createFailedToRetrieve();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Throwable e) {
                exception = RegistryException.createFailedToCloseConnection();
            }
        }
        if (exception != null) {
            throw exception;
        }

        if (playerId == null) {
            throw PlayerRepositoryException.createNotFoundByName(playerName);
        }
        return playerId;
    }

    static void insertPlayer(String userId, String name)
    {
        String sql = String.format(
            "INSERT INTO %s(userId, name) VALUES('%s','%s')",
            PlayerRepository.TABLE_NAME,
            userId,
            name
        );
        PlayerRepository.executeUpdate(sql);
    }

    static void updatePlayer(String userId, String name)
    {
        String sql = String.format(
            "UPDATE %s SET name = '%s' WHERE userId = '%s'",
            PlayerRepository.TABLE_NAME,
            userId,
            name
        );
        PlayerRepository.executeUpdate(sql);
    }

    private static void executeUpdate(String sql)
    {
        Connection        connection = null;
        Statement         statement  = null;
        RegistryException exception  = null;
        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath("database"));
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (Throwable e) {
            exception = RegistryException.createFailedToUpdate();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Throwable e) {
                exception = RegistryException.createFailedToCloseConnection();
            }
        }
        if (exception != null) {
            throw exception;
        }
    }
}
