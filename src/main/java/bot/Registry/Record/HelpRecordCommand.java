package bot.Registry.Record;

import bot.Command;
import bot.HelpCommandInterface;
import bot.PrivateLogger;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelpRecordCommand extends Command implements HelpCommandInterface
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
    HelpRecordCommand(
        @NotNull ProcessManager processManager,
        @NotNull PrivateLogger privateLogger,
        @NotNull ArrayList<Command> commands
    )
    {
        super(
            processManager,
            "help record",
            new ArrayList<>(),
            "View all commands for recording information.",
            false
        );

        this.commands = commands;
        this.logger = privateLogger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event) throws RecordCommandException
    {
        logger.logHelpPage(event.getAuthor(), commands);
    }
}
