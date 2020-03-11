package bot.Battle.Encounter;

import bot.Battle.DungeonMasterChecker;
import bot.Player.Player;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class LootCommand extends EncounterCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param dmChecker      Dungeon master checker
     */
    LootCommand(@NotNull ProcessManager processManager, @NotNull DungeonMasterChecker dmChecker)
    {
        super(
            processManager,
            dmChecker,
            "loot",
            new ArrayList<>(),
            "Collect materials and rewards at the end of an encounter.",
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

        getEncounter().lootAction(player);
    }
}
