package bot.Battle.Command;

import bot.Battle.DungeonMasterChecker.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CreateHostileEncounterCommand extends EncounterCommand
{
    private EncounterHolder holder;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param holder         Battle holder
     * @param dmChecker      Dungeon master checker
     */
    CreateHostileEncounterCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        super(
            processManager,
            holder,
            dmChecker,
            "create encounter",
            new ArrayList<>(),
            "Begin creating a new encounter.",
            true
        );
        this.holder = holder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        removeProcessToManager(holder.getEncounter());
        holder.createHostileEncounter(event.getChannel(), getDungeonMaster(event).getId());
        addProcessToManager(holder.getEncounter());
    }
}
