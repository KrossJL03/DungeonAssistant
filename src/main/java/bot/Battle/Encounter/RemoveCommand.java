package bot.Battle.Encounter;

import bot.Battle.DungeonMasterChecker;
import bot.CommandParameter;
import bot.Message;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class RemoveCommand extends EncounterCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param dmChecker      Dungeon master checker
     */
    RemoveCommand(@NotNull ProcessManager processManager, @NotNull DungeonMasterChecker dmChecker)
    {
        super(
            processManager,
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
    protected void execute(@NotNull MessageReceivedEvent event)
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
