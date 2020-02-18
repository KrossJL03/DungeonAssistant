package bot;

import bot.Player.Player;
import bot.Player.PlayerRepository;
import net.dv8tion.jda.core.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PrivateLogger
{
    private HelpMessageBuilderInterface helpMessageBuilder;

    /**
     * Constructor.
     *
     * @param helpMessageBuilder Help message builder
     */
    public PrivateLogger(@NotNull HelpMessageBuilderInterface helpMessageBuilder)
    {
        this.helpMessageBuilder = helpMessageBuilder;
    }

    /**
     * Log help page
     *
     * @param user     User to log help page to
     * @param commands List of commands
     */
    public void logHelpPage(@NotNull User user, @NotNull ArrayList<Command> commands)
    {
        ArrayList<Command> memberCommands = new ArrayList<>();
        ArrayList<Command> modCommands    = new ArrayList<>();
        for (Command command : commands) {
            if (command.isModCommand()) {
                modCommands.add(command);
            } else {
                memberCommands.add(command);
            }
        }

        Player player = PlayerRepository.getPlayer(user.getId());

        sendPrivateMessage(user, helpMessageBuilder.buildDescriptionMessage());
        if (player.isMod() && modCommands.size() > 0) {
            sendPrivateMessage(user, helpMessageBuilder.buildAdminCommandsMessage(modCommands));
        }
        if (memberCommands.size() > 0) {
            sendPrivateMessage(user, helpMessageBuilder.buildMemberCommandsMessage(memberCommands));
        }
    }

    /**
     * Log message to user's private channel
     *
     * @param user    User to send message to
     * @param message Message to send
     */
    private void sendPrivateMessage(@NotNull User user, @NotNull String message)
    {
        user.openPrivateChannel().queue((channel) -> channel.sendMessage(message).queue());
    }
}
