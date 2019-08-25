package bot.Lottery;

import bot.CommandFactoryInterface;
import bot.Lottery.Mofongo.MofongoServiceProvider;
import bot.PrivateLogger;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LotteryServiceProvider
{
    private LotteryCommandFactory  commandFactory;
    private MofongoServiceProvider mofongoServiceProvider;
    private PrivateLogger          privateLogger;
    private ProcessManager         processManager;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     */
    public LotteryServiceProvider(@NotNull ProcessManager processManager)
    {
        this.mofongoServiceProvider = new MofongoServiceProvider(processManager);
        this.privateLogger = new PrivateLogger(new LotteryHelpMessageBuilder());
        this.processManager = processManager;

        this.commandFactory = new LotteryCommandFactory(
            this.processManager,
            privateLogger,
            new ArrayList<CommandFactoryInterface>()
            {
                {
                    add(mofongoServiceProvider.getCommandFactory());
                }
            }
        );
    }

    /**
     * Get command factory
     *
     * @return CommandFactoryInterface
     */
    public @NotNull CommandFactoryInterface getCommandFactory()
    {
        return commandFactory;
    }
}
