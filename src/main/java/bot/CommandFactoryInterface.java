package bot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface CommandFactoryInterface
{
    /**
     * Create instances for commands
     *
     * @return ArrayList
     */
    @NotNull ArrayList<CommandInterface> createCommands();

    /**
     * Get commands for interfacing with other bots
     *
     * @return ArrayList
     */
    @NotNull ArrayList<CommandInterface> createAdditionalCommands();
}
