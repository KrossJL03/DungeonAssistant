package bot.Registry.Review.Command;

import bot.Command;
import bot.CommandParameter;
import bot.Registry.RegistryLogger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

abstract class ReviewCommand extends Command
{
    private RegistryLogger logger;

    /**
     * RecordCommand constructor
     *
     * @param logger      Registry logger
     * @param commandName Command name
     * @param parameters  Parameters
     * @param description Command description
     */
    protected ReviewCommand(
        @NotNull RegistryLogger logger,
        @NotNull String commandName,
        @NotNull ArrayList<CommandParameter> parameters,
        @NotNull String description
    )
    {
        super(commandName, parameters, description);
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
