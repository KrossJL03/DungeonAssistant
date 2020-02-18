package bot.Lottery.Pan;

import bot.Command;
import bot.HelpCommandInterface;
import bot.PrivateLogger;
import bot.ProcessManager;
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
     * @param logger         Private logger
     * @param commands       Commands
     */
    HelpCommand(
        @NotNull ProcessManager processManager,
        @NotNull PrivateLogger logger,
        @NotNull ArrayList<Command> commands
    )
    {
        super(
            processManager,
            "help pan",
            new ArrayList<>(),
            "View all commands related to Pan's Scavenger Hunt.",
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
