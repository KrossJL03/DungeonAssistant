package bot.Battle.Encounter;

import bot.Battle.DungeonMasterChecker;
import bot.Player.Player;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class DodgeCommand extends EncounterCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param dmChecker      Dungeon master checker
     */
    DodgeCommand(@NotNull ProcessManager processManager, @NotNull DungeonMasterChecker dmChecker)
    {
        super(
            processManager,
            dmChecker,
            "dodge",
            new ArrayList<>(),
            "Attempt to dodge enemy attacks during the dodge turn.",
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

        getEncounter().dodgeAction(player);
    }
}
