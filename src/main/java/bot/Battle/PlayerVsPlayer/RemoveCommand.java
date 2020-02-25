package bot.Battle.PlayerVsPlayer;

import bot.Battle.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.Message;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class RemoveCommand extends bot.Battle.RemoveCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param holder         Battle holder
     * @param dmChecker      Dungeon master checker
     */
    RemoveCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        super(
            processManager,
            holder,
            dmChecker,
            buildDescription(),
            "ExplorerName"
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event)
    {
        String creatureName = getStringParameterFromEvent(event);

        getBattle().remove(creatureName);
    }

    /**
     * Build description
     *
     * @return String
     */
    private static @NotNull String buildDescription()
    {
        Message description = new Message();

        description.add("Remove an explorer from the battle.");
        description.add("Player may join again with any explorer.");

        return description.getAsString();
    }
}
