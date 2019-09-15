package bot.Registry.Record.Command;

import bot.CommandParameter;
import bot.Explorer.ExplorerManager;
import bot.MyProperties;
import bot.ProcessManager;
import bot.Registry.RegistryLogger;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DeleteCharacterCommand extends RecordCommand
{
    /**
     * DeleteCharacterCommand constructor
     *
     * @param processManager Process manager
     * @param logger         Logger
     */
    DeleteCharacterCommand(@NotNull ProcessManager processManager, @NotNull RegistryLogger logger)
    {
        super(
            processManager,
            logger,
            "delete explorer",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("Name", true));
                }
            },
            "Delete an explorer you own with the given name."
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event) throws RecordCommandException
    {
        ensureRecordingNotLocked();

        MessageChannel channel    = event.getChannel();
        String         authorId   = event.getAuthor().getId();
        String[]       parameters = getParametersFromEvent(event);
        String         name       = parameters[0];

        ExplorerManager.deleteExplorer(authorId, name);
        channel.sendMessage(String.format("%s record has been deleted!", name)).queue();
        getLogger().logExplorersWithDetails(event.getChannel(), ExplorerManager.getAllMyExplorers(authorId));
    }
}
