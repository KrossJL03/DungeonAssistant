package bot.Repository;

public class RepositoryPaths {
    private static String DATABASE_CONNECTION = "jdbc:sqlite:src/main/java/bot/Registry/Database/";

    public static String getDatabasePath(String dbName) {
        return String.format("%s/%s.db", RepositoryPaths.DATABASE_CONNECTION, dbName);
    }
}
