package bot.Registry.Review.Command;

import bot.Item.Consumable.ConsumableManager;
import bot.Registry.RegistryLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewItemsCommand extends ReviewCommand
{
    /**
     * ViewExplorersCommand constructor
     *
     * @param logger Logger
     */
    ViewItemsCommand(@NotNull RegistryLogger logger)
    {
        super(
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
