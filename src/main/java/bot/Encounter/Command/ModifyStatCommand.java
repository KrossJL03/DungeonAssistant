package bot.Encounter.Command;

import bot.CommandParameter;
import bot.Encounter.DungeonMasterChecker.DungeonMasterChecker;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ModifyStatCommand extends EncounterCommand
{
    /**
     * ModifyStatCommand constructor
     *
     * @param processManager Process manager
     * @param holder         Encounter holder
     * @param logger         Encounter logger
     * @param dmChecker      Dungeon master checker
     */
    ModifyStatCommand(
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
            "modify",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("TargetName", true));
                    add(new CommandParameter("StatName", true));
                    add(new CommandParameter("Amount", true));
                }
            },
            "Temporarily set a target or group's stat to the amount for the duration of the encounter. " +
            "Add '+' or '-' to the amount to increase or decrease the stat by the given amount. " +
            "Group options: pcs, hostiles.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        String[] parameters  = getParametersFromEvent(event);
        String   targetName  = parameters[0];
        String   statName    = parameters[1];
        String   valueString = parameters[2];
        boolean  isModify    = false;
        int      valueInt;

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
                getHostileEncounter().modifyStatForAllExplorers(statName, valueInt);
                break;
            case "hostiles":
                getHostileEncounter().modifyStatForAllHostiles(statName, valueInt);
                break;
            default:
                if (isModify) {
                    getHostileEncounter().modifyStat(targetName, statName, valueInt);
                } else {
                    getHostileEncounter().setStat(targetName, statName, valueInt);
                }
                break;
        }
    }
}
