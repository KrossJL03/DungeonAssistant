package bot.Registry;

import bot.CommandFactoryInterface;
import bot.Encounter.Command.EncounterCommandFactory;
import bot.ProcessManager;
import bot.Registry.Record.Command.RecordCommandFactory;
import bot.Registry.Review.Command.ReviewCommandFactory;
import org.jetbrains.annotations.NotNull;

public class RegistryServiceProvider
{
    private ProcessManager processManager;
    private RegistryLogger logger;

    /**
     * RegistryServiceProvider constructor.
     *
     * @param processManager Process manager
     */
    public RegistryServiceProvider(@NotNull ProcessManager processManager)
    {
        this.logger = new RegistryLogger();
        this.processManager = processManager;
    }

    /**
     * Get record command factory
     *
     * @return CommandFactoryInterface
     */
    public @NotNull CommandFactoryInterface getRecordCommandFactory()
    {
        return new RecordCommandFactory(processManager, logger);
    }

    /**
     * Get review command factory
     *
     * @return CommandFactoryInterface
     */
    public @NotNull CommandFactoryInterface getReviewCommandFactory()
    {
        return new ReviewCommandFactory(processManager, logger);
    }
}
