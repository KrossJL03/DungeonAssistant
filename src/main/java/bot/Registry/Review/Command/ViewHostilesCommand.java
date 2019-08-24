package bot.Registry.Review.Command;

import bot.Hostile.HostileRepository;
import bot.Player.Player;
import bot.Player.PlayerRepository;
import bot.ProcessManager;
import bot.Registry.RegistryLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Hashtable;

public class ViewHostilesCommand extends ReviewCommand
{
    /**
     * ViewHostilesCommand constructor
     *
     * @param processManager Process manager
     * @param logger         Logger
     */
    ViewHostilesCommand(@NotNull ProcessManager processManager, @NotNull RegistryLogger logger)
    {
        super(
            processManager,
            logger,
            "view hostiles",
            new ArrayList<>(),
            "View a list of all hostiles."
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event)
    {
        Player                               player      = PlayerRepository.getPlayer(event.getAuthor().getId());
        ArrayList<Hashtable<String, String>> hostileInfo = player.isMod()
                                                           ? HostileRepository.getInfoForAllHostiles()
                                                           : HostileRepository.getInfoForViewablelHostiles();

        getLogger().logHostileInfo(event.getChannel(), hostileInfo);
    }
}
