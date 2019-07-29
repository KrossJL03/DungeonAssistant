package bot.Registry.Review.Command;

import bot.CommandParameter;
import bot.Item.Consumable.ConsumableManager;
import bot.ProcessManager;
import bot.Registry.RegistryLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewItemCommand extends ReviewCommand
{
    /**
     * ViewItemCommand constructor
     *
     * @param processManager Process manager
     * @param logger         Logger
     */
    ViewItemCommand(@NotNull ProcessManager processManager, @NotNull RegistryLogger logger)
    {
        super(
            processManager,
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
