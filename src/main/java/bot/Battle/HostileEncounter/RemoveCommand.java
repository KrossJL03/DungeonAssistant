package bot.Battle.HostileEncounter;

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
            "CreatureName"
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

        description.add("Remove a creature from the battle.");
        description.add("If an explorer is removed the player may join again with any explorer.");

        return description.getAsString();
    }
}
