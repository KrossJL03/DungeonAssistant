package bot.Battle.HostileEncounter;

import bot.Battle.BattleInterface;
import bot.Battle.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.CustomException;
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
            "Begin creating a new encounter style battle.",
            true
        );
        this.holder = holder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event)
    {
        if (holder.hasActiveBattle()) {
            BattleInterface battle = holder.getBattle();
            throw new CustomException(String.format(
                "Can't start a new battle right now, there's a %s battle in progress",
                battle.getBattleStyle()
            ));
        }

        removeProcessToManager(holder.getBattle());
        holder.createHostileEncounter(event.getChannel(), getDungeonMaster(event).getId());
        addProcessToManager(holder.getBattle());
    }
}
