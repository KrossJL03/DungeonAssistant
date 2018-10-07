package bot.Hostile;

import bot.Repository.RepositoryPaths;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class HostileRepository {

    static String TABLE_NAME = "hostile";

    static void insertHostile(String species, int dangerLevel, int hitpoints, int attackDice) {
        String sql = String.format(
            "INSERT INTO %s(species, danger_level, hitpoints, attack_dice) VALUES('%s',%d,%d,%d)",
            HostileRepository.TABLE_NAME,
            species,
            dangerLevel,
            hitpoints,
            attackDice
        );
        HostileRepository.executeUpdate(sql);
    }

    public static void deleteHostileIfExists(String species) {
        String sql = String.format("DELETE FROM %s WHERE species = '%s'", HostileRepository.TABLE_NAME, species);
        HostileRepository.executeUpdate(sql);
    }

    public static ArrayList<Hashtable<String, String>> getAllHostiles() {
        Connection                           connection   = null;
        Statement                            statement    = null;
        ArrayList<Hashtable<String, String>> hostileInfos = new ArrayList<>();
        String sql = String.format(
            "SELECT species, danger_level, hitpoints, attack_dice FROM %s ORDER BY danger_level;",
            HostileRepository.TABLE_NAME
        );
        try {
            connection = DriverManager.getConnection(RepositoryPaths.getDatabasePath("database"));
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    Hashtable<String, String> hostileInfo = new Hashtable<>();
                    hostileInfo.put("species", resultSet.getString("species"));
                    hostileInfo.put("danger_level", resultSet.getString("danger_level"));
                    hostileInfo.put("hitpoints", resultSet.getString("hitpoints"));
                    hostileInfo.put("attack_dice", resultSet.getString("attack_dice"));
                    hostileInfos.add(hostileInfo);
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
        return hostileInfos;
    }

    public static Hostile getHostile(String species) {
        String sql = String.format(
            "SELECT h.rowid, h.*, l.* " +
            "FROM %s h " +
            "INNER JOIN %s l ON l.hostile_id = h.rowid " +
            "WHERE lower(h.species) = '%s';",
            HostileRepository.TABLE_NAME,
            LootRepository.TABLE_NAME,
            species.toLowerCase()
        );

        Connection             connection = null;
        Statement              statement  = null;
        HashMap<Integer, Loot> lootList   = HostileRepository.getBlankLootList();

        try {
            connection = DriverManager.getConnection(RepositoryPaths.getDatabasePath("database"));
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                int    id          = resultSet.getInt("rowid");
                String species2    = resultSet.getString("species");
                int    dangerLevel = resultSet.getInt("danger_level");
                int    hitpoints   = resultSet.getInt("hitpoints");
                int    attackDice  = resultSet.getInt("attack_dice");
                while (resultSet.next()) {
                    Loot loot = new Loot(
                        resultSet.getInt("dice_roll"),
                        resultSet.getString("item_name"),
                        resultSet.getInt("quantity")
                    );
                    lootList.put(loot.getDiceRoll(), loot);
                }
                return new Hostile(id, species2, dangerLevel, hitpoints, attackDice, lootList);
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

    static int getHostileId(String species) {
        String sql = String.format(
            "SELECT h.rowid " +
            "FROM %s h " +
            "WHERE lower(h.species) = '%s'",
            HostileRepository.TABLE_NAME,
            species.toLowerCase()
        );

        Connection connection = null;
        Statement  statement  = null;
        try {
            connection = DriverManager.getConnection(RepositoryPaths.getDatabasePath("database"));
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet.getInt("rowid");
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
        return -1;
    }

    static void updateHostile(String species, int dangerLevel, int hitpoints, int attackDice) {
        String sql = String.format(
            "REPLACE INTO %s(species, danger_level, hitpoints, attack_dice) VALUES('%s',%d,%d,%d)",
            HostileRepository.TABLE_NAME,
            species,
            dangerLevel,
            hitpoints,
            attackDice
        );
        HostileRepository.executeUpdate(sql);
    }

    static void createTableIfNotExists() {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s", HostileRepository.TABLE_NAME) +
                     "(" +
                     " species      TEXT PRIMARY KEY NOT NULL, " +
                     " danger_level INT  DEFAULT 1   NOT NULL, " +
                     " hitpoints    INT              NOT NULL, " +
                     " attack_dice  INT              NOT NULL " +
                     ")";
        HostileRepository.executeUpdate(sql);
    }

    private static ResultSet executeQueryTest(String sql) {
        Connection connection = null;
        Statement  statement  = null;
        try {
            connection = DriverManager.getConnection(RepositoryPaths.getDatabasePath("database"));
            statement = connection.createStatement();
            return statement.executeQuery(sql);
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

    private static HashMap<Integer, Loot> getBlankLootList() {
        HashMap<Integer, Loot> lootList = new HashMap<>();
        for (int i = 1; i < 11; i++) {
            lootList.put(i, new Loot(i, null, 0));
        }
        return lootList;
    }
}
