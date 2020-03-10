package bot.Registry.Record.Command;

import bot.Player.Player;
import bot.ProcessManager;
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
     * @param processManager Process manager
     * @param logger         Logger
     */
    RegisterPlayerCommand(@NotNull ProcessManager processManager, @NotNull RegistryLogger logger)
    {
        super(
            processManager,
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
        Player         player  = updatePlayer(event);
        MessageChannel channel = event.getChannel();

        channel.sendMessage(String.format("Hi %s! I've got all your information down now.", player.getName())).queue();
    }
}
