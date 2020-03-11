package bot.Battle;

import bot.Player.Player;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

abstract public class LeaveCommand extends BattleCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param dmChecker      Dungeon master checker
     */
    protected LeaveCommand(
        @NotNull ProcessManager processManager,
        @NotNull DungeonMasterChecker dmChecker,
        @NotNull String description
    )
    {
        super(
            processManager,
            dmChecker,
            "leave",
            new ArrayList<>(),
            description,
            false
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute(@NotNull MessageReceivedEvent event)
    {
        Player player = getPlayerFromEvent(event);

        getBattle().leave(player);
    }
}
