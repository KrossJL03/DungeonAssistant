package bot.Encounter;

import bot.CommandFactoryInterface;
import bot.Encounter.Command.EncounterCommandFactory;
import bot.Encounter.DungeonMasterChecker.DungeonMasterChecker;
import bot.Encounter.Logger.EncounterLogger;
import bot.Encounter.Logger.Message.Action.ActionMessageBuilder;
import bot.Encounter.Logger.Message.Help.HelpEncounterMessageBuilder;
import bot.Encounter.Logger.Message.PhaseChange.PhaseChangeMessageBuilder;
import bot.Encounter.Logger.Message.Summary.SummaryMessageBuilder;
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
