package bot.Encounter.Command;

import bot.CommandParameter;
import bot.Encounter.DungeonMasterChecker.DungeonMasterChecker;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GiveProtectCommand extends EncounterCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param holder         Encounter holder
     * @param logger         Encounter logger
     * @param dmChecker      Dungeon master checker
     */
    GiveProtectCommand(
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
            "give protect",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("Explorer", true));
                    add(new CommandParameter("HP", false));
                }
            },
            "Give an explorer another protect action. Option to heal them by X HP.",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        String[] parameters      = getParametersFromEvent(event);
        String   explorerName    = parameters[0];
        int      hitpointsHealed = parameters.length > 1 ? Integer.parseInt(parameters[1]) : 0;

        getHostileEncounter().healAndGiveProtect(explorerName, hitpointsHealed);
    }
}