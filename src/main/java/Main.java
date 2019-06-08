import bot.*;

import bot.Encounter.*;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Main
{
    public static void main(String[] arguments) throws Exception
    {
        EncounterHolder        encounterHolder        = new EncounterHolder();
        EncounterLoggerContext encounterLoggerContext = new EncounterLoggerContext();
        EncounterLogger        encounterLogger        = new EncounterLogger(encounterLoggerContext);

        EncounterManager encountermanager = new EncounterManager(
            encounterHolder,
            encounterLogger,
            encounterLoggerContext
        );

        CommandManager  commandManager  = new CommandManager(encountermanager, encounterHolder);
        CommandListener commandListener = new CommandListener(commandManager);

        Main.populateTestData();

        JDA api = new JDABuilder(MyProperties.token).build();
        api.addEventListener(commandListener);
    }

    private static void populateTestData()
    {
    }
}