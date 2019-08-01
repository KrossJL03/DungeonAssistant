package bot.Registry.Review.Command;

import bot.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelpReviewCommand extends Command implements HelpCommandInterface
{
    private PrivateLogger               privateLogger;
    private ArrayList<CommandInterface> commands;

    /**
     * HelpReviewCommand constructor.
     *
     * @param processManager Process manager
     * @param privateLogger  Private logger
     * @param commands       Commands
     */
    HelpReviewCommand(
        @NotNull ProcessManager processManager,
        @NotNull PrivateLogger privateLogger,
        @NotNull ArrayList<CommandInterface> commands
    )
    {
        super(
            processManager,
            "help view",
            new ArrayList<>(),
            "View all commands for viewing information."
        );
        this.commands = commands;
        this.privateLogger = privateLogger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event) throws ReviewCommandException
    {
        privateLogger.logMemberHelpPage(event.getAuthor(), commands);
    }
}
