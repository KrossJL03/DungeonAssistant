package bot.Battle;

import bot.CommandParameter;
import bot.Message;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

abstract public class RemoveCommand extends BattleCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param holder         Battle holder
     * @param dmChecker      Dungeon master checker
     * @param description    Description
     * @param parameterName  Parameter name
     */
    protected RemoveCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull DungeonMasterChecker dmChecker,
        @NotNull String description,
        @NotNull String parameterName
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
                    add(new CommandParameter(parameterName, true));
                }
            },
            description,
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

        description.add("Remove a creature from a battle.");
        description.add("If an explorer is removed the player may join again with any explorer.");

        return description.getAsString();
    }
}
