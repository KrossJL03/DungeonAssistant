package bot;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelpCommand extends Command implements HelpCommandInterface
{
    private ArrayList<Command> commands;
    private PrivateLogger      logger;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param commands       Help commands
     */
    HelpCommand(@NotNull ProcessManager processManager, @NotNull ArrayList<Command> commands)
    {
        super(
            processManager,
            "help",
            new ArrayList<>(),
            "Receive a description of the bot and list of help commands with specific content.",
            false
        );

        this.commands = commands;
        this.logger = new PrivateLogger(new HelpDefaultMessageBuilder());
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
