package bot.Battle.Encounter;

import bot.Battle.Battle;
import bot.Battle.BattleInterface;
import bot.Battle.DungeonMasterChecker;
import bot.CustomException;
import bot.Mention;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class CreateEncounterCommand extends EncounterCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param dmChecker      Dungeon master checker
     */
    CreateEncounterCommand(@NotNull ProcessManager processManager, @NotNull DungeonMasterChecker dmChecker)
    {
        super(
            processManager,
            dmChecker,
            "create encounter",
            new ArrayList<>(),
            "Begin creating a new encounter style battle.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute(@NotNull MessageReceivedEvent event)
    {
        if (hasBattle()) {
            BattleInterface battle = getBattle();
            throw new CustomException(String.format(
                "Can't start a new battle right now, there's a %s battle in progress",
                battle.getBattleStyle()
            ));
        }

        Battle battle = new Encounter(event.getChannel(), new Mention(getDungeonMaster(event).getId()));
        addProcessToManager(battle);
    }
}
