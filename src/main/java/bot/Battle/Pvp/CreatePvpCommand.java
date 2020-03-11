package bot.Battle.Pvp;

import bot.Battle.Battle;
import bot.Battle.BattleCommand;
import bot.Battle.BattleInterface;
import bot.Battle.DungeonMasterChecker;
import bot.Battle.ExplorerRoster;
import bot.CommandParameter;
import bot.CustomException;
import bot.Mention;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class CreatePvpCommand extends BattleCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param dmChecker      Dungeon master checker
     */
    CreatePvpCommand(@NotNull ProcessManager processManager, @NotNull DungeonMasterChecker dmChecker)
    {
        super(
            processManager,
            dmChecker,
            "create pvp",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("PartySize", false));
                }
            },
            String.format(
                "Start a new pvp style battle. Default party size is %s.",
                ExplorerRoster.DEFAULT_SIZE
            ),
            false
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute(@NotNull MessageReceivedEvent event)
    {
        if (hasBattle()) {
            BattleInterface battle = getBattle();
            throw new CustomException(String.format(
                "Can't start a new battle right now, there's a %s battle in progress",
                battle.getBattleStyle()
            ));
        }

        Battle battle = new Pvp(event.getChannel(), new Mention(getDungeonMaster(event).getId()));
        addProcessToManager(battle);
    }
}
