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

public class CreateExplorerCommand extends RecordCommand
{
    /**
     * Constructor
     *
     * @param processManager Process manager
     * @param logger         Logger
     */
    CreateExplorerCommand(@NotNull ProcessManager processManager, @NotNull RegistryLogger logger)
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
    public void handle(@NotNull MessageReceivedEvent event) throws RecordCommandException
    {
        ensureRecordingNotLocked();

        updatePlayer(event);

        MessageChannel channel    = event.getChannel();
        String         authorId   = event.getAuthor().getId();
        String[]       parameters = getParametersFromEvent(event);

        String name = parameters[0];
        int    HP   = Integer.parseInt(parameters[1]);
        int    STR  = Integer.parseInt(parameters[2]);
        int    WIS  = Integer.parseInt(parameters[3]);
        int    AGI  = Integer.parseInt(parameters[4]);
        int    DEF  = Integer.parseInt(parameters[5]);

        // todo add validation for links
        String appLink   = parameters[6];
        String statsLink = parameters[7];

        ExplorerManager.createExplorer(authorId, name, HP, STR, DEF, AGI, WIS, appLink, statsLink);
        channel.sendMessage(String.format("%s record has been saved!", name)).queue();
        getLogger().logExplorersWithDetails(event.getChannel(), ExplorerManager.getAllMyExplorers(authorId));
    }
}
