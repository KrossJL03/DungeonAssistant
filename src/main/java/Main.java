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
        ProcessManager           processManager           = new ProcessManager();
        EncounterServiceProvider encounterServiceProvider = new EncounterServiceProvider(processManager);
        RegistryServiceProvider  registryServiceProvider  = new RegistryServiceProvider(processManager);

        ArrayList<CommandFactoryInterface> commandFactories   = new ArrayList<>();
        ArrayList<CommandInterface>        commands           = new ArrayList<>();
        ArrayList<CommandInterface>        additionalCommands = new ArrayList<>();

        commandFactories.add(encounterServiceProvider.getCommandFactory());
        commandFactories.add(registryServiceProvider.getRecordCommandFactory());
        commandFactories.add(registryServiceProvider.getReviewCommandFactory());

        for (CommandFactoryInterface commandFactory : commandFactories) {
            commands.addAll(commandFactory.createCommands());
            additionalCommands.addAll(commandFactory.createAdditionalCommands());
        }

        CommandListener commandListener = new CommandListener(commands, additionalCommands);

        JDA api = new JDABuilder(MyProperties.token).build();
        api.addEventListener(commandListener);
    }
}