import bot.*;

import bot.Encounter.Encounter;
import bot.Encounter.Logger.EncounterLogger;
import bot.Encounter.EncounterManager;
import bot.Encounter.Logger.MessageBuilder.ActionMessageBuilder;
import bot.Encounter.Logger.MessageBuilder.SummaryMessageBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Main
{
    public static void main(String[] arguments) throws Exception
    {

        Encounter encounter = new Encounter();
        EncounterLogger encounterLogger = new EncounterLogger(new ActionMessageBuilder(), new SummaryMessageBuilder());

        EncounterManager encountermanager = new EncounterManager(
            encounter,
            encounterLogger
        );

        CommandManager  commandManager  = new CommandManager(encountermanager);
        CommandListener commandListener = new CommandListener(commandManager);

        JDA api = new JDABuilder(MyProperties.token).build();
        api.addEventListener(commandListener);
    }
}