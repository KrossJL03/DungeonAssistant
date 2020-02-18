package bot.Registry.Review;

import bot.Command;
import bot.CommandParameter;
import bot.ProcessManager;
import bot.Registry.RegistryLogger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

abstract class ReviewCommand extends Command
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
    ReviewCommand(
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
     * Get logger
     *
     * @return RegistryLogger
     */
    final protected @NotNull RegistryLogger getLogger()
    {
        return logger;
    }
}
