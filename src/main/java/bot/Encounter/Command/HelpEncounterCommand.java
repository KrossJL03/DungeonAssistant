package bot.Encounter.Command;

import bot.*;
import bot.Encounter.DungeonMasterChecker.DungeonMasterChecker;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelpEncounterCommand extends Command implements HelpCommandInterface
{
    private DungeonMasterChecker        dmChecker;
    private PrivateLogger               logger;
    private ArrayList<CommandInterface> adminCommands;
    private ArrayList<CommandInterface> memberCommands;

    /**
     * HelpEncounterCommand constructor.
     *
     * @param processManager Process manager
     * @param dmChecker      Dungeon master checker
     * @param logger         Private logger
     * @param commands       Encounter commands
     */
    HelpEncounterCommand(
        @NotNull ProcessManager processManager,
        @NotNull DungeonMasterChecker dmChecker,
        @NotNull PrivateLogger logger,
        @NotNull ArrayList<EncounterCommand> commands
    )
    {
        super(
            processManager,
            "help encounter",
            new ArrayList<>(),
            "View all encounter commands."
        );
        this.adminCommands = new ArrayList<>();
        this.dmChecker = dmChecker;
        this.logger = logger;
        this.memberCommands = new ArrayList<>();

        for (EncounterCommand command : commands) {
            if (command.isDmExclusive()) {
                adminCommands.add(command);
            } else {
                memberCommands.add(command);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        if (dmChecker.isDungeonMaster(event)) {
            logger.logAdminHelpPage(event.getAuthor(), adminCommands, memberCommands);
        } else {
            logger.logMemberHelpPage(event.getAuthor(), memberCommands);
        }
    }
}
