package bot.Battle.Encounter;

import bot.Battle.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.Message;
import bot.Player.Player;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DodgePassCommand extends EncounterCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param holder         Battle holder
     * @param dmChecker      Dungeon master checker
     */
    DodgePassCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        super(
            processManager,
            holder,
            dmChecker,
            "dodgePass",
            new ArrayList<>(),
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
        Player player = getPlayerFromEvent(event);

        getEncounter().dodgePassRequestAction(player);
    }

    /**
     * Build description
     *
     * @return String
     */
    private static @NotNull String buildDescription()
    {
        Message description = new Message();

        description.add("Auto dodge all attacks. Use for smoke bombs or special abilities.");
        description.add("DM will be pinged to confirm.");

        return description.getAsString();
    }
}
