import bot.*;
import bot.Encounter.EncounterServiceProvider;
import bot.Registry.RegistryServiceProvider;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import java.util.ArrayList;

public class Main
{
    public static void main(String[] arguments) throws Exception
    {
        CommandProvider commandProvider = new CommandProvider();
        CommandListener commandListener = new CommandListener(
            commandProvider.getCommands(),
            commandProvider.getAdditionalCommands()
        );

        JDA api = new JDABuilder(MyProperties.token).build();
        api.addEventListener(commandListener);
    }
}