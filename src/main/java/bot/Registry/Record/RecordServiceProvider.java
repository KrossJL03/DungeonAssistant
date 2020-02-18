package bot.Registry.Record;

import bot.Command;
import bot.CommandProviderInterface;
import bot.PrivateLogger;
import bot.ProcessManager;
import bot.Registry.RegistryLogger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecordServiceProvider implements CommandProviderInterface
{
    private ArrayList<Command> commands;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     */
    public RecordServiceProvider(@NotNull ProcessManager processManager)
    {
        RegistryLogger registryLogger = new RegistryLogger();
        PrivateLogger  privateLogger  = new PrivateLogger(new HelpMessageBuilder());

        commands = new ArrayList<>();

        commands.add(new CreateExplorerCommand(processManager, registryLogger));
        commands.add(new DeleteCharacterCommand(processManager, registryLogger));
        commands.add(new RegisterPlayerCommand(processManager, registryLogger));

        commands.add(new HelpRecordCommand(processManager, privateLogger, new ArrayList<>(commands)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<Command> getCommands()
    {
        return new ArrayList<>(commands);
    }
}
