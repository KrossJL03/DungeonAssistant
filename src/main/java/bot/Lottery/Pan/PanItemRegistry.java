package bot.Lottery.Pan;

import bot.Registry.RegistryPaths;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

class PanItemRegistry
{
    private static String TABLE_NAME      = "panItem";
    private static String COLUMN_ID       = "rowId";
    private static String COLUMN_LINK     = "link";
    private static String COLUMN_NAME     = "name";
    private static String COLUMN_RARITY   = "rarity";
    private static String COLUMN_ROLL     = "roll";

    /**
     * Create table if it does not exist
     */
    static void createTableIfNotExists()
    {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s", TABLE_NAME) +
                     "(" +
                     String.format(" %s TEXT              NOT NULL, ", COLUMN_NAME) +
                     String.format(" %s TEXT              NOT NULL, ", COLUMN_RARITY) +
                     String.format(" %s INT               NOT NULL, ", COLUMN_ROLL) +
                     String.format(" %s TEXT DEFAULT NULL           ", COLUMN_LINK) +
                     ")";
        executeUpdate(sql);
    }

    /**
     * Get item by rarity and item roll
     *
     * @param rarity   Rarity
     * @param itemRoll Item roll
     *
     * @return Item
     *
     * @throws PanException If item not found
     */
    static @NotNull PanItem getItemByRarityAndRoll(@NotNull PanRarity rarity, int itemRoll) throws PanException
    {
        Connection connection = null;
        Statement  statement  = null;

        String sql = String.format(
            "SELECT %s,%s,%s,%s FROM %s WHERE lower(%s) = '%s' AND %s = %s;",
            COLUMN_NAME,
            COLUMN_RARITY,
            COLUMN_ROLL,
            COLUMN_LINK,
            TABLE_NAME,
            COLUMN_RARITY,
            rarity.getName().toLowerCase(),
            COLUMN_ROLL,
            itemRoll
        );
        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                return new PanItem(
                    resultSet.getString(COLUMN_NAME),
                    resultSet.getString(COLUMN_RARITY),
                    resultSet.getInt(COLUMN_ROLL),
                    resultSet.getString(COLUMN_LINK)
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

        throw PanException.createItemNotFound(rarity.getName(), itemRoll);
    }


    /**
     * Get item count by rarity
     *
     * @param rarity Rarity
     *
     * @return int
     */
    static int getItemCountByRarity(@NotNull PanRarity rarity)
    {
        Connection connection = null;
        Statement  statement  = null;
        int        result     = 0;

        String sql = String.format(
            "SELECT COUNT(*) FROM %s WHERE lower(%s) = '%s';",
            TABLE_NAME,
            COLUMN_RARITY,
            rarity.getName().toLowerCase()
        );

        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            result = resultSet != null ? resultSet.getInt("COUNT(*)") : 0;
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

        return result;
    }

    /**
     * Insert item
     *
     * @param item Item to insert
     */
    static void insertItem(@NotNull PanItem item)
    {
        String sql = String.format(
            "INSERT INTO %s(%s,%s,%s,%s) VALUES('%s','%s',%d,'%s')",
            TABLE_NAME,
            COLUMN_NAME,
            COLUMN_RARITY,
            COLUMN_ROLL,
            COLUMN_LINK,
            item.getName(),
            item.getRarityName(),
            item.getRoll(),
            item.getLink()
        );
        executeUpdate(sql);
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
