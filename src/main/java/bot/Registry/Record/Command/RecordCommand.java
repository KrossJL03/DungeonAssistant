package bot.Registry.Record.Command;

import bot.Command;
import bot.CommandParameter;
import bot.MyProperties;
import bot.ProcessManager;
import bot.Registry.RegistryLogger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

abstract class RecordCommand extends Command
{
    private RegistryLogger logger;

    /**
     * RecordCommand constructor
     *
     * @param processManager Process manager
     * @param logger         Registry logger
     * @param commandName    HelpCommand name
     * @param parameters     Parameters
     * @param description    HelpCommand description
     */
    protected RecordCommand(
        @NotNull ProcessManager processManager,
        @NotNull RegistryLogger logger,
        @NotNull String commandName,
        @NotNull ArrayList<CommandParameter> parameters,
        @NotNull String description
    )
    {
        super(processManager, commandName, parameters, description);
        this.logger = logger;
    }

    /**
     * Ensure recording is not locked
     */
    final protected void ensureRecordingNotLocked()
    {
        if (!MyProperties.BOOL_RECORDING_ENABLED | isDatabaseLocked()) {
            throw RecordCommandException.createCommandLocked();
        }
    }

    /**
     * Get logger
     *
     * @return RegistryLogger
     */
    final protected @NotNull RegistryLogger getLogger()
    {
        return logger;
    }
}
