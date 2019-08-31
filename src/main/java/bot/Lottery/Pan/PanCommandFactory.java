package bot.Lottery.Pan;

import bot.CommandFactoryInterface;
import bot.CommandInterface;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PanCommandFactory implements CommandFactoryInterface
{
    private PanItemRoller   itemRoller;
    private PanLogger       panLogger;
    private PanRarityRoller rarityRoller;
    private ProcessManager  processManager;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param panLogger      Logger
     * @param itemRoller     Item roller
     * @param rarityRoller   Rarity roller
     */
    PanCommandFactory(
        @NotNull ProcessManager processManager,
        @NotNull PanLogger panLogger,
        @NotNull PanItemRoller itemRoller,
        @NotNull PanRarityRoller rarityRoller
    )
    {
        this.itemRoller = itemRoller;
        this.panLogger = panLogger;
        this.processManager = processManager;
        this.rarityRoller = rarityRoller;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createCommands()
    {
        return new ArrayList<CommandInterface>()
        {
            {
                add(new PanRollCommand(processManager, panLogger, itemRoller, rarityRoller));
                add(new PanRollSpecialCommand(processManager, panLogger, itemRoller, rarityRoller));
            }
        };
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
