package bot.Registry.Record;

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
     * Constructor.
     *
     * @param processManager Process manager
     * @param logger         Registry logger
     * @param commandName    HelpCommand name
     * @param parameters     Parameters
     * @param description    HelpCommand description
     * @param isModCommand   Is mod command
     */
    protected RecordCommand(
        @NotNull ProcessManager processManager,
        @NotNull RegistryLogger logger,
        @NotNull String commandName,
        @NotNull ArrayList<CommandParameter> parameters,
        @NotNull String description,
        boolean isModCommand
    )
    {
        super(processManager, commandName, parameters, description, isModCommand);
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
