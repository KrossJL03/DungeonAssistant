package bot.Battle.Command;

import bot.Battle.DungeonMasterChecker.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.Battle.EncounterInterface;
import bot.Battle.Logger.EncounterLogger;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewSummaryCommand extends EncounterCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param holder         Encounter holder
     * @param logger         Encounter logger
     * @param dmChecker      Dungeon master checker
     */
    ViewSummaryCommand(
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
            "view summary",
            new ArrayList<>(),
            "View encounter summary.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        EncounterInterface encounter = getEncounter();
        getLogger().logSummary(encounter.getAllCreatures());
    }
}
