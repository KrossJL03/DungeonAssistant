package bot.Battle;

import bot.Battle.Command.EncounterCommandFactory;
import bot.CommandFactoryInterface;
import bot.PrivateLogger;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

public class EncounterServiceProvider
{
    private DungeonMasterChecker dmChecker;
    private EncounterHolder      encounterHolder;
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
        this.encounterHolder = new EncounterHolder();
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
        return new EncounterCommandFactory(processManager, encounterHolder, privateLogger, dmChecker);
    }
}
