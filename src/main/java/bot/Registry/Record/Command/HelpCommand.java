package bot.Registry.Record.Command;

import bot.CommandParameter;
import bot.Registry.RegistryLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelpCommand extends RecordCommand
{
    /**
     * HelpCommand constructor
     *
     * @param logger       Logger
     */
    HelpCommand(@NotNull RegistryLogger logger)
    {
        super(
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
    public void handle(@NotNull MessageReceivedEvent event) throws RecordCommandException
    {
        event.getChannel().sendMessage("Help").queue();
    }
}
