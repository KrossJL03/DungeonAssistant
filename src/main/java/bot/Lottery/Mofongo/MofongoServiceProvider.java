package bot.Lottery.Mofongo;

import bot.Command;
import bot.CommandProviderInterface;
import bot.PrivateLogger;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MofongoServiceProvider implements CommandProviderInterface
{
    private ArrayList<Command> commands;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     */
    public MofongoServiceProvider(@NotNull ProcessManager processManager)
    {
        MofongoLogger mofongoLogger = new MofongoLogger();
        PrivateLogger privateLogger = new PrivateLogger(new HelpMessageBuilder());

        commands = new ArrayList<>();

        commands.add(new MofongoRollCommand(processManager, mofongoLogger));
        commands.add(new MofongoViewPetsCommand(processManager, mofongoLogger));

        commands.add(new HelpCommand(processManager, privateLogger, new ArrayList<>(commands)));
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
