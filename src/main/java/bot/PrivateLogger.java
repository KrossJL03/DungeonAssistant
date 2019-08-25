package bot;

import net.dv8tion.jda.core.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PrivateLogger
{
    private HelpMessageBuilderInterface helpMessageBuilder;

    /**
     * PrivateLogger constructor.
     *
     * @param helpMessageBuilder Help message builder
     */
    public PrivateLogger(@NotNull HelpMessageBuilderInterface helpMessageBuilder)
    {
        this.helpMessageBuilder = helpMessageBuilder;
    }

    /**
     * Log help page for an admin
     *
     * @param user           User to log help page to
     * @param adminCommands  List of admin commands
     * @param memberCommands List of member commands
     */
    public void logAdminHelpPage(
        User user,
        ArrayList<CommandInterface> adminCommands,
        ArrayList<CommandInterface> memberCommands
    )
    {
        sendPrivateMessage(user, helpMessageBuilder.buildDescriptionMessage());
        if (adminCommands.size() > 0) {
            sendPrivateMessage(user, helpMessageBuilder.buildAdminCommandsMessage(adminCommands));
        }
        if (memberCommands.size() > 0) {
            sendPrivateMessage(user, helpMessageBuilder.buildMemberCommandsMessage(memberCommands));
        }
    }

    /**
     * Log help page for a member
     *
     * @param user     User to log help page to
     * @param commands List of member commands
     */
    public void logMemberHelpPage(User user, ArrayList<CommandInterface> commands)
    {
        sendPrivateMessage(user, helpMessageBuilder.buildDescriptionMessage());
        sendPrivateMessage(user, helpMessageBuilder.buildMemberCommandsMessage(commands));
    }

    /**
     * Log message to user's private channel
     *
     * @param user    User to send message to
     * @param message Message to send
     */
    private void sendPrivateMessage(User user, String message)
    {
        user.openPrivateChannel().queue((channel) -> channel.sendMessage(message).queue());
    }
}
