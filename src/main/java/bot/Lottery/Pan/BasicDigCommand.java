package bot.Lottery.Pan;

import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

class BasicDigCommand extends DigCommand
{
    /**
     * Constructor.
     */
    BasicDigCommand(
        @NotNull ProcessManager processManager,
        @NotNull PanLogger logger,
        @NotNull PanItemRoller itemRoller,
        @NotNull BasicRarityRoller rarityRoller
    )
    {
        super(
            processManager,
            logger,
            itemRoller,
            rarityRoller,
            "roll pan",
            "Dig for treasure in Pan's Scavenger Hunt."
        );
    }
}
