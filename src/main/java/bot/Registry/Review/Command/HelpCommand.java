package bot.Registry.Review.Command;

import bot.Command;
import bot.CommandInterface;
import bot.PrivateLogger;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelpCommand extends Command
{
    private PrivateLogger               privateLogger;
    private ArrayList<CommandInterface> commands;

    /**
     * HelpCommand constructor.
     *
     * @param processManager Process manager
     * @param privateLogger  Private logger
     * @param commands       Commands
     */
    HelpCommand(
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