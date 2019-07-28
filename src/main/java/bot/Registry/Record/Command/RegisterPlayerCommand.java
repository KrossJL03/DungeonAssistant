package bot.Registry.Record.Command;

import bot.Player.PlayerManager;
import bot.Registry.RegistryLogger;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RegisterPlayerCommand extends RecordCommand
{
    /**
     * RegisterPlayerCommand constructor
     *
     * @param logger       Logger
     */
    RegisterPlayerCommand(@NotNull RegistryLogger logger)
    {
        super(
            logger,
            "hello",
            new ArrayList<>(),
            "Introduce yourself to the bot to create a player record."
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event) throws RecordCommandException
    {
        ensureRecordingNotLocked();

        MessageChannel channel = event.getChannel();
        String         nickname = event.getMember().getNickname();
        if (nickname == null) {
            nickname = event.getAuthor().getName();
        }

        PlayerManager.savePlayer(event.getAuthor().getId(), nickname);
        channel.sendMessage(String.format("Hi %s! I've got all your information down now.", nickname)).queue();
    }
}
