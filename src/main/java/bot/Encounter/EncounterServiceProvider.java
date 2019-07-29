package bot.Encounter;

import bot.CommandFactoryInterface;
import bot.Encounter.Command.EncounterCommandFactory;
import bot.Encounter.Logger.EncounterLogger;
import bot.Encounter.Logger.Message.Action.ActionMessageBuilder;
import bot.Encounter.Logger.Message.PhaseChange.PhaseChangeMessageBuilder;
import bot.Encounter.Logger.Message.Summary.SummaryMessageBuilder;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

public class EncounterServiceProvider
{
    private EncounterHolder holder;
    private EncounterLogger logger;
    private ProcessManager  processManager;

    /**
     * EncounterServiceProvider constructor.
     *
     * @param processManager Process manager
     */
    public EncounterServiceProvider(@NotNull ProcessManager processManager)
    {
        this.logger = new EncounterLogger(
            new ActionMessageBuilder(),
            new PhaseChangeMessageBuilder(),
            new SummaryMessageBuilder()
        );
        this.holder = new EncounterHolder(logger);
        this.processManager = processManager;
    }

    /**
     * Get command factory
     *
     * @return CommandFactoryInterface
     */
    public @NotNull CommandFactoryInterface getCommandFactory()
    {
        return new EncounterCommandFactory(processManager, holder, logger);
    }
}
