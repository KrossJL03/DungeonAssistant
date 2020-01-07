package bot.Item;

import bot.Registry.RegistryPaths;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

class ItemRegistry
{
    private static String TABLE_NAME = "item";

    /**
     * Insert item
     *
     * @param item Item to insert
     */
    void insertItem(@NotNull Item item)
    {
        String sql = String.format(
            "INSERT OR REPLACE INTO %s(%s,%s) " +
            "VALUES(\"%s\",\"%s\")",
            TABLE_NAME,
            Item.FIELD_NAME,
            Item.FIELD_PAN_RARITY,
            item.getName(),
            item.getPanRarity()
        );

        executeUpdate(sql);
    }

    /**
     * Reset item table
     */
    void reset()
    {
        dropTable();
        createTableIfNotExists();
    }

    /**
     * Create table if it does not exist
     */
    private static void createTableIfNotExists()
    {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s", TABLE_NAME) +
                     "(" +
                     String.format(" %s TEXT PRIMARY KEY  NOT NULL, ", Item.FIELD_NAME) +
                     String.format(" %s TEXT DEFAULT NULL           ", Item.FIELD_PAN_RARITY) +
                     ")";

        executeUpdate(sql);
    }

    /**
     * Drop table
     */
    private static void dropTable()
    {
        String sql = String.format("DROP TABLE %s", TABLE_NAME);

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