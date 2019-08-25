package bot;

import bot.Encounter.EncounterServiceProvider;
import bot.Lottery.LotteryServiceProvider;
import bot.Registry.RegistryServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommandProvider
{
    private ArrayList<CommandInterface> commands;
    private ArrayList<CommandInterface> additionalCommands;

    /**
     * CommandProvider constructor.
     */
    public CommandProvider()
    {
        this.commands = new ArrayList<>();
        this.additionalCommands = new ArrayList<>();
        init();
    }

    /**
     * Create instances for commands
     *
     * @return ArrayList
     */
    public @NotNull ArrayList<CommandInterface> getCommands()
    {
        return new ArrayList<>(commands);
    }

    /**
     * Get commands for interfacing with other bots
     *
     * @return ArrayList
     */
    public @NotNull ArrayList<CommandInterface> getAdditionalCommands()
    {
        return new ArrayList<>(additionalCommands);
    }

    /**
     * Get help commands
     *
     * @return ArrayList
     */
    private @NotNull ArrayList<CommandInterface> getHelpCommands()
    {
        ArrayList<CommandInterface> helpCommands = new ArrayList<>();
        for (CommandInterface command : commands) {
            if (command instanceof HelpCommandInterface) {
                helpCommands.add(command);
            }
        }

        return helpCommands;
    }

    /**
     * Initialize commands
     */
    private void init()
    {
        ProcessManager           processManager             = new ProcessManager();
        EncounterServiceProvider encounterServiceProvider   = new EncounterServiceProvider(processManager);
        RegistryServiceProvider  registryServiceProvider    = new RegistryServiceProvider(processManager);
        LotteryServiceProvider   itemLotteryServiceProvider = new LotteryServiceProvider(processManager);

        ArrayList<CommandFactoryInterface> commandFactories = new ArrayList<>();

        commandFactories.add(encounterServiceProvider.getCommandFactory());
        commandFactories.add(registryServiceProvider.getRecordCommandFactory());
        commandFactories.add(registryServiceProvider.getReviewCommandFactory());
        commandFactories.add(itemLotteryServiceProvider.getCommandFactory());

        for (CommandFactoryInterface commandFactory : commandFactories) {
            commands.addAll(commandFactory.createCommands());
            additionalCommands.addAll(commandFactory.createAdditionalCommands());
        }

        commands.add(new HelpCommand(processManager, getHelpCommands()));
    }
}
