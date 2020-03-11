package bot.Battle;

import bot.CommandParameter;
import bot.Explorer.Explorer;
import bot.Explorer.ExplorerManager;
import bot.Player.Player;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class JoinCommand extends BattleCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param holder         Battle holder
     * @param dmChecker      Dungeon master checker
     */
    public JoinCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        super(
            processManager,
            holder,
            dmChecker,
            "join",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("ExplorerName", true));
                    add(new CommandParameter("Nickname", false));
                }
            },
            "Join a battle with your explorer. Option to use a temporary nickname.",
            false
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute(@NotNull MessageReceivedEvent event)
    {
        Player   player       = updatePlayer(event);
        String[] parameters   = getParametersFromEvent(event);
        String   explorerName = parameters[0];
        String   nickname     = parameters.length > 1 ? parameters[1] : null;
        Explorer explorer     = ExplorerManager.getMyExplorer(player.getUserId(), explorerName);

        getBattle().join(explorer, nickname);
    }
}
