package bot.Encounter.Command;

import bot.Encounter.DungeonMasterChecker.DungeonMasterChecker;
import bot.Encounter.EncounterHolder;
import bot.CommandParameter;
import bot.Encounter.Logger.EncounterLogger;
import bot.Player.Player;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AttackCommand extends EncounterCommand
{
    /**
     * AttackCommand constructor
     *
     * @param processManager Process manager
     * @param holder         Encounter holder
     * @param logger         Encounter logger
     * @param dmChecker      Dungeon master checker
     */
    AttackCommand(
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
            "attack",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("CreatureName", true));
                }
            },
            "Attack a creature during the attack turn",
            false
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        Player   player     = getPlayerFromEvent(event);
        String[] parameters = getParametersFromEvent(event);
        String   targetName = parameters[0];

        getEncounter().attackAction(player, targetName);
    }
}
