package bot.Battle.Encounter;

import bot.Battle.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.CommandParameter;
import bot.Message;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RemoveCommand extends EncounterCommand
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
            "remove",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("CreatureName", true));
                }
            },
            buildDescription(),
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event)
    {
        String creatureName = getStringParameterFromEvent(event);

        getEncounter().remove(creatureName);
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
