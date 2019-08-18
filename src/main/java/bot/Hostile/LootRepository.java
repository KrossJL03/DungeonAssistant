package bot.Hostile;

import bot.Registry.RegistryPaths;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

class LootRepository {

    static String TABLE_NAME = "loot";

    static void initializeLoot(int hostileId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
            String.format(
                "INSERT INTO %s(hostile_id, dice_roll, item_name, quantity) VALUES",
                LootRepository.TABLE_NAME
            )
        );
        for (int i = 1; i < 11; i++) {
            sql.append(String.format("(%d,%d,'%s',%d),", hostileId, i, null, 0));
        }
        sql.deleteCharAt(sql.length() - 1);
        LootRepository.executeUpdate(sql.toString());
    }

    static void insertLoot(int hostileId, int diceRoll, String item, int quantity) {
        String sql = String.format(
            "INSERT OR REPLACE INTO %s(hostile_id, dice_roll, item_name, quantity) " +
            "VALUES(%d,%d,'%s',%d)",
            LootRepository.TABLE_NAME,
            hostileId,
            diceRoll,
            item,
            quantity
        );
        LootRepository.executeUpdate(sql);
    }

    static ArrayList<Loot> getLootList(String hostileSpecies) {
        String sql = String.format(
            "SELECT * " +
            "FROM %s l " +
            "INNER JOIN %s h ON h.id = l.hostile_id " +
            "WHERE h.species = %s;",
            LootRepository.TABLE_NAME,
            HostileRepository.TABLE_NAME,
            hostileSpecies
        );
        return LootRepository.executeQuery(sql);
    }

    static void createTableIfNotExists() {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s", LootRepository.TABLE_NAME) +
                     "(" +
                     " hostile_id INT            NOT NULL, " +
                     " dice_roll  INT            NOT NULL, " +
                     " item_name  TEXT           DEFAULT NULL, " +
                     " quantity   INT  DEFAULT 1 NOT NULL, " +
                     "PRIMARY KEY (hostile_id, dice_roll)" +
                     ")";
//        todo implement proper table
//        String sql = String.format("CREATE TABLE IF NOT EXISTS %s", LootRepository.TABLE_NAME) +
//                     "(" +
//                     " hostile_id INT            NOT NULL, " +
//                     " dice_roll  INT            NOT NULL, " +
//                     " item_id    INT            NOT NULL, " +
//                     " item_type  TEXT           NOT NULL, " +
//                     " item_name  TEXT           NOT NULL, " +
//                     " quantity   INT  DEFAULT 1 NOT NULL, " +
//                     "PRIMARY KEY (hostile_id, dice_roll)" +
//                     ")";
        LootRepository.executeUpdate(sql);
    }

    private static ArrayList<Loot> executeQuery(String sql) {
        Connection connection    = null;
        Statement statement      = null;
        ArrayList<Loot> lootList = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(RegistryPaths.getDatabasePath());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    Loot loot = new Loot(
                        resultSet.getInt("dice_roll"),
                        resultSet.getString("item"),
                        resultSet.getInt("quantity")
                    );
                    lootList.add(loot);
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
        return lootList;
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
