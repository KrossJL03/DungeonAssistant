import bot.*;
import bot.Repository.HostileRepository;
import bot.Repository.PCRepository;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import java.util.Properties;

public class Main {
    public static void main(String[] arguments) throws Exception {
        EncounterContext       encounterContext       = new EncounterContext();
        EncounterLoggerContext encounterLoggerContext = new EncounterLoggerContext();
        HostileRepository      hostileRepository      = new HostileRepository();
        PCRepository           pcRepository           = new PCRepository();

        EncounterLogger encounterLogger = new EncounterLogger(encounterLoggerContext);

        EncounterManager encountermanager = new EncounterManager(
            encounterContext,
            encounterLogger,
            encounterLoggerContext,
            hostileRepository
        );

        CommandManager commandManager = new CommandManager(
            new PrivateLogger(),
            encountermanager,
            pcRepository,
            hostileRepository
        );

        CommandListener commandListener = new CommandListener(commandManager);

        Properties properties = new Properties();
        properties.load(Main.class.getClassLoader().getResourceAsStream("config.properties"));
        JDA api = new JDABuilder(properties.getProperty("token")).build();
        api.addEventListener(commandListener);
    }
}