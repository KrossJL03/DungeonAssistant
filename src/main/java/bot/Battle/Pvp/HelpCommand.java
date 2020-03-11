package bot.Battle.Pvp;

import bot.Command;
import bot.HelpCommandInterface;
import bot.PrivateLogger;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class HelpCommand extends Command implements HelpCommandInterface
{
    private ArrayList<Command> commands;
    private PrivateLogger      logger;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param logger         Private logger
     * @param commands       Battle commands
     */
    HelpCommand(
        @NotNull ProcessManager processManager,
        @NotNull PrivateLogger logger,
        @NotNull ArrayList<Command> commands
    )
    {
        super(
            processManager,
            "help pvp",
            new ArrayList<>(),
            "View all pvp commands.",
            false
        );

        this.commands = commands;
        this.logger = logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event)
    {
        logger.logHelpPage(event.getAuthor(), commands);
    }
}
