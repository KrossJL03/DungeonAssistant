import bot.*;

import bot.Encounter.Encounter;
import bot.Encounter.Logger.EncounterLogger;
import bot.Encounter.Logger.EncounterLoggerContext;
import bot.Encounter.EncounterManager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Main {
    public static void main(String[] arguments) throws Exception {

        Encounter              encounterContext       = new Encounter();
        EncounterLoggerContext encounterLoggerContext = new EncounterLoggerContext();

        EncounterLogger encounterLogger = new EncounterLogger(encounterLoggerContext);

        EncounterManager encountermanager = new EncounterManager(
            encounterContext,
            encounterLogger,
            encounterLoggerContext
        );

        CommandManager  commandManager  = new CommandManager(encountermanager);
        CommandListener commandListener = new CommandListener(commandManager);

        JDA api = new JDABuilder(MyProperties.token).build();
        api.addEventListener(commandListener);
    }
}