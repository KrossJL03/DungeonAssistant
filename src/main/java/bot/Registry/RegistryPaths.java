package bot.Registry;

public class RegistryPaths
{
    private static String DATABASE_CONNECTION = "jdbc:sqlite:src/main/java/bot/Registry/Database/database.db";

    public static String getDatabasePath() {
        return DATABASE_CONNECTION;
    }
}
