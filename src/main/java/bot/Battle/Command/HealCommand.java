package bot.Battle.Command;

import bot.CommandParameter;
import bot.Battle.DungeonMasterChecker.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.Battle.Logger.EncounterLogger;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HealCommand extends EncounterCommand
{
    /**
     * HealCommand constructor
     *
     * @param processManager Process manager
     * @param holder         Battle holder
     * @param logger         Battle logger
     * @param dmChecker      Dungeon master checker
     */
    HealCommand(
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
            "heal",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("TargetName", true));
                    add(new CommandParameter("Amount", true));
                }
            },
            "Heal creature or group by X hitpoints. Group options: pcs, hostiles.",
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
                getHostileEncounter().healAllExplorers(amount);
                break;
            case "hostiles":
                getHostileEncounter().healAllHostiles(amount);
                break;
            default:
                getEncounter().heal(targetName, amount);
                break;
        }
    }
}
