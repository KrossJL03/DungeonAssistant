package bot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface CommandProviderInterface
{
    /**
     * Create instances for commands
     *
     * @return ArrayList
     */
    @NotNull ArrayList<Command> getCommands();
}
