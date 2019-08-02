package bot.Registry.Review.Command;

import bot.CommandParameter;
import bot.Explorer.ExplorerManager;
import bot.Player.PlayerRepository;
import bot.ProcessManager;
import bot.Registry.RegistryLogger;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewExplorersCommand extends ReviewCommand
{
    /**
     * ViewExplorersCommand constructor
     *
     * @param processManager Process manager
     * @param logger         Logger
     */
    ViewExplorersCommand(@NotNull ProcessManager processManager, @NotNull RegistryLogger logger)
    {
        super(
            processManager,
            logger,
            "view explorers",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("Owner", false));
                }
            },
            "View explorers. Shows all by default. " +
            "Add 'mine' or a username to view explorers owned by that player in detail."
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event)
    {
        MessageChannel channel   = event.getChannel();
        String         parameter = getStringParameterFromEvent(event);

        if (parameter.length() < 1) {
            getLogger().logExplorersWithOwners(channel, ExplorerManager.getAllExplorers());
        } else {
            String ownerName = parameter.toLowerCase();
            String ownerId;
            if (ownerName.equals("mine")) {
                ownerId = event.getAuthor().getId();
            } else {
                ownerId = PlayerRepository.getPlayerIdByName(ownerName);
            }
            getLogger().logExplorersWithDetails(channel, ExplorerManager.getAllMyExplorers(ownerId));
        }
    }
}
