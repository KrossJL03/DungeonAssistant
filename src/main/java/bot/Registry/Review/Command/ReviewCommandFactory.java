package bot.Registry.Review.Command;

import bot.CommandFactoryInterface;
import bot.CommandInterface;
import bot.PrivateLogger;
import bot.ProcessManager;
import bot.Registry.RegistryLogger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReviewCommandFactory implements CommandFactoryInterface
{
    private PrivateLogger  privateLogger;
    private ProcessManager processManager;
    private RegistryLogger registryLogger;

    /**
     * ReviewCommandFactory constructor.
     *
     * @param processManager Process manager
     * @param registryLogger Registry logger
     * @param privateLogger  Private logger
     */
    public ReviewCommandFactory(
        @NotNull ProcessManager processManager,
        @NotNull RegistryLogger registryLogger,
        @NotNull PrivateLogger privateLogger
    )
    {
        this.privateLogger = privateLogger;
        this.processManager = processManager;
        this.registryLogger = registryLogger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createAdditionalCommands()
    {
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createCommands()
    {
        ArrayList<CommandInterface> commands = new ArrayList<>();

        commands.add(new ViewExplorersCommand(processManager, registryLogger));
        commands.add(new ViewHostileLootCommand(processManager, registryLogger));
        commands.add(new ViewHostilesCommand(processManager, registryLogger));
        commands.add(new RollLootCommand(processManager));

        commands.add(new HelpReviewCommand(processManager, privateLogger, new ArrayList<>(commands)));

        return commands;
    }
}
