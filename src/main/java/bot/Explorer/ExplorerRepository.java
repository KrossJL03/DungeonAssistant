package bot.Explorer;

import bot.Player.Player;
import bot.Player.PlayerRepository;
import bot.Registry.RegistryException;
import bot.Registry.RegistryPaths;

import java.sql.*;
import java.util.ArrayList;

class ExplorerRepository
{
    private static String TABLE_NAME = "explorer";

    static void deleteExplorer(String playerId, String name) {
        String sql = String.format(
            "DELETE FROM %s WHERE playerId = '%s' AND name = '%s' COLLATE NOCASE",
            ExplorerRepository.TABLE_NAME,
            playerId,
            name
        );
        ExplorerRepository.executeUpdate(sql);
    }

    static void insertExplorer(
        String playerId,
        String name,
        int strength,
        int defense,
        int agility,
        int wisdom,
        int hitpoints,
        String appLink,
        String statsLink
    ) {
        String sql =
            String.format(
                "INSERT OR REPLACE INTO %s(playerId, name, hitpoints, strength, wisdom, agility, defense, appLink, statsLink)",
                ExplorerRepository.TABLE_NAME
            ) +
            String.format(
                "VALUES('%s','%s',%d,%d,%d,%d,%d,'%s','%s')",
                playerId,
                name,
                hitpoints,
                strength,
                wisdom,
                agility,
                defense,
                appLink,
                statsLink
            );
        ExplorerRepository.executeUpdate(sql);
    }

    static ArrayList<Explorer> getAllMyExplorers(String playerId) {
        String sql = String.format(
            "SELECT * FROM %s WHERE playerId = '%s';",
            ExplorerRepository.TABLE_NAME,
            playerId
        );
        Connection          connection = null;
        Statement           statement  = null;
        RegistryException   exception  = null;
        ArrayList<Explorer> explorers  = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    Player player = PlayerRepository.getPlayer(resultSet.getString("playerId"));
                    Explorer explorer = new Explorer(
                        resultSet.getString("name"),
                        player,
                        resultSet.getInt("hitpoints"), resultSet.getInt("strength"),
                        resultSet.getInt("defense"),
                        resultSet.getInt("agility"),
                        resultSet.getInt("wisdom"),
                        resultSet.getString("appLink"),
                        resultSet.getString("statsLink")
                    );
                    explorers.add(explorer);
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
                exception = RegistryException.createFailedToCloseConnection();
            }
        }
        if (exception != null) {
            throw exception;
        }
        return explorers;
    }

    static ArrayList<Explorer> getAllExplorers() {
        String sql = String.format(
            "SELECT * FROM %s ORDER BY name;",
            ExplorerRepository.TABLE_NAME
        );
        Connection          connection = null;
        Statement           statement  = null;
        RegistryException   exception  = null;
        ArrayList<Explorer> explorers  = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    Player player = PlayerRepository.getPlayer(resultSet.getString("playerId"));
                    Explorer explorer = new Explorer(
                        resultSet.getString("name"),
                        player,
                        resultSet.getInt("hitpoints"), resultSet.getInt("strength"),
                        resultSet.getInt("defense"),
                        resultSet.getInt("agility"),
                        resultSet.getInt("wisdom"),
                        resultSet.getString("appLink"),
                        resultSet.getString("statsLink")
                    );
                    explorers.add(explorer);
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
                exception = RegistryException.createFailedToCloseConnection();
            }
        }
        if (exception != null) {
            throw exception;
        }
        return explorers;
    }

    static Explorer getMyExplorer(String playerId, String name) {
        Connection connection = null;
        Statement  statement  = null;
        String sql = String.format(
            "SELECT * FROM %s WHERE playerId = '%s' AND lower(name) = '%s'",
            ExplorerRepository.TABLE_NAME,
            playerId,
            name.toLowerCase()
        );
        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                Player player = PlayerRepository.getPlayer(resultSet.getString("playerId"));
                return new Explorer(
                    resultSet.getString("name"),
                    player,
                    resultSet.getInt("hitpoints"), resultSet.getInt("strength"),
                    resultSet.getInt("defense"),
                    resultSet.getInt("agility"),
                    resultSet.getInt("wisdom"),
                    resultSet.getString("appLink"),
                    resultSet.getString("statsLink")
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
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s", ExplorerRepository.TABLE_NAME) +
                     "(" +
                     " playerId  TEXT NOT NULL, " +
                     " name      TEXT NOT NULL , " +
                     " hitpoints INT  NOT NULL, " +
                     " strength  INT  NOT NULL, " +
                     " wisdom    INT  NOT NULL, " +
                     " agility   INT  NOT NULL, " +
                     " defense   INT  NOT NULL, " +
                     " appLink   TEXT NOT NULL, " +
                     " statsLink TEXT NOT NULL, " +
                     " unique (playerId, name)  " +
                     ")";
        ExplorerRepository.executeUpdate(sql);
    }

    private static void executeUpdate(String sql) {
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
