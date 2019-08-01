package bot.Encounter.Command;

import bot.Encounter.DungeonMasterChecker.DungeonMasterChecker;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CreateHostileEncounterCommand extends EncounterCommand
{
    private EncounterHolder holder;

    /**
     * CreateHostileEncounterCommand constructor
     *
     * @param processManager Process manager
     * @param holder         Encounter holder
     * @param logger         Encounter logger
     * @param dmChecker      Dungeon master checker
     */
    CreateHostileEncounterCommand(
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
