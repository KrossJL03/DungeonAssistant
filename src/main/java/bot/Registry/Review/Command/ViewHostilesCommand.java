package bot.Registry.Review.Command;

import bot.Hostile.HostileRepository;
import bot.ProcessManager;
import bot.Registry.RegistryLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
        getLogger().logHostileInfo(event.getChannel(), HostileRepository.getInfoForAllHostiles());
    }
}
