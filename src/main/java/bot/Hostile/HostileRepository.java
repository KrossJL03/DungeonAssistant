package bot.Hostile;

import bot.Hostile.Exception.HostileNotFoundException;
import bot.Registry.RegistryPaths;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class HostileRepository
{
    static         String TABLE_NAME          = "hostile";
    private static String COLUMN_ID           = "rowId";
    private static String COLUMN_SPECIES      = "species";
    private static String COLUMN_DANGER_LEVEL = "dangerLevel";
    private static String COLUMN_HITPOINTS    = "hitpoints";
    private static String COLUMN_ATTACK       = "attack";
    private static String COLUMN_ATTACK_COUNT = "attackCount";
    private static String COLUMN_LOOT_ROLLS   = "lootRollCount";
    private static String COLUMN_IS_VIEWABLE  = "isViewable";

    /**
     * Insert hostile
     *
     * @param hostile Hostile to insert
     */
    static void insertHostile(@NotNull Hostile hostile)
    {
        String sql = String.format(
            "INSERT INTO %s(%s,%s,%s,%s,%s,%s,%s) VALUES('%s',%d,%d,%d,%s,%s,'%s')",
            TABLE_NAME,
            COLUMN_SPECIES,
            COLUMN_DANGER_LEVEL,
            COLUMN_HITPOINTS,
            COLUMN_ATTACK,
            COLUMN_ATTACK_COUNT,
            COLUMN_LOOT_ROLLS,
            COLUMN_IS_VIEWABLE,
            hostile.getSpecies(),
            hostile.getDangerLevel(),
            hostile.getHitpoints(),
            hostile.getAttack(),
            hostile.getAttackCount(),
            hostile.getLootRollCount(),
            hostile.isViewable()
        );
        HostileRepository.executeUpdate(sql);
    }

    /**
     * Get info for all hostiles
     *
     * @return ArrayList
     */
    public static ArrayList<Hashtable<String, String>> getInfoForAllHostiles()
    {
        Connection                           connection   = null;
        Statement                            statement    = null;
        ArrayList<Hashtable<String, String>> hostileInfos = new ArrayList<>();
        String sql = String.format(
            "SELECT %s,%s,%s,%s,%s FROM %s ORDER BY %s,%s,%s;",
            COLUMN_SPECIES,
            COLUMN_DANGER_LEVEL,
            COLUMN_HITPOINTS,
            COLUMN_ATTACK,
            COLUMN_ATTACK_COUNT,
            TABLE_NAME,
            COLUMN_DANGER_LEVEL,
            COLUMN_HITPOINTS,
            COLUMN_ATTACK
        );
        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    Hashtable<String, String> hostileInfo = new Hashtable<>();
                    hostileInfo.put(COLUMN_SPECIES, resultSet.getString(COLUMN_SPECIES));
                    hostileInfo.put(COLUMN_DANGER_LEVEL, resultSet.getString(COLUMN_DANGER_LEVEL));
                    hostileInfo.put(COLUMN_HITPOINTS, resultSet.getString(COLUMN_HITPOINTS));
                    hostileInfo.put(COLUMN_ATTACK, resultSet.getString(COLUMN_ATTACK));
                    hostileInfo.put(COLUMN_ATTACK_COUNT, resultSet.getString(COLUMN_ATTACK_COUNT));
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

    /**
     * Get info for viewable hostiles
     *
     * @return ArrayList
     */
    public static ArrayList<Hashtable<String, String>> getInfoForViewablelHostiles()
    {
        Connection                           connection   = null;
        Statement                            statement    = null;
        ArrayList<Hashtable<String, String>> hostileInfos = new ArrayList<>();
        String sql = String.format(
            "SELECT %s,%s,%s,%s,%s FROM %s WHERE %s = 'true' ORDER BY %s,%s,%s;",
            COLUMN_SPECIES,
            COLUMN_DANGER_LEVEL,
            COLUMN_HITPOINTS,
            COLUMN_ATTACK,
            COLUMN_ATTACK_COUNT,
            TABLE_NAME,
            COLUMN_IS_VIEWABLE,
            COLUMN_DANGER_LEVEL,
            COLUMN_HITPOINTS,
            COLUMN_ATTACK
        );
        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    Hashtable<String, String> hostileInfo = new Hashtable<>();
                    hostileInfo.put(COLUMN_SPECIES, resultSet.getString(COLUMN_SPECIES));
                    hostileInfo.put(COLUMN_DANGER_LEVEL, resultSet.getString(COLUMN_DANGER_LEVEL));
                    hostileInfo.put(COLUMN_HITPOINTS, resultSet.getString(COLUMN_HITPOINTS));
                    hostileInfo.put(COLUMN_ATTACK, resultSet.getString(COLUMN_ATTACK));
                    hostileInfo.put(COLUMN_ATTACK_COUNT, resultSet.getString(COLUMN_ATTACK_COUNT));
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

    /**
     * Get hostile by species
     *
     * @param species Species of hostile to retrieve
     *
     * @return Hostile
     *
     * @throws HostileNotFoundException If hostile not found
     */
    public static @NotNull Hostile getHostile(@NotNull String species) throws HostileNotFoundException
    {
        String sql = String.format(
            "SELECT h.%s, h.*, l.* " +
            "FROM %s h " +
            "INNER JOIN %s l ON l.hostile_id = h.rowId " +
            "WHERE lower(h.%s) = '%s';",
            COLUMN_ID,
            TABLE_NAME,
            LootRepository.TABLE_NAME,
            COLUMN_SPECIES,
            species.toLowerCase()
        );

        Connection             connection = null;
        Statement              statement  = null;
        HashMap<Integer, Loot> lootList   = new HashMap<>();

        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                String  species2      = resultSet.getString(COLUMN_SPECIES);
                int     dangerLevel   = resultSet.getInt(COLUMN_DANGER_LEVEL);
                int     hitpoints     = resultSet.getInt(COLUMN_HITPOINTS);
                int     attackDice    = resultSet.getInt(COLUMN_ATTACK);
                int     attackCount   = resultSet.getInt(COLUMN_ATTACK_COUNT);
                int     lootRollCount = resultSet.getInt(COLUMN_LOOT_ROLLS);
                boolean isViewable    = resultSet.getBoolean(COLUMN_IS_VIEWABLE);
                while (resultSet.next()) {
                    Loot loot = new Loot(
                        resultSet.getInt("dice_roll"),
                        resultSet.getString("item_name"),
                        resultSet.getInt("quantity")
                    );
                    lootList.put(loot.getDiceRoll(), loot);
                }
                return new Hostile(
                    species2,
                    dangerLevel,
                    hitpoints,
                    attackDice,
                    attackCount,
                    lootRollCount,
                    lootList,
                    isViewable
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

        throw HostileNotFoundException.createNotInDatabase(species);
    }

    /**
     * Get hostile id by species
     *
     * @param species Species of hostile id to retrieve
     *
     * @return int
     */
    static int getHostileId(@NotNull String species)
    {
        String sql = String.format(
            "SELECT h.rowid " +
            "FROM %s h " +
            "WHERE lower(h.%s) = '%s'",
            HostileRepository.TABLE_NAME,
            COLUMN_SPECIES,
            species.toLowerCase()
        );

        Connection connection = null;
        Statement  statement  = null;
        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet.getInt(COLUMN_ID);
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


    /**
     * Get viewable hostile by species
     *
     * @param species Species of hostile to retrieve
     *
     * @return Hostile
     *
     * @throws HostileNotFoundException If viewable hostile not found
     */
    public static @NotNull Hostile getViewableHostile(@NotNull String species) throws HostileNotFoundException
    {
        String sql = String.format(
            "SELECT h.%s, h.*, l.* " +
            "FROM %s h " +
            "INNER JOIN %s l ON l.hostile_id = h.rowid " +
            "WHERE lower(h.%s) = '%s' and %s = 'true';",
            COLUMN_ID,
            TABLE_NAME,
            LootRepository.TABLE_NAME,
            COLUMN_SPECIES,
            species.toLowerCase(),
            COLUMN_IS_VIEWABLE
        );

        Connection             connection = null;
        Statement              statement  = null;
        HashMap<Integer, Loot> lootList   = new HashMap<>();

        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                String  species2      = resultSet.getString(COLUMN_SPECIES);
                int     dangerLevel   = resultSet.getInt(COLUMN_DANGER_LEVEL);
                int     hitpoints     = resultSet.getInt(COLUMN_HITPOINTS);
                int     attackDice    = resultSet.getInt(COLUMN_ATTACK);
                int     attackCount   = resultSet.getInt(COLUMN_ATTACK_COUNT);
                int     lootRollCount = resultSet.getInt(COLUMN_LOOT_ROLLS);
                boolean isViewable    = resultSet.getBoolean(COLUMN_IS_VIEWABLE);
                while (resultSet.next()) {
                    Loot loot = new Loot(
                        resultSet.getInt("dice_roll"),
                        resultSet.getString("item_name"),
                        resultSet.getInt("quantity")
                    );
                    lootList.put(loot.getDiceRoll(), loot);
                }
                return new Hostile(
                    species2,
                    dangerLevel,
                    hitpoints,
                    attackDice,
                    attackCount,
                    lootRollCount,
                    lootList,
                    isViewable
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

        throw HostileNotFoundException.createNotInDatabase(species);
    }

    /**
     * Create table if it does not exist
     */
    static void createTableIfNotExists()
    {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s", HostileRepository.TABLE_NAME) +
                     "(" +
                     String.format(" %s TEXT PRIMARY KEY  NOT NULL, ", COLUMN_SPECIES) +
                     String.format(" %s INT  DEFAULT 1    NOT NULL, ", COLUMN_DANGER_LEVEL) +
                     String.format(" %s INT               NOT NULL, ", COLUMN_HITPOINTS) +
                     String.format(" %s INT               NOT NULL, ", COLUMN_ATTACK) +
                     String.format(" %s INT  DEFAULT 1    NOT NULL, ", COLUMN_ATTACK_COUNT) +
                     String.format(" %s INT  DEFAULT 1    NOT NULL, ", COLUMN_LOOT_ROLLS) +
                     String.format(" %s BOOL DEFAULT TRUE NOT NULL  ", COLUMN_IS_VIEWABLE) +
                     ")";
        HostileRepository.executeUpdate(sql);
    }

    /**
     * Execute update
     *
     * @param sql Update to execute
     */
    private static void executeUpdate(@NotNull String sql)
    {
        Connection connection = null;
        Statement  statement  = null;
        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
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
