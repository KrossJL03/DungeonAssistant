package bot.Registry.Review.Command;

import bot.CommandParameter;
import bot.Hostile.Hostile;
import bot.Hostile.HostileRepository;
import bot.Player.Player;
import bot.Player.PlayerRepository;
import bot.ProcessManager;
import bot.Registry.RegistryLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewHostileLootCommand extends ReviewCommand
{
    /**
     * ViewHostileLootCommand constructor
     *
     * @param processManager Process manager
     * @param logger         Logger
     */
    ViewHostileLootCommand(@NotNull ProcessManager processManager, @NotNull RegistryLogger logger)
    {
        super(
            processManager,
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
        Player   player     = PlayerRepository.getPlayer(event.getAuthor().getId());
        String[] parameters = getParametersFromEvent(event);
        String   species    = parameters[0];
        Hostile  hostile    = player.isMod()
                              ? HostileRepository.getHostile(species)
                              : HostileRepository.getViewableHostile(species);

        getLogger().logHostileLoot(event.getChannel(), hostile);
    }
}
