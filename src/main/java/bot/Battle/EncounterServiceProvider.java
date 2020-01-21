package bot.Battle;

import bot.CommandFactoryInterface;
import bot.Battle.Command.EncounterCommandFactory;
import bot.Battle.DungeonMasterChecker.DungeonMasterChecker;
import bot.Battle.Logger.EncounterLogger;
import bot.Battle.Logger.Message.Action.ActionMessageBuilder;
import bot.Battle.Logger.Message.Help.HelpEncounterMessageBuilder;
import bot.Battle.Logger.Message.PhaseChange.PhaseChangeMessageBuilder;
import bot.Battle.Logger.Message.Summary.SummaryMessageBuilder;
import bot.PrivateLogger;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

public class EncounterServiceProvider
{
    private DungeonMasterChecker dmChecker;
    private EncounterHolder      encounterHolder;
    private EncounterLogger      encounterLogger;
    private PrivateLogger        privateLogger;
    private ProcessManager       processManager;

    /**
     * EncounterServiceProvider constructor.
     *
     * @param processManager Process manager
     */
    public EncounterServiceProvider(@NotNull ProcessManager processManager)
    {
        this.dmChecker = new DungeonMasterChecker();
        this.encounterLogger = new EncounterLogger(
            new ActionMessageBuilder(),
            new PhaseChangeMessageBuilder(),
            new SummaryMessageBuilder()
        );
        this.encounterHolder = new EncounterHolder(encounterLogger);
        this.privateLogger = new PrivateLogger(new HelpEncounterMessageBuilder());
        this.processManager = processManager;
    }

    /**
     * Get command factory
     *
     * @return CommandFactoryInterface
     */
    public @NotNull CommandFactoryInterface getCommandFactory()
    {
        return new EncounterCommandFactory(processManager, encounterHolder, encounterLogger, privateLogger, dmChecker);
    }
}
