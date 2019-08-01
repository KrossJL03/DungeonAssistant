package bot.Encounter.Command;

import bot.CommandParameter;
import bot.Encounter.DungeonMasterChecker.DungeonMasterChecker;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StatBoostCommand extends EncounterCommand
{
    /**
     * StatBoostCommand constructor
     *
     * @param processManager Process manager
     * @param holder         Encounter holder
     * @param logger         Encounter logger
     * @param dmChecker      Dungeon master checker
     */
    StatBoostCommand(
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
            "statBoost",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("Name", true));
                    add(new CommandParameter("StatName", true));
                    add(new CommandParameter("Amount", true));
                }
            },
            "Temporarily increase an explorer's stat by the boost amount for the duration of the encounter.",
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
        int      boostAmount = Integer.parseInt(parameters[2]);
        getEncounter().modifyStat(targetName, statName, boostAmount);
    }
}
