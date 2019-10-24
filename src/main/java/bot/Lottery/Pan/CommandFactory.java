package bot.Lottery.Pan;

import bot.CommandFactoryInterface;
import bot.CommandInterface;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommandFactory implements CommandFactoryInterface
{
    private BasicRarityRoller   basicRarityRoller;
    private PanItemRoller       itemRoller;
    private PanLogger           panLogger;
    private ProcessManager      processManager;
    private SpecialRarityRoller specialRarityRoller;

    /**
     * Constructor.
     *
     * @param processManager      Process manager
     * @param panLogger           Logger
     * @param itemRoller          Item roller
     * @param rarityRoller        Basic rarity roller
     * @param specialRarityRoller Special rarity roller
     */
    CommandFactory(
        @NotNull ProcessManager processManager,
        @NotNull PanLogger panLogger,
        @NotNull PanItemRoller itemRoller,
        @NotNull BasicRarityRoller rarityRoller,
        @NotNull SpecialRarityRoller specialRarityRoller
    )
    {
        this.basicRarityRoller = rarityRoller;
        this.itemRoller = itemRoller;
        this.panLogger = panLogger;
        this.processManager = processManager;
        this.specialRarityRoller = specialRarityRoller;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandInterface> createAdditionalCommands()
    {
        return new ArrayList<>();
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
                add(new BasicDigCommand(processManager, panLogger, itemRoller, basicRarityRoller));
                add(new SpecialDigCommand(processManager, panLogger, itemRoller, specialRarityRoller));
            }
        };
    }
}
