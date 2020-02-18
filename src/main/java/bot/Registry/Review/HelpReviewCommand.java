package bot.Registry.Review;

import bot.Command;
import bot.HelpCommandInterface;
import bot.PrivateLogger;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelpReviewCommand extends Command implements HelpCommandInterface
{
    private ArrayList<Command> commands;
    private PrivateLogger      logger;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param privateLogger  Private logger
     * @param commands       Commands
     */
    HelpReviewCommand(
        @NotNull ProcessManager processManager,
        @NotNull PrivateLogger privateLogger,
        @NotNull ArrayList<Command> commands
    )
    {
        super(
            processManager,
            "help view",
            new ArrayList<>(),
            "View all commands for viewing information.",
            false
        );

        this.commands = commands;
        this.logger = privateLogger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event) throws ReviewCommandException
    {
        logger.logHelpPage(event.getAuthor(), commands);
    }
}
