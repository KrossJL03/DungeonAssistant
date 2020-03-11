package bot.Battle.Encounter;

import bot.Battle.BattleInterface;
import bot.Battle.DungeonMasterChecker;
import bot.CommandParameter;
import bot.Message;
import bot.Player.Player;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class UseItemCommand extends EncounterCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param dmChecker      Dungeon master checker
     */
    UseItemCommand(@NotNull ProcessManager processManager, @NotNull DungeonMasterChecker dmChecker)
    {
        super(
            processManager,
            dmChecker,
            "rp!use",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("ItemName", true));
                    add(new CommandParameter("Amount", true));
                }
            },
            buildDescription(),
            false
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExternalCommand()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute(@NotNull MessageReceivedEvent event)
    {
        BattleInterface battle = getBattle();

        if (!battle.isOver() && battle instanceof Encounter) {
            Player player = getPlayerFromEvent(event);
            battle.useItemAction(player);
        }
    }

    /**
     * Build description
     *
     * @return String
     */
    private static @NotNull String buildDescription()
    {
        Message description = new Message();

        description.add("Use an item through rp!bot.");
        description.add("The DM will be pinged to activate the item.");

        return description.getAsString();
    }
}
