package bot.Registry.Review.Command;

import bot.CommandParameter;
import bot.Hostile.Hostile;
import bot.Hostile.HostileRepository;
import bot.Registry.RegistryLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewHostileLootCommand extends ReviewCommand
{
    /**
     * ViewExplorersCommand constructor
     *
     * @param logger Logger
     */
    ViewHostileLootCommand(@NotNull RegistryLogger logger)
    {
        super(
            logger,
            "view loot",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("HostileName", true));
                }
            },
            "View the loot for a given hostile."
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event)
    {
        String[] parameters = getParametersFromEvent(event);
        String   species    = parameters[0];
        Hostile  hostile    = HostileRepository.getHostile(species);

        getLogger().logHostileLoot(event.getChannel(), hostile);
    }
}
