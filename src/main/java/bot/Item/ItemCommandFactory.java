package bot.Item;

import bot.CommandFactoryInterface;
import bot.CommandInterface;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ItemCommandFactory implements CommandFactoryInterface
{
    private ItemImporter   itemImporter;
    private ProcessManager processManager;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param itemImporter   Item importer
     */
    ItemCommandFactory(
        @NotNull ProcessManager processManager,
        @NotNull ItemImporter itemImporter
    )
    {
        this.itemImporter = itemImporter;
        this.processManager = processManager;
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

        commands.add(new ImportItemsCommand(processManager, itemImporter));

        return commands;
    }
}
