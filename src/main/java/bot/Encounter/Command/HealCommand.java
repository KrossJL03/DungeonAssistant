package bot.Encounter.Command;

import bot.CommandParameter;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HealCommand extends EncounterCommand
{
    /**
     * HealCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    HealCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
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
