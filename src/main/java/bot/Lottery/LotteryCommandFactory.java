package bot.Lottery;

import bot.CommandFactoryInterface;
import bot.CommandInterface;
import bot.PrivateLogger;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LotteryCommandFactory implements CommandFactoryInterface
{
    private PrivateLogger                      privateLogger;
    private ProcessManager                     processManager;
    private ArrayList<CommandFactoryInterface> commandFactories;

    /**
     * Constructor.
     *
     * @param processManager   Process manager
     * @param privateLogger    Private logger
     * @param commandFactories Command Factories
     */
    LotteryCommandFactory(
        @NotNull ProcessManager processManager,
        @NotNull PrivateLogger privateLogger,
        @NotNull ArrayList<CommandFactoryInterface> commandFactories
    )
    {
        this.commandFactories = commandFactories;
        this.privateLogger = privateLogger;
        this.processManager = processManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createCommands()
    {
        ArrayList<CommandInterface> result = new ArrayList<>();
        for (CommandFactoryInterface commandFactory : commandFactories) {
            result.addAll(commandFactory.createCommands());
        }

        ArrayList<CommandInterface> commands = createAdditionalCommands();
        commands.addAll(result);
        CommandInterface helpCommand = new LotteryHelpCommand(
            processManager,
            privateLogger,
            commands
        );
        result.add(helpCommand);

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createAdditionalCommands()
    {
        ArrayList<CommandInterface> result = new ArrayList<>();
        for (CommandFactoryInterface commandFactory : commandFactories) {
            result.addAll(commandFactory.createAdditionalCommands());
        }

        return result;
    }
}
