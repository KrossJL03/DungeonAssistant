package bot.Item.Consumable;

import bot.Registry.RegistryPaths;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

class ConsumableRepository {

    private static String TABLE_NAME = "consumable";

    static void insertItem(
        String name,
        String image,
        String description,
        String shortDescription,
        int buyValue,
        int sellValue,
        int seasonalMonth,
        String usablePhase,
        int damage,
        int hitpointsHealed,
        int tempStatBoost,
        int uses,
        float percentHealed,
        boolean dodges,
        boolean healsUser,
        boolean pingDM,
        boolean protects,
        boolean recipientRequired,
        boolean revives
    ) {
        String sql = String.format(
            "INSERT INTO %s(" +
            " name, " +
            " image, " +
            " description, " +
            " shortDescription, " +
            " buyValue, " +
            " sellValue, " +
            " seasonalMonth, " +
            " usablePhase, " +
            " damage, " +
            " hitpointsHealed, " +
            " tempStatBoost, " +
            " uses, " +
            " percentHealed, " +
            " dodges, " +
            " healsUser, " +
            " pingDM, " +
            " protects, " +
            " recipientRequired, " +
            " revives" +
            ") " +
            "VALUES('%s','%s','%s','%s',%d,%d,%d,'%s',%d,%d,%d,%d,%f,%d,%d,%d,%d,%d,%d)",
            ConsumableRepository.TABLE_NAME,
            name,
            image,
            description,
            shortDescription,
            buyValue,
            sellValue,
            seasonalMonth,
            usablePhase,
            damage,
            hitpointsHealed,
            tempStatBoost,
            uses,
            percentHealed,
            dodges ? 1 : 0,
            healsUser ? 1 : 0,
            pingDM ? 1 : 0,
            protects ? 1 : 0,
            recipientRequired ? 1 : 0,
            revives ? 1 : 0
        );
        ConsumableRepository.executeUpdate(sql);
    }

    static ArrayList<ConsumableItem> getAllItems() {
        String sql = String.format("SELECT * FROM %s ORDER BY name", ConsumableRepository.TABLE_NAME);

        Connection                connection = null;
        Statement                 statement  = null;
        ArrayList<ConsumableItem> items      = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    items.add(
                        new ConsumableItem(
                            resultSet.getString("name"),
                            resultSet.getString("image"),
                            resultSet.getString("description"),
                            resultSet.getString("shortDescription"),
                            resultSet.getInt("buyValue"),
                            resultSet.getInt("sellValue"),
                            resultSet.getInt("seasonalMonth"),
                            resultSet.getString("usablePhase"),
                            resultSet.getInt("damage"),
                            resultSet.getInt("hitpointsHealed"),
                            resultSet.getInt("tempStatBoost"),
                            resultSet.getInt("uses"),
                            resultSet.getFloat("percentHealed"),
                            resultSet.getBoolean("dodges"),
                            resultSet.getBoolean("healsUser"),
                            resultSet.getBoolean("pingDM"),
                            resultSet.getBoolean("protects"),
                            resultSet.getBoolean("recipientRequired"),
                            resultSet.getBoolean("revives")
                        )
                    );
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
        return items;
    }

    static ConsumableItem getItem(String name) {
        String sql = String.format(
            "SELECT rowid, *" +
            "FROM %s " +
            "WHERE lower(name) = '%s';",
            ConsumableRepository.TABLE_NAME,
            name.toLowerCase()
        );

        Connection connection = null;
        Statement  statement  = null;

        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                return new ConsumableItem(
                    resultSet.getString("name"),
                    resultSet.getString("image"),
                    resultSet.getString("description"),
                    resultSet.getString("shortDescription"),
                    resultSet.getInt("buyValue"),
                    resultSet.getInt("sellValue"),
                    resultSet.getInt("seasonalMonth"),
                    resultSet.getString("usablePhase"),
                    resultSet.getInt("damage"),
                    resultSet.getInt("hitpointsHealed"),
                    resultSet.getInt("tempStatBoost"),
                    resultSet.getInt("uses"),
                    resultSet.getFloat("percentHealed"),
                    resultSet.getBoolean("dodges"),
                    resultSet.getBoolean("healsUser"),
                    resultSet.getBoolean("pingDM"),
                    resultSet.getBoolean("protects"),
                    resultSet.getBoolean("recipientRequired"),
                    resultSet.getBoolean("revives")
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
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s", ConsumableRepository.TABLE_NAME) +
                     "(" +
                     " name              TEXT PRIMARY KEY NOT NULL, " +
                     " image             TEXT             DEFAULT NULL, " +
                     " description       TEXT             DEFAULT NULL, " +
                     " shortDescription  TEXT             DEFAULT NULL, " +
                     " buyValue          INT              DEFAULT 0, " +
                     " sellValue         INT              DEFAULT 0, " +
                     " seasonalMonth     INT              DEFAULT 0, " +
                     " usablePhase       TEXT             DEFAULT NULL, " +
                     " damage            INT              DEFAULT 0, " +
                     " hitpointsHealed   INT              DEFAULT 0, " +
                     " tempStatBoost     INT              DEFAULT 0, " +
                     " uses              INT              DEFAULT 1, " +
                     " percentHealed     FLOAT            DEFAULT 0.0, " +
                     " dodges            INT              DEFAULT 0, " +
                     " healsUser         INT              DEFAULT 0, " +
                     " pingDM            INT              DEFAULT 0, " +
                     " protects          INT              DEFAULT 0, " +
                     " recipientRequired INT              DEFAULT 0, " +
                     " revives           INT              DEFAULT 0" +
                     ")";
        ConsumableRepository.executeUpdate(sql);
    }

    private static void executeUpdate(String sql) {
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
