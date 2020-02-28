package bot.Battle.HostileEncounter;

import bot.Battle.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.CommandParameter;
import bot.Message;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class ModifyStatCommand extends EncounterCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param holder         Battle holder
     * @param dmChecker      Dungeon master checker
     */
    ModifyStatCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        super(
            processManager,
            holder,
            dmChecker,
            "modify",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("TargetName", true));
                    add(new CommandParameter("StatName", true));
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
    public void execute(@NotNull MessageReceivedEvent event)
    {
        HostileEncounter encounter   = getHostileEncounter();
        String[]         parameters  = getParametersFromEvent(event);
        String           targetName  = parameters[0];
        String           statName    = parameters[1];
        String           valueString = parameters[2];
        boolean          isModify    = false;
        int              valueInt;

        if (valueString.startsWith("-")) {
            valueInt = 0 - Integer.parseInt(valueString.substring(1).trim());
            isModify = true;
        } else if (valueString.startsWith("+")) {
            valueInt = Integer.parseInt(valueString.substring(1).trim());
            isModify = true;
        } else {
            valueInt = Integer.parseInt(valueString.trim());
        }

        switch (targetName) {
            case "pcs":
                encounter.modifyStatForAllExplorers(statName, valueInt);
                break;
            case "hostiles":
                encounter.modifyStatForAllHostiles(statName, valueInt);
                break;
            default:
                if (isModify) {
                    encounter.modifyStat(targetName, statName, valueInt);
                } else {
                    encounter.setStat(targetName, statName, valueInt);
                }
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

        description.add("Temporarily set a target or group's stat to the amount for the duration of the battle.");
        description.add("Add '+' or '-' to the amount to increase or decrease the stat by the given amount.");
        description.add("Group options: pcs, hostiles.");

        return description.getAsString();
    }
}
