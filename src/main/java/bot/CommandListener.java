package bot;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;

/**
 * Entry point for all commands
 */
public class CommandListener extends ListenerAdapter
{
    public static String COMMAND_KEY = "?";

    private ArrayList<CommandInterface> commands;
    private ArrayList<CommandInterface> additionalCommands;

    /**
     * CommandListener constructor.
     *
     * @param commands           Commands for this bot
     * @param additionalCommands Additional commands for handling other bot commands
     */
    public CommandListener(
        ArrayList<CommandInterface> commands,
        ArrayList<CommandInterface> additionalCommands
    )
    {
        this.additionalCommands = additionalCommands;
        this.commands = commands;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (!event.getAuthor().isBot()) {
            this.processMessage(event);
        }
    }


    /**
     * Process message
     *
     * @param event Event
     */
    private void processMessage(MessageReceivedEvent event)
    {
        Message        message = event.getMessage();
        MessageChannel channel = event.getChannel();
        String         input   = message.getContentRaw();

        try {
            if (input.startsWith(CommandListener.COMMAND_KEY)) {
                String commandString = input.substring(1).toLowerCase();

                for (CommandInterface command : commands) {
                    if (command.isCommand(commandString)) {
                        command.handle(event);
                        return;
                    }
                }

                channel.sendMessage("Sorry, did you say something? I don't know that command").queue();
            } else {
                String[] splitArray  = input.split("\\s+");
                String   commandName = splitArray[0].toLowerCase();
                for (CommandInterface command : additionalCommands) {
                    if (command.isCommand(commandName)) {
                        command.handle(event);
                        return;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            channel.sendMessage(
                String.format(
                    "Could you say that again? I think I'm missing something... Check `%shelp`",
                    CommandListener.COMMAND_KEY
                )
            ).queue();
        } catch (NumberFormatException e) {
            channel.sendMessage("Hey could you say that again? Sorry, I'm bad with numbers.").queue();
        } catch (Throwable e) {
            if (e instanceof CustomExceptionInterface) {
                channel.sendMessage(e.getMessage()).queue();
            } else {
                channel.sendMessage("Something went wrong, but I'm not sure what...").queue();
                System.out.println(e.toString());
            }
        }
    }
}