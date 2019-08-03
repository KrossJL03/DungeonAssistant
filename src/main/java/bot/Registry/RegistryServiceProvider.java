package bot.Registry;

import bot.CommandFactoryInterface;
import bot.PrivateLogger;
import bot.ProcessManager;
import bot.Registry.Record.Command.RecordCommandFactory;
import bot.Registry.Record.Logger.Message.HelpRecordMessageBuilder;
import bot.Registry.Review.Command.ReviewCommandFactory;
import bot.Registry.Review.Logger.Message.HelpReviewMessageBuilder;
import org.jetbrains.annotations.NotNull;

public class RegistryServiceProvider
{
    private PrivateLogger  recordPrivateLogger;
    private PrivateLogger  reviewPrivateLogger;
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
        this.recordPrivateLogger = new PrivateLogger(new HelpRecordMessageBuilder());
        this.reviewPrivateLogger = new PrivateLogger(new HelpReviewMessageBuilder());
    }

    /**
     * Get record command factory
     *
     * @return CommandFactoryInterface
     */
    public @NotNull CommandFactoryInterface getRecordCommandFactory()
    {
        return new RecordCommandFactory(processManager, logger, recordPrivateLogger);
    }

    /**
     * Get review command factory
     *
     * @return CommandFactoryInterface
     */
    public @NotNull CommandFactoryInterface getReviewCommandFactory()
    {
        return new ReviewCommandFactory(processManager, logger, reviewPrivateLogger);
    }
}
