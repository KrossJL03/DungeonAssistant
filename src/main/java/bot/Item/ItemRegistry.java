package bot.Item;

import bot.Registry.RegistryPaths;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ItemRegistry
{
    private static String TABLE_NAME = "item";

    /**
     * Helper method to create table
     *
     * @param args Default
     */
    public static void main(String[] args)
    {
        String sql = String.format("DROP TABLE %s", TABLE_NAME);
        executeUpdate(sql);
        createTableIfNotExists();
    }

    /**
     * Insert item
     *
     * @param item Item to insert
     */
    void insertItem(@NotNull ItemAbstract item)
    {
        String sql = String.format(
            "INSERT OR REPLACE INTO %s(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) " +
            "VALUES(\"%s\",\"%s\",\"%s\",\"%s\",%d,%d,%s,%s,%s,%s,%d,%d,%d,%d,%d,%s)",
            TABLE_NAME,
            ItemAbstract.FIELD_NAME,
            ItemAbstract.FIELD_ITEM_TYPE,
            ItemAbstract.FIELD_ITEM_SUBTYPE,
            ItemAbstract.FIELD_IMAGE_URL,
            ItemAbstract.FIELD_BUY_VALUE,
            ItemAbstract.FIELD_SELL_VALUE,
            ItemAbstract.FIELD_IS_BUYABLE,
            ItemAbstract.FIELD_IS_CRAFTABLE,
            ItemAbstract.FIELD_IS_EVENT_EXCLUSIVE,
            ItemAbstract.FIELD_IS_SEASONAL,
            Equipment.FIELD_STAT_VALUE_HP,
            Equipment.FIELD_STAT_VALUE_STR,
            Equipment.FIELD_STAT_VALUE_WIS,
            Equipment.FIELD_STAT_VALUE_AGI,
            Equipment.FIELD_STAT_VALUE_DEF,
            Equipment.FIELD_ARMOR_SET_NAME,
            item.getName(),
            item.getType(),
            item.getSubtype(),
            item.getImageUrl(),
            item.getBuyValue(),
            item.getSellValue(),
            item.isBuyable(),
            item.isCraftable(),
            item.isEventExclusive(),
            item.isSeasonal(),
            (item instanceof Equipment) ? ((Equipment) item).getHpStatValue() : null,
            (item instanceof Equipment) ? ((Equipment) item).getStrStatValue() : null,
            (item instanceof Equipment) ? ((Equipment) item).getWisStatValue() : null,
            (item instanceof Equipment) ? ((Equipment) item).getAgiStatValue() : null,
            (item instanceof Equipment) ? ((Equipment) item).getDefStatValue() : null,
            (item instanceof Equipment) ? "'" + ((Equipment) item).getArmorSetName() + "'" : null
        );

        executeUpdate(sql);
    }

    /**
     * Create table if it does not exist
     */
    private static void createTableIfNotExists()
    {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s", TABLE_NAME) +
                     "(" +
                     String.format(" %s TEXT PRIMARY KEY  NOT NULL, ", ItemAbstract.FIELD_NAME) +
                     String.format(" %s TEXT              NOT NULL, ", ItemAbstract.FIELD_ITEM_TYPE) +
                     String.format(" %s TEXT DEFAULT NULL         , ", ItemAbstract.FIELD_ITEM_SUBTYPE) +
                     String.format(" %s TEXT              NOT NULL, ", ItemAbstract.FIELD_IMAGE_URL) +
                     String.format(" %s INT  DEFAULT 0    NOT NULL, ", ItemAbstract.FIELD_BUY_VALUE) +
                     String.format(" %s INT  DEFAULT 0    NOT NULL, ", ItemAbstract.FIELD_SELL_VALUE) +
                     String.format(" %s INT  DEFAULT 0    NOT NULL, ", ItemAbstract.FIELD_IS_BUYABLE) +
                     String.format(" %s INT  DEFAULT 0    NOT NULL, ", ItemAbstract.FIELD_IS_CRAFTABLE) +
                     String.format(" %s INT  DEFAULT 0    NOT NULL, ", ItemAbstract.FIELD_IS_EVENT_EXCLUSIVE) +
                     String.format(" %s INT  DEFAULT 0    NOT NULL, ", ItemAbstract.FIELD_IS_SEASONAL) +
                     String.format(" %s INT  DEFAULT 0    NOT NULL, ", Equipment.FIELD_STAT_VALUE_HP) +
                     String.format(" %s INT  DEFAULT 0    NOT NULL, ", Equipment.FIELD_STAT_VALUE_STR) +
                     String.format(" %s INT  DEFAULT 0    NOT NULL, ", Equipment.FIELD_STAT_VALUE_WIS) +
                     String.format(" %s INT  DEFAULT 0    NOT NULL, ", Equipment.FIELD_STAT_VALUE_AGI) +
                     String.format(" %s INT  DEFAULT 0    NOT NULL, ", Equipment.FIELD_STAT_VALUE_DEF) +
                     String.format(" %s TEXT DEFAULT NULL           ", Equipment.FIELD_ARMOR_SET_NAME) +
                     ")";

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