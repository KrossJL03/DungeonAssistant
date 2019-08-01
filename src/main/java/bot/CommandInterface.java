package bot;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface CommandInterface
{
    /**
     * Get command name
     *
     * @return String
     */
    @NotNull String getCommandName();

    /**
     * Get command description
     *
     * @return String
     */
    @NotNull String getDescription();

    /**
     * Get parameter names
     *
     * @return ArrayList<String>
     */
    @NotNull ArrayList<CommandParameter> getParameters();

    /**
     * Handle the given event
     *
     * @param event Event
     */
    void handle(@NotNull MessageReceivedEvent event);

    /**
     * Is this the command in string form
     *
     * @param commandString Command string
     *
     * @return boolean
     */
    boolean isCommand(@NotNull String commandString);
}
