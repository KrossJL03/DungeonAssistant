package bot.Lottery.Pan;

import bot.CommandFactoryInterface;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

public class PanServiceProvider
{
    private PanCommandFactory commandFactory;
    private PanItemRoller     itemRoller;
    private PanLogger         panLogger;
    private ProcessManager    processManager;
    private PanRarityRoller   rarityRoller;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     */
    public PanServiceProvider(@NotNull ProcessManager processManager)
    {
        this.itemRoller = new PanItemRoller();
        this.panLogger = new PanLogger();
        this.processManager = processManager;
        this.rarityRoller = new PanRarityRoller();

        init();
    }

    /**
     * Get command factory.
     *
     * @return CommandFactoryInterface
     */
    public @NotNull CommandFactoryInterface getCommandFactory()
    {
        return commandFactory;
    }

    /**
     * Initialize services.
     */
    private void init()
    {
        this.commandFactory = new PanCommandFactory(processManager, panLogger, itemRoller, rarityRoller);
    }
}
