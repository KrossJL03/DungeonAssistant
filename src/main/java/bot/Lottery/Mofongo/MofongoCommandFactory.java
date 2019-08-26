package bot.Lottery.Mofongo;

import bot.CommandFactoryInterface;
import bot.CommandInterface;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class MofongoCommandFactory implements CommandFactoryInterface
{
    private MofongoLogger  mofongoLogger;
    private ProcessManager processManager;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param mofongoLogger  Logger
     */
    MofongoCommandFactory(
        @NotNull ProcessManager processManager,
        MofongoLogger mofongoLogger
    )
    {
        this.mofongoLogger = mofongoLogger;
        this.processManager = processManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createCommands()
    {
        return new ArrayList<CommandInterface>()
        {
            {
                add(new MofongoRollCommand(processManager, mofongoLogger));
                add(new MofongoViewPetsCommand(processManager, mofongoLogger));
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createAdditionalCommands()
    {
        return new ArrayList<>();
    }
}
