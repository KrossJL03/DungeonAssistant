import bot.*;
import bot.Encounter.Command.EncounterCommandFactory;
import bot.Registry.Record.Command.RecordCommandFactory;
import bot.Registry.Review.Command.ReviewCommandFactory;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import java.util.ArrayList;

public class Main
{
    public static void main(String[] arguments) throws Exception
    {
        ArrayList<CommandFactoryInterface> commandFactories   = new ArrayList<>();
        ArrayList<CommandInterface>        commands           = new ArrayList<>();
        ArrayList<CommandInterface>        additionalCommands = new ArrayList<>();

        commandFactories.add(new EncounterCommandFactory());
        commandFactories.add(new RecordCommandFactory());
        commandFactories.add(new ReviewCommandFactory());

        for (CommandFactoryInterface commandFactory : commandFactories) {
            commands.addAll(commandFactory.createCommands());
            additionalCommands.addAll(commandFactory.createAdditionalCommands());
        }

        CommandListener commandListener = new CommandListener(commands, additionalCommands);

        JDA api = new JDABuilder(MyProperties.token).build();
        api.addEventListener(commandListener);
    }
}