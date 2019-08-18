package bot.Encounter.Tier;

import bot.Encounter.TierInterface;
import bot.Registry.RegistryPaths;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TierRegistry
{
    private static String TABLE_NAME = "tier";

    /**
     * Get tier by name
     *
     * @param name Tier name
     *
     * @return Tier
     *
     * @throws TierException If tier not found
     */
    public static @NotNull Tier getTier(@NotNull String name) throws TierException
    {
        String sql = String.format(
            "SELECT t.*" +
            "FROM %s t " +
            "WHERE lower(t.name) = '%s';",
            TABLE_NAME,
            name.toLowerCase()
        );

        Connection connection = null;
        Statement  statement  = null;

        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                String resultName        = resultSet.getString("name");
                int    minStatPointTotal = resultSet.getInt("min_stat_point_total");
                int    maxStatPointTotal = resultSet.getInt("max_stat_point_total");
                return new Tier(resultName, minStatPointTotal, maxStatPointTotal);
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
        throw TierException.createNotFound(name);
    }

    /**
     * Add new tier record
     *
     * @param tier Tier
     */
    static void addTier(@NotNull TierInterface tier)
    {
        String sql = String.format(
            "INSERT INTO %s(name, min_stat_point_total, max_stat_point_total) VALUES('%s',%d,%d)",
            TABLE_NAME,
            tier.getName(),
            tier.getMinStatPointTotal(),
            tier.getMaxStatPointTotal()
        );
        executeUpdate(sql);
    }

    /**
     * Create tier table if not exists
     */
    static void createTableIfNotExists()
    {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s", TABLE_NAME) +
                     "(" +
                     " name                 TEXT PRIMARY KEY NOT NULL, " +
                     " min_stat_point_total INT              NOT NULL, " +
                     " max_stat_point_total INT              NOT NULL  " +
                     ")";
        executeUpdate(sql);
    }

    /**
     * Execute update
     *
     * @param sql Sql
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