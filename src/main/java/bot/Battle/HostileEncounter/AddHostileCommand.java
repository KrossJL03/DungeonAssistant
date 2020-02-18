package bot.Battle.HostileEncounter;

import bot.Battle.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.CommandParameter;
import bot.Hostile.Hostile;
import bot.Hostile.HostileRepository;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AddHostileCommand extends EncounterCommand
{
    /**
     * Constructor.
     *
     * @param processManager Processed manager
     * @param holder         Battle holder
     * @param dmChecker      Dungeon master checker
     */
    AddHostileCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        super(
            processManager,
            holder,
            dmChecker,
            "addHostile",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("Species", true));
                    add(new CommandParameter("Nickname", false));
                }
            },
            "Add a hostile to an encounter. Optional nickname.",
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
        Hostile  hostile    = HostileRepository.getHostile(parameters[0]);
        String   name       = parameters.length > 1 ? parameters[1] : parameters[0];

        getHostileEncounter().addHostile(hostile, name);
    }
}
