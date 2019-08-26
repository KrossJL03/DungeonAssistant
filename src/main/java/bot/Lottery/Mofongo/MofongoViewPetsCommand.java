package bot.Lottery.Mofongo;

import bot.Command;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MofongoViewPetsCommand extends Command
{
    private MofongoLogger logger;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param logger         Logger
     */
    MofongoViewPetsCommand(@NotNull ProcessManager processManager, @NotNull MofongoLogger logger)
    {
        super(
            processManager,
            "view mofongo pets",
            new ArrayList<>(),
            "View all pets configured for Mofongo."
        );
        this.logger = logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event)
    {
        logger.logPetStats(event.getChannel(), MofongoPetStatRegistry.getAllPetStats());
    }
}
