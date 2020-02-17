package bot.Battle.Command;

import bot.Battle.DungeonMasterChecker.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.Battle.Logger.EncounterLogger;
import bot.Player.Player;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DodgePassCommand extends EncounterCommand
{
    /**
     * DodgePassCommand constructor
     *
     * @param processManager Process manager
     * @param holder         Battle holder
     * @param logger         Battle logger
     * @param dmChecker      Dungeon master checker
     */
    DodgePassCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull EncounterLogger logger,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        super(
            processManager,
            holder,
            logger,
            dmChecker,
            "dodgePass",
            new ArrayList<>(),
            "Auto dodge all attacks. Use for smoke bombs, special abilities, or being protected." +
            " DM will be pinged to confirm.",
            false
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        Player player = getPlayerFromEvent(event);

        getLogger().pingDmDodgePass(player);
    }
}
