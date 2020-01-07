package bot.Item;

import bot.CommandFactoryInterface;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

public class ItemServiceProvider
{
    private ItemFactory        itemFactory;
    private ItemImporter       itemImporter;
    private ItemRegistry       itemRegistry;
    private ProcessManager     processManager;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     */
    public ItemServiceProvider(@NotNull ProcessManager processManager)
    {
        this.processManager = processManager;
        init();
    }

    /**
     * Get record command factory
     *
     * @return CommandFactoryInterface
     */
    public @NotNull CommandFactoryInterface getCommandFactory()
    {
        return new ItemCommandFactory(processManager, itemImporter);
    }

    /**
     * Initialize.
     */
    private void init()
    {
        this.itemFactory = new ItemFactory();
        this.itemRegistry = new ItemRegistry();

        this.itemImporter = new ItemImporter(itemFactory, itemRegistry);
    }
}
