package bot.Registry.Record.Command;

import bot.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelpRecordCommand extends Command implements HelpCommandInterface
{
    private PrivateLogger               privateLogger;
    private ArrayList<CommandInterface> commands;

    /**
     * HelpRecordCommand constructor.
     *
     * @param processManager Process manager
     * @param privateLogger  Private logger
     * @param commands       Commands
     */
    HelpRecordCommand(
        @NotNull ProcessManager processManager,
        @NotNull PrivateLogger privateLogger,
        @NotNull ArrayList<CommandInterface> commands
    )
    {
        super(
            processManager,
            "help record",
            new ArrayList<>(),
            "View all commands for recording information."
        );
        this.commands = commands;
        this.privateLogger = privateLogger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event) throws RecordCommandException
    {
        privateLogger.logMemberHelpPage(event.getAuthor(), commands);
    }
}
