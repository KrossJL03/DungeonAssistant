package bot.Encounter.Command;

import bot.Encounter.DungeonMasterChecker.DungeonMasterChecker;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PassCommand extends EncounterCommand
{
    /**
     * PassCommand constructor
     *
     * @param processManager Process manager
     * @param holder         Encounter holder
     * @param logger         Encounter logger
     * @param dmChecker      Dungeon master checker
     */
    PassCommand(
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
            "pass",
            new ArrayList<>(),
            "The current explorer passes their turn successfully. Used to dodge all attacks.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        getHostileEncounter().passAction();
    }
}
