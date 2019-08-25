package bot.Lottery.Mofongo;

import bot.Registry.RegistryPaths;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

class MofongoPetStatRegistry
{
    private static String TABLE_NAME          = "mofongoPetStat";
    private static String COLUMN_ID           = "rowId";
    private static String COLUMN_RARITY       = "rarity";
    private static String COLUMN_ROLL_COUNT   = "rollCount";
    private static String COLUMN_SPECIES_NAME = "speciesName";

    /**
     * Create table if it does not exist
     */
    static void createTableIfNotExists()
    {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s", TABLE_NAME) +
                     "(" +
                     String.format(" %s TEXT           NOT NULL, ", COLUMN_SPECIES_NAME) +
                     String.format(" %s TEXT           NOT NULL, ", COLUMN_RARITY) +
                     String.format(" %s TEXT DEFAULT 1 NOT NULL  ", COLUMN_ROLL_COUNT) +
                     ")";
        executeUpdate(sql);
    }

    /**
     * Get item by rarity and item roll
     *
     * @param speciesName Pet species name
     *
     * @return int
     *
     * @throws MofongoException If roll count not found
     */
    static int getRollCountBySpeciesName(@NotNull String speciesName) throws MofongoException
    {
        Connection connection = null;
        Statement  statement  = null;

        String sql = String.format(
            "SELECT %s FROM %s WHERE lower(%s) = '%s';",
            COLUMN_ROLL_COUNT,
            TABLE_NAME,
            COLUMN_SPECIES_NAME,
            speciesName.toLowerCase()
        );
        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet.getInt(COLUMN_ROLL_COUNT);
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

        throw MofongoException.createPetRollCountNotFound(speciesName);
    }

    /**
     * Insert item
     *
     * @param speciesName Pet species name
     * @param rarity      Pet rarity
     * @param rollCount   Number of rolls for this species
     */
    static void insertPetStat(@NotNull String speciesName, @NotNull String rarity, int rollCount)
    {
        String sql = String.format(
            "INSERT INTO %s(%s,%s,%s) VALUES('%s','%s',%d)",
            TABLE_NAME,
            COLUMN_SPECIES_NAME,
            COLUMN_RARITY,
            COLUMN_ROLL_COUNT,
            speciesName,
            rarity,
            rollCount
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
