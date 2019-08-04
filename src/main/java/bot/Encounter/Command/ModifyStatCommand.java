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
            "Temporarily modify a creature's stat by the amount for the duration of the encounter. " +
            "Add '-' to the amount to decrease the stat by the given amount.",
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
        String   boostString = parameters[2];

        if (boostString.startsWith("-")) {
            int boostAmount = Integer.parseInt(boostString.substring(1).trim());
            getEncounter().modifyStat(targetName, statName, 0 - boostAmount);
        } else if (boostString.startsWith("+")) {
            int boostAmount = Integer.parseInt(boostString.substring(1).trim());
            getEncounter().modifyStat(targetName, statName, boostAmount);
        } else {
            int boostAmount = Integer.parseInt(boostString.trim());
            getEncounter().modifyStat(targetName, statName, boostAmount);
        }
    }
}
