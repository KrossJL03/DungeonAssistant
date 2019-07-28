package bot.Registry.Record.Command;

import bot.CommandFactoryInterface;
import bot.CommandInterface;
import bot.Registry.RegistryLogger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecordCommandFactory implements CommandFactoryInterface
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createCommands()
    {
        ArrayList<CommandInterface> commands = new ArrayList<>();
        RegistryLogger              logger   = new RegistryLogger();

        commands.add(new CreateCharacterCommand(logger));
        commands.add(new DeleteCharacterCommand(logger));
        commands.add(new HelpCommand(logger));
        commands.add(new RegisterPlayerCommand(logger));

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
