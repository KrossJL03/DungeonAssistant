package bot.Lottery;

import bot.Command;
import bot.CommandInterface;
import bot.HelpCommandInterface;
import bot.PrivateLogger;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LotteryHelpCommand extends Command implements HelpCommandInterface
{
    private PrivateLogger               privateLogger;
    private ArrayList<CommandInterface> commands;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param privateLogger  Private logger
     * @param commands       Commands
     */
    LotteryHelpCommand(
        @NotNull ProcessManager processManager,
        @NotNull PrivateLogger privateLogger,
        @NotNull ArrayList<CommandInterface> commands
    )
    {
        super(
            processManager,
            "help lottery",
            new ArrayList<>(),
            "View all commands related to lotteries."
        );
        this.commands = commands;
        this.privateLogger = privateLogger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event)
    {
        privateLogger.logAdminHelpPage(event.getAuthor(), commands, new ArrayList<>());
    }
}
