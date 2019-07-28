package bot.Registry.Review.Command;

import bot.CommandParameter;
import bot.Item.Consumable.ConsumableManager;
import bot.Registry.RegistryLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewItemCommand extends ReviewCommand
{
    /**
     * ViewExplorersCommand constructor
     *
     * @param logger Logger
     */
    ViewItemCommand(@NotNull RegistryLogger logger)
    {
        super(
            logger,
            "view item",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("ItemName", true));
                }
            },
            "View a single item in detail by name."
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event)
    {
        String[] parameters = getParametersFromEvent(event);
        String   itemName   = parameters[0];

        getLogger().logItemDetails(event.getChannel(), ConsumableManager.getItem(itemName));
    }
}
