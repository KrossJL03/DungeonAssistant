package bot.Battle;

import bot.Player.Player;
import bot.Player.PlayerRepository;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

abstract public class EndBattleCommand extends BattleCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param holder         Battle holder
     * @param dmChecker      Dungeon master checker
     */
    protected EndBattleCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull DungeonMasterChecker dmChecker,
        boolean isDmExclusive
    )
    {
        super(
            processManager,
            holder,
            dmChecker,
            "endBattle",
            new ArrayList<>(),
            "End the battle.",
            isDmExclusive
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute(@NotNull MessageReceivedEvent event)
    {
        Player player = PlayerRepository.getPlayer(event.getAuthor().getId());
        getBattle().endBattle(player);
    }
}
