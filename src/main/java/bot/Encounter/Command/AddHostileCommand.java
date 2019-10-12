package bot.Encounter.Command;

import bot.CommandParameter;
import bot.Encounter.DungeonMasterChecker.DungeonMasterChecker;
import bot.Encounter.EncounterHolder;
import bot.Encounter.EncounteredHostileInterface;
import bot.Encounter.Logger.EncounterLogger;
import bot.Hostile.Hostile;
import bot.Hostile.HostileRepository;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AddHostileCommand extends EncounterCommand
{
    /**
     * AddHostileCommand constructor
     *
     * @param processManager Processed manager
     * @param holder         Encounter holder
     * @param logger         Encounter Logger
     * @param dmChecker      Dungeon master checker
     */
    AddHostileCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull EncounterLogger logger,
        @NotNull DungeonMasterChecker dmChecker
    ){
        super(
            processManager,
            holder,
            logger,
            dmChecker,
            "addHostile",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("Species", true));
                    add(new CommandParameter("Nickname", false));
                }
            },
            "Add a hostile to an encounter.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        String[]                    parameters = getParametersFromEvent(event);
        Hostile                     hostile    = HostileRepository.getHostile(parameters[0]);
        String                      name       = parameters.length > 1 ? parameters[1] : parameters[0];
        EncounteredHostileInterface result     = getHostileEncounter().addHostile(hostile, name);
        getLogger().logAddedHostile(result);
    }
}
