package bot.Battle.Command;

import bot.CommandParameter;
import bot.Battle.DungeonMasterChecker.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.Battle.Logger.EncounterLogger;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReviveCommand extends EncounterCommand
{
    /**
     * Constructor
     *
     * @param processManager Process manager
     * @param holder         Encounter holder
     * @param logger         Encounter logger
     * @param dmChecker      Dungeon master checker
     */
    ReviveCommand(
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
            "revive",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("TargetName", true));
                }
            },
            "Revive an explorer to half health.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        String[] parameters = getParametersFromEvent(event);
        String   targetName = parameters[0];

        getHostileEncounter().revive(targetName);
    }
}