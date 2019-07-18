import bot.CommandListener;
import bot.CommandManager;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.Encounter.EncounterManager;
import bot.Encounter.Logger.Message.Action.ActionMessageBuilder;
import bot.Encounter.Logger.Message.PhaseChange.PhaseChangeMessageBuilder;
import bot.Encounter.Logger.Message.Summary.SummaryMessageBuilder;
import bot.MyProperties;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Main
{
    public static void main(String[] arguments) throws Exception
    {
        EncounterHolder encounterHolder = new EncounterHolder();
        EncounterLogger encounterLogger = new EncounterLogger(
            new ActionMessageBuilder(),
            new PhaseChangeMessageBuilder(),
            new SummaryMessageBuilder()
        );

        EncounterManager encountermanager = new EncounterManager(
            encounterHolder,
            encounterLogger
        );

        CommandManager  commandManager  = new CommandManager(encountermanager, encounterHolder);
        CommandListener commandListener = new CommandListener(commandManager);

        JDA api = new JDABuilder(MyProperties.token).build();
        api.addEventListener(commandListener);
    }
}