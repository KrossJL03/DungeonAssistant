package bot.Battle.Command;

import bot.Battle.DungeonMasterChecker.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.CommandParameter;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HurtCommand extends EncounterCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param holder         Battle holder
     * @param dmChecker      Dungeon master checker
     */
    HurtCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        super(
            processManager,
            holder,
            dmChecker,
            "hurt",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("TargetName", true));
                    add(new CommandParameter("Amount", true));
                }
            },
            "Hurt creature or group by X hitpoints. Group options: pcs, hostiles.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        String[] parameters = getParametersFromEvent(event);
        String   targetName = parameters[0];
        int      amount     = Integer.parseInt(parameters[1]);

        switch (targetName) {
            case "pcs":
                getBattle().hurtAllExplorers(amount);
                break;
            case "hostiles":
                getHostileEncounter().hurtAllHostiles(amount);
                break;
            default:
                getBattle().hurt(targetName, amount);
                break;
        }
    }
}
