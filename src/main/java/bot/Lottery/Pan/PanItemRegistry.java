package bot.Lottery.Pan;

import bot.Item.Item;
import bot.Registry.RegistryPaths;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

class PanItemRegistry
{
    private static String TABLE_NAME = "item";

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
            Item.FIELD_PAN_RARITY,
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
     * Get item name by rarity and item roll
     *
     * @param rarity   Rarity
     * @param itemRoll Item roll
     *
     * @return String
     *
     * @throws PanException If item not found
     */
    static @NotNull String getItemNameByRarityAndRoll(@NotNull PanRarity rarity, int itemRoll) throws PanException
    {
        Connection        connection = null;
        Statement         statement  = null;
        ArrayList<String> itemNames  = new ArrayList<>();

        String sql = String.format(
            "SELECT %s FROM %s WHERE lower(%s) = '%s' ORDER BY %s;",
            Item.FIELD_NAME,
            TABLE_NAME,
            Item.FIELD_PAN_RARITY,
            rarity.getName().toLowerCase(),
            Item.FIELD_NAME
        );

        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    itemNames.add(resultSet.getString(Item.FIELD_NAME));
                }
                return itemNames.get(itemRoll);
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
}
