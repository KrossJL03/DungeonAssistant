package bot.Registry;

public class RegistryPaths
{
    private static String DATABASE_CONNECTION = "jdbc:sqlite:src/main/java/bot/Registry/Database/";

    public static String getDatabasePath(String dbName) {
        return String.format("%s/%s.db", RegistryPaths.DATABASE_CONNECTION, dbName);
    }
}
