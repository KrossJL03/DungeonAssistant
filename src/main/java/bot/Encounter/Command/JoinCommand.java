package bot.Encounter.Command;

import bot.CommandParameter;
import bot.Encounter.DungeonMasterChecker.DungeonMasterChecker;
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
     * @param dmChecker      Dungeon master checker
     */
    JoinCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull EncounterLogger logger,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        super(
            processManager,
            holder,
            logger,
            dmChecker,
            "join",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("ExplorerName", true));
                    add(new CommandParameter("Nickname", false));
                }
            },
            "Join an encounter with your explorer. Option to use a nickname for the battle.",
            false
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        Player   player       = updatePlayer(event);
        String[] parameters   = getParametersFromEvent(event);
        String   explorerName = parameters[0];
        String   nickname     = parameters.length > 1 ? parameters[1] : null;
        Explorer explorer     = ExplorerManager.getMyExplorer(player.getUserId(), explorerName);

        getEncounter().join(explorer, nickname);
    }
}
