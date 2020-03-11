package bot.Battle.PlayerVsPlayer;

import bot.Battle.BattleCommand;
import bot.Battle.BattleInterface;
import bot.Battle.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.Battle.ExplorerRoster;
import bot.CommandParameter;
import bot.CustomException;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class CreatePvpCommand extends BattleCommand
{
    private EncounterHolder holder;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param holder         Battle holder
     * @param dmChecker      Dungeon master checker
     */
    CreatePvpCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        super(
            processManager,
            holder,
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

        this.holder = holder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute(@NotNull MessageReceivedEvent event)
    {
        if (holder.hasActiveBattle()) {
            BattleInterface battle = holder.getBattle();
            throw new CustomException(String.format(
                "Can't start a new battle right now, there's a %s battle in progress",
                battle.getBattleStyle()
            ));
        }

        removeProcessToManager(holder.getBattle());
        holder.createPvp(event.getChannel(), getDungeonMaster(event).getId());
        addProcessToManager(holder.getBattle());
    }
}
