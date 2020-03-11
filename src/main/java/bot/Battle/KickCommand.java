package bot.Battle;

import bot.CommandParameter;
import bot.Message;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class KickCommand extends BattleCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param dmChecker      Dungeon master checker
     */
    public KickCommand(@NotNull ProcessManager processManager, @NotNull DungeonMasterChecker dmChecker)
    {
        super(
            processManager,
            dmChecker,
            "kick",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("ExplorerName", true));
                }
            },
            buildDescription(),
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute(@NotNull MessageReceivedEvent event)
    {
        String[] parameters   = getParametersFromEvent(event);
        String   explorerName = parameters[0];

        getBattle().kick(explorerName);
    }

    /**
     * Build description
     *
     * @return String
     */
    private static @NotNull String buildDescription()
    {
        Message description = new Message();

        description.add("Forcibly remove a player from a battle.");
        description.add("The rejoin command cannot be used by the player to return.");
        description.add("The player cannot join again with a different explorer.");

        return description.getAsString();
    }
}
