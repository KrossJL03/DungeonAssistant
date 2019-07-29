package bot.Registry.Review.Command;

import bot.Item.Consumable.ConsumableManager;
import bot.ProcessManager;
import bot.Registry.RegistryLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewItemsCommand extends ReviewCommand
{
    /**
     * ViewItemsCommand constructor
     *
     * @param processManager Process manager
     * @param logger         Logger
     */
    ViewItemsCommand(@NotNull ProcessManager processManager, @NotNull RegistryLogger logger)
    {
        super(
            processManager,
            logger,
            "view items",
            new ArrayList<>(),
            "View a list of items."
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event)
    {
        getLogger().logItemList(event.getChannel(), ConsumableManager.getAllItems());
    }
}
