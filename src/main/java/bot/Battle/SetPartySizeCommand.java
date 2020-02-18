package bot.Battle;

import bot.CommandParameter;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SetPartySizeCommand extends BattleCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param holder         Battle holder
     * @param dmChecker      Dungeon master checker
     */
    public SetPartySizeCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        super(
            processManager,
            holder,
            dmChecker,
            "maxPlayers",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("Amount", true));
                }
            },
            "Set number of players permitted for this battle.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event)
    {
        String[] parameters = getParametersFromEvent(event);
        int      maxPlayers = Integer.parseInt(parameters[0]);

        getBattle().setMaxPlayerCount(maxPlayers);
    }
}
