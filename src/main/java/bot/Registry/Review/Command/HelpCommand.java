package bot.Registry.Review.Command;

import bot.CommandParameter;
import bot.ProcessManager;
import bot.Registry.RegistryLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelpCommand extends ReviewCommand
{
    /**
     * HelpCommand constructor
     *
     * @param processManager Process manager
     * @param logger         Logger
     */
    HelpCommand(@NotNull ProcessManager processManager, @NotNull RegistryLogger logger)
    {
        super(
            processManager,
            logger,
            "create explorer",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("Name", true));
                    add(new CommandParameter("HP", true));
                    add(new CommandParameter("STR", true));
                    add(new CommandParameter("WIS", true));
                    add(new CommandParameter("AGI", true));
                    add(new CommandParameter("DEF", true));
                }
            },
            "Create an explorer."
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event) throws ReviewCommandException
    {
        event.getChannel().sendMessage("Help").queue();
    }
}
