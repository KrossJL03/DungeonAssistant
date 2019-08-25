package bot.Lottery.Mofongo;

import bot.CommandFactoryInterface;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

public class MofongoServiceProvider
{
    private MofongoLogger  mofongoLogger;
    private ProcessManager processManager;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     */
    public MofongoServiceProvider(@NotNull ProcessManager processManager)
    {
        this.mofongoLogger = new MofongoLogger();
        this.processManager = processManager;
    }

    /**
     * Get command factory
     *
     * @return CommandFactoryInterface
     */
    public @NotNull CommandFactoryInterface getCommandFactory()
    {
        return new MofongoCommandFactory(processManager, mofongoLogger);
    }
}
