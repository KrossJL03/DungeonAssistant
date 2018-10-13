import bot.*;

import bot.Encounter.EncounterContext;
import bot.Encounter.EncounterLogger;
import bot.Encounter.EncounterLoggerContext;
import bot.Encounter.EncounterManager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import java.io.FileInputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] arguments) throws Exception {
        EncounterContext       encounterContext       = new EncounterContext();
        EncounterLoggerContext encounterLoggerContext = new EncounterLoggerContext();

        EncounterLogger encounterLogger = new EncounterLogger(encounterLoggerContext);

        EncounterManager encountermanager = new EncounterManager(
            encounterContext,
            encounterLogger,
            encounterLoggerContext
        );

        CommandManager  commandManager  = new CommandManager(encountermanager);
        CommandListener commandListener = new CommandListener(commandManager);

        Main.populateTestData();

        Properties properties = new Properties();
        properties.load(new FileInputStream("config.properties"));
        JDA api = new JDABuilder(properties.getProperty("token")).build();
        api.addEventListener(commandListener);
    }

    private static void populateTestData() {
    }
}