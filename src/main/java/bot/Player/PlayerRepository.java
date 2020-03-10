package bot.Player;

import bot.Registry.RegistryException;
import bot.Registry.RegistryPaths;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PlayerRepository
{
    private static String COLUMN_CUMULUS = "cumulus";
    private static String COLUMN_IS_MOD  = "isMod";
    private static String COLUMN_NAME    = "name";
    private static String COLUMN_USER_ID = "userId";
    private static String TABLE_NAME     = "player";

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
            "SELECT * FROM %s WHERE %s = '%s'",
            TABLE_NAME,
            COLUMN_USER_ID,
            userId
        );

        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                player = new Player(
                    resultSet.getString(COLUMN_USER_ID),
                    resultSet.getString(COLUMN_NAME),
                    resultSet.getInt(COLUMN_CUMULUS),
                    resultSet.getBoolean(COLUMN_IS_MOD)
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
            "SELECT %s FROM %s WHERE lower(name) = '%s'",
            COLUMN_NAME,
            TABLE_NAME,
            playerName
        );

        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                playerId = resultSet.getString(COLUMN_USER_ID);
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

    /**
     * Is this player a mod
     *
     * @param userId User id
     *
     * @return boolean
     *
     * @throws RegistryException         If failure during retrieval
     *                                   If failed to close statement
     * @throws PlayerRepositoryException If player does not exist
     */
    public static boolean isModPlayer(@NotNull String userId)
    {
        Connection        connection = null;
        Statement         statement  = null;
        RegistryException exception  = null;
        String sql = String.format(
            "SELECT %s FROM %s WHERE %s = '%s'",
            COLUMN_IS_MOD,
            TABLE_NAME,
            COLUMN_USER_ID,
            userId
        );

        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                return resultSet.getBoolean(COLUMN_IS_MOD);
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

        throw PlayerRepositoryException.createNotFound();
    }

    static void createTableIfNotExists()
    {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s ", PlayerRepository.TABLE_NAME) +
                     "(" +
                     String.format(" %s TEXT PRIMARY KEY NOT NULL,     ", COLUMN_USER_ID) +
                     String.format(" %s TEXT             NOT NULL,     ", COLUMN_NAME) +
                     String.format(" %s INT              DEFAULT 0     ", COLUMN_CUMULUS) +
                     String.format(" %s BOOL             DEFAULT FALSE ", COLUMN_IS_MOD) +
                     ")";
        PlayerRepository.executeUpdate(sql);
    }

    static boolean doesPlayerExist(String userId)
    {
        Connection        connection = null;
        Statement         statement  = null;
        RegistryException exception;
        String sql = String.format(
            "SELECT COUNT(*) FROM %s WHERE %s = '%s'",
            TABLE_NAME,
            COLUMN_USER_ID,
            userId
        );
        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
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

        throw exception;
    }

    static void insertPlayer(String userId, String name)
    {
        String sql = String.format(
            "INSERT INTO %s(%s,%s) VALUES('%s','%s')",
            TABLE_NAME,
            COLUMN_USER_ID,
            COLUMN_NAME,
            userId,
            name
        );
        PlayerRepository.executeUpdate(sql);
    }

    static void updatePlayer(String userId, String name)
    {
        String sql = String.format(
            "UPDATE %s SET %s = '%s' WHERE userId = '%s'",
            TABLE_NAME,
            COLUMN_NAME,
            name,
            userId
        );
        PlayerRepository.executeUpdate(sql);
    }

    private static void executeUpdate(String sql)
    {
        Connection        connection = null;
        Statement         statement  = null;
        RegistryException exception  = null;
        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
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
