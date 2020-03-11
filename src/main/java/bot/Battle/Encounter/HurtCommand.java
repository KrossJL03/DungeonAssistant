package bot.Battle.Encounter;

import bot.Battle.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.CommandParameter;
import bot.Message;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class HurtCommand extends EncounterCommand
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
        Encounter encounter  = getEncounter();
        String[]  parameters = getParametersFromEvent(event);
        String    targetName = parameters[0];
        int       amount     = Integer.parseInt(parameters[1]);

        switch (targetName) {
            case "pcs":
                encounter.hurtAllExplorers(amount);
                break;
            case "hostiles":
                encounter.hurtAllHostiles(amount);
                break;
            default:
                encounter.hurt(targetName, amount);
                break;
        }
    }

    /**
     * Build description
     *
     * @return String
     */
    private static @NotNull String buildDescription()
    {
        Message description = new Message();

        description.add("Hurt creature or group by X hitpoints.");
        description.add("Group options: pcs, hostiles.");

        return description.getAsString();
    }
}
