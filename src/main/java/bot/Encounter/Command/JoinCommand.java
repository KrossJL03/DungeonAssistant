package bot.Encounter.Command;

import bot.CommandParameter;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.Explorer.Explorer;
import bot.Explorer.ExplorerManager;
import bot.Player.Player;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class JoinCommand extends EncounterCommand
{
    /**
     * JoinCommand constructor
     *
     * @param processManager Process manager
     * @param holder         Encounter holder
     * @param logger         Encounter logger
     */
    JoinCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull EncounterLogger logger
    )
    {
        super(
            processManager,
            holder,
            logger,
            "join",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("ExplorerName", true));
                }
            },
            "Join an encounter with your explorer.",
            false
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        Player   player       = getPlayerFromEvent(event);
        String[] parameters   = getParametersFromEvent(event);
        String   explorerName = parameters[0];

        Explorer explorer = ExplorerManager.getMyExplorer(player.getUserId(), explorerName);
        getEncounter().join(explorer);
    }
}
