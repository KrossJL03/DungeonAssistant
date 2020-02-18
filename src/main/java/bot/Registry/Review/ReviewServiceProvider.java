package bot.Registry.Review;

import bot.Command;
import bot.CommandProviderInterface;
import bot.PrivateLogger;
import bot.ProcessManager;
import bot.Registry.RegistryLogger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReviewServiceProvider implements CommandProviderInterface
{
    private ArrayList<Command> commands;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     */
    public ReviewServiceProvider(@NotNull ProcessManager processManager)
    {
        RegistryLogger registryLogger = new RegistryLogger();
        PrivateLogger  privateLogger  = new PrivateLogger(new HelpMessageBuilder());

        commands = new ArrayList<>();

        commands.add(new ViewExplorersCommand(processManager, registryLogger));
        commands.add(new ViewHostileLootCommand(processManager, registryLogger));
        commands.add(new ViewHostilesCommand(processManager, registryLogger));
        commands.add(new RollLootCommand(processManager));

        commands.add(new HelpReviewCommand(processManager, privateLogger, new ArrayList<>(commands)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<Command> getCommands()
    {
        return new ArrayList<>(commands);
    }
}
