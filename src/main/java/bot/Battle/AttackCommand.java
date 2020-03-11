package bot.Battle;

import bot.CommandParameter;
import bot.Player.Player;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

abstract public class AttackCommand extends BattleCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param holder         Battle holder
     * @param dmChecker      Dungeon master checker
     * @param description    Description
     * @param parameterName  Name of the parameter
     */
    protected AttackCommand(
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
            "attack",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter(parameterName, true));
                }
            },
            description,
            false
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute(@NotNull MessageReceivedEvent event)
    {
        Player   player     = getPlayerFromEvent(event);
        String[] parameters = getParametersFromEvent(event);
        String   targetName = parameters[0];

        getBattle().attackAction(player, targetName);
    }
}
