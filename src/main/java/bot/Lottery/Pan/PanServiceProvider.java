package bot.Lottery.Pan;

import bot.Command;
import bot.CommandProviderInterface;
import bot.PrivateLogger;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PanServiceProvider implements CommandProviderInterface
{
    private ArrayList<Command> commands;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     */
    public PanServiceProvider(@NotNull ProcessManager processManager)
    {
        BasicRarityRoller   basicRarityRoller   = new BasicRarityRoller();
        ItemImporter        itemImporter        = new ItemImporter(new ItemFactory(), new ItemRegistry());
        PanItemRoller       itemRoller          = new PanItemRoller();
        PanLogger           panLogger           = new PanLogger();
        PrivateLogger       privateLogger       = new PrivateLogger(new HelpMessageBuilder());
        SpecialRarityRoller specialRarityRoller = new SpecialRarityRoller();

        commands = new ArrayList<>();

        commands.add(new BasicDigCommand(processManager, panLogger, itemRoller, basicRarityRoller));
        commands.add(new ImportItemsCommand(processManager, itemImporter));
        commands.add(new SpecialDigCommand(processManager, panLogger, itemRoller, specialRarityRoller));

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
