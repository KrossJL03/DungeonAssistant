package bot;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Entry point for all commands
 */
public class CommandListener extends ListenerAdapter
{
    private ArrayList<Command> commands;

    /**
     * Constructor.
     *
     * @param commands Commands
     */
    public CommandListener(@NotNull ArrayList<Command> commands)
    {
        this.commands = commands;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (!event.getAuthor().isBot()) {
            processMessage(event);
        }
    }

    /**
     * Process message
     *
     * @param event Event
     */
    private void processMessage(@NotNull MessageReceivedEvent event)
    {
        Message        message = event.getMessage();
        MessageChannel channel = event.getChannel();
        String         input   = message.getContentRaw();

        try {
            for (Command command : commands) {
                if (command.isCommand(input)) {
                    command.handle(event);
                    return;
                }
            }

            if (input.startsWith(MyProperties.COMMAND_PREFIX)) {
                channel.sendMessage("Sorry, did you say something? I don't know that command").queue();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            channel.sendMessage(
                String.format(
                    "Could you say that again? I think I'm missing something... Check `%shelp`",
                    MyProperties.COMMAND_PREFIX
                )
            ).queue();
        } catch (NumberFormatException exception) {
            channel.sendMessage("I need a number and I could be wrong but I don't think that was a number...").queue();
        } catch (CustomException exception) {
            channel.sendMessage(exception.getMessage()).queue();
        } catch (Throwable exception) {
            channel.sendMessage("Something went wrong, but I'm not sure what...").queue();
            System.out.println(exception.toString());
        }
    }
}