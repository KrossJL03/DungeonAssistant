package bot.Registry.Record.Command;

import bot.CommandFactoryInterface;
import bot.CommandInterface;
import bot.PrivateLogger;
import bot.ProcessManager;
import bot.Registry.RegistryLogger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecordCommandFactory implements CommandFactoryInterface
{
    private PrivateLogger  privateLogger;
    private ProcessManager processManager;
    private RegistryLogger registryLogger;

    /**
     * RecordCommandFactory constructor.
     *
     * @param processManager Process manager
     * @param registryLogger Registry logger
     * @param privateLogger  Private logger
     */
    public RecordCommandFactory(
        @NotNull ProcessManager processManager,
        @NotNull RegistryLogger registryLogger,
        @NotNull PrivateLogger privateLogger
    )
    {
        this.privateLogger = privateLogger;
        this.processManager = processManager;
        this.registryLogger = registryLogger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createCommands()
    {
        ArrayList<CommandInterface> commands = new ArrayList<>();

        commands.add(new CreateExplorerCommand(processManager, registryLogger));
        commands.add(new DeleteCharacterCommand(processManager, registryLogger));
        commands.add(new RegisterPlayerCommand(processManager, registryLogger));

        commands.add(new HelpRecordCommand(processManager, privateLogger, new ArrayList<>(commands)));

        return commands;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createAdditionalCommands()
    {
        return new ArrayList<>();
    }
}
