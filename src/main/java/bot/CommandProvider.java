package bot;

import bot.Encounter.EncounterServiceProvider;
import bot.Item.ItemServiceProvider;
import bot.Lottery.LotteryServiceProvider;
import bot.Registry.RegistryServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommandProvider
{
    private ArrayList<CommandInterface> additionalCommands;
    private ArrayList<CommandInterface> commands;

    /**
     * Constructor.
     */
    public CommandProvider()
    {
        this.commands = new ArrayList<>();
        this.additionalCommands = new ArrayList<>();
        init();
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
     * Create instances for commands
     *
     * @return ArrayList
     */
    public @NotNull ArrayList<CommandInterface> getCommands()
    {
        return new ArrayList<>(commands);
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
        ItemServiceProvider      itemServiceProvider        = new ItemServiceProvider(processManager);

        ArrayList<CommandFactoryInterface> commandFactories = new ArrayList<>();

        commandFactories.add(encounterServiceProvider.getCommandFactory());
        commandFactories.add(registryServiceProvider.getRecordCommandFactory());
        commandFactories.add(registryServiceProvider.getReviewCommandFactory());
        commandFactories.add(itemLotteryServiceProvider.getCommandFactory());
        commandFactories.add(itemServiceProvider.getCommandFactory());

        for (CommandFactoryInterface commandFactory : commandFactories) {
            commands.addAll(commandFactory.createCommands());
            additionalCommands.addAll(commandFactory.createAdditionalCommands());
        }

        commands.add(new HelpCommand(processManager, getHelpCommands()));
    }
}
