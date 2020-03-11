package bot.Battle.Encounter;

import bot.Battle.BattleInterface;
import bot.Battle.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.CommandParameter;
import bot.Message;
import bot.Player.Player;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UseItemCommand extends EncounterCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param holder         Battle holder
     * @param dmChecker      Dungeon master checker
     */
    UseItemCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        super(
            processManager,
            holder,
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
    public void execute(@NotNull MessageReceivedEvent event)
    {
        BattleInterface battle = getBattle();

        if (!battle.isOver() && battle instanceof Encounter) {
            Player player = getPlayerFromEvent(event);
            battle.useItemAction(player);
        }
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
