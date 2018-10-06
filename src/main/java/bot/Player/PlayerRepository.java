package bot.Player;

import bot.Repository.RepositoryPaths;

import java.sql.*;
import java.util.ArrayList;

public class PlayerRepository {

    private static String TABLE_NAME = "player";

    public static void addPlayerIfNotExists(String userId, String name) {
        PlayerRepository.createTableIfNotExists();
        Player player = PlayerRepository.getPlayer(userId);
        if (player == null) {
            String sql = String.format(
                "INSERT INTO %s(user_id, name) VALUES('%s','%s')",
                PlayerRepository.TABLE_NAME,
                userId,
                name
            );
            PlayerRepository.executeUpdate(sql);
        }
    }

    public static Player getPlayer(String userId) {
        String sql = String.format(
            "SELECT * FROM %s WHERE user_id = '%s'",
            PlayerRepository.TABLE_NAME,
            userId
        );
        ArrayList<Player> players = PlayerRepository.executeQuery(sql);
        if (players.isEmpty()) {
            return null;
        } else if (players.size() > 1) {
            // todo throw multiple players exception
            return null;
        }
        return players.get(0);
    }

    public static void main(String[] args) {
        // todo remove after testing
//        PlayerRepository.createTableIfNotExists();
        PlayerRepository.addPlayerIfNotExists("281640303499149314", "JKSketchy");
//        Player player = PlayerRepository.getPlayer("281640303499149314");
    }

    private static void createTableIfNotExists() {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (", PlayerRepository.TABLE_NAME) +
                     " user_id TEXT PRIMARY KEY NOT NULL, " +
                     " name    TEXT             NOT NULL, " +
                     " cumulus INT              DEFAULT 0 " +
                     ")";
        PlayerRepository.executeUpdate(sql);
    }

    private static ArrayList<Player> executeQuery(String sql) {
        Connection        connection = null;
        Statement         statement  = null;
        ArrayList<Player> players    = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(RepositoryPaths.getDatabasePath("database"));
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    Player player = new Player(
                        resultSet.getRow(),
                        resultSet.getString("user_id"),
                        resultSet.getString("name"),
                        resultSet.getInt("cumulus")
                    );
                    players.add(player);
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
        return players;
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
}
