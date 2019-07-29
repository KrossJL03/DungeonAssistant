package bot.Registry.Record.Command;

import bot.CommandFactoryInterface;
import bot.CommandInterface;
import bot.ProcessManager;
import bot.Registry.RegistryLogger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecordCommandFactory implements CommandFactoryInterface
{
    private RegistryLogger logger;
    private ProcessManager processManager;

    /**
     * RecordCommandFactory constructor.
     *
     * @param processManager Process manager
     * @param logger         Registry logger
     */
    public RecordCommandFactory(
        @NotNull ProcessManager processManager,
        @NotNull RegistryLogger logger
    )
    {
        this.logger = logger;
        this.processManager = processManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createCommands()
    {
        ArrayList<CommandInterface> commands = new ArrayList<>();

        commands.add(new CreateCharacterCommand(processManager, logger));
        commands.add(new DeleteCharacterCommand(processManager, logger));
        commands.add(new HelpCommand(processManager, logger));
        commands.add(new RegisterPlayerCommand(processManager, logger));

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
