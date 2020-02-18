package bot;

import bot.Battle.BattleServiceProvider;
import bot.Lottery.Mofongo.MofongoServiceProvider;
import bot.Lottery.Pan.PanServiceProvider;
import bot.Registry.Record.RecordServiceProvider;
import bot.Registry.Review.ReviewServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommandProvider implements CommandProviderInterface
{
    private ArrayList<Command> commands;

    /**
     * Constructor.
     */
    public CommandProvider()
    {
        ProcessManager processManager = new ProcessManager();

        BattleServiceProvider  battleServiceProvider  = new BattleServiceProvider(processManager);
        MofongoServiceProvider mofongoServiceProvider = new MofongoServiceProvider(processManager);
        PanServiceProvider     panServiceProvider     = new PanServiceProvider(processManager);
        RecordServiceProvider  recordServiceProvider  = new RecordServiceProvider(processManager);
        ReviewServiceProvider  reviewServiceProvider  = new ReviewServiceProvider(processManager);

        commands = new ArrayList<>();

        commands.addAll(battleServiceProvider.getCommands());
        commands.addAll(recordServiceProvider.getCommands());
        commands.addAll(reviewServiceProvider.getCommands());
        commands.addAll(mofongoServiceProvider.getCommands());
        commands.addAll(panServiceProvider.getCommands());

        commands.add(new HelpCommand(processManager, getHelpCommands()));
    }

    /**
     * Create instances for commands
     *
     * @return ArrayList
     */
    public @NotNull ArrayList<Command> getCommands()
    {
        return new ArrayList<>(commands);
    }

    /**
     * Get help commands
     *
     * @return ArrayList
     */
    private @NotNull ArrayList<Command> getHelpCommands()
    {
        ArrayList<Command> helpCommands = new ArrayList<>();
        for (Command command : commands) {
            if (command instanceof HelpCommandInterface) {
                helpCommands.add(command);
            }
        }

        return helpCommands;
    }
}
