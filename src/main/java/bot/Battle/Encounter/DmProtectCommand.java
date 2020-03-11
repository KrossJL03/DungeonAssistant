package bot.Battle.Encounter;

import bot.Battle.DungeonMasterChecker;
import bot.CommandParameter;
import bot.Message;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class DmProtectCommand extends EncounterCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param dmChecker      Dungeon master checker
     */
    DmProtectCommand(@NotNull ProcessManager processManager, @NotNull DungeonMasterChecker dmChecker)
    {
        super(
            processManager,
            dmChecker,
            "dmProtect",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("TargetName", true));
                    add(new CommandParameter("HP", false));
                }
            },
            buildDescription(),
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute(@NotNull MessageReceivedEvent event)
    {
        String[] parameters      = getParametersFromEvent(event);
        String   explorerName    = parameters[0];
        int      hitpointsHealed = parameters.length > 1 ? Integer.parseInt(parameters[1]) : 0;

        getEncounter().manualProtectAction(explorerName, hitpointsHealed);
    }

    /**
     * Build description
     *
     * @return String
     */
    private static @NotNull String buildDescription()
    {
        Message description = new Message();

        description.add("Manually make the current explorer protect a target.");
        description.add("Option to heal protector by X HP.");

        return description.getAsString();
    }
}