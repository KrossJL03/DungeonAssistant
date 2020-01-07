package bot.Lottery.Pan;

import bot.CommandFactoryInterface;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

public class PanServiceProvider
{
    private BasicRarityRoller   basicRarityRoller;
    private CommandFactory      commandFactory;
    private PanItemRoller       itemRoller;
    private PanLogger           panLogger;
    private ProcessManager      processManager;
    private SpecialRarityRoller specialRarityRoller;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     */
    public PanServiceProvider(@NotNull ProcessManager processManager)
    {
        this.basicRarityRoller = new BasicRarityRoller();
        this.itemRoller = new PanItemRoller();
        this.panLogger = new PanLogger();
        this.processManager = processManager;
        this.specialRarityRoller = new SpecialRarityRoller();

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
        this.commandFactory = new CommandFactory(
            processManager,
            panLogger,
            itemRoller,
            basicRarityRoller,
            specialRarityRoller
        );
    }
}
