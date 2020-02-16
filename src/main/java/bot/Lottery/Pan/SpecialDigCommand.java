package bot.Lottery.Pan;

import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

class SpecialDigCommand extends DigCommand
{
    /**
     * Constructor.
     */
    SpecialDigCommand(
        @NotNull ProcessManager processManager,
        @NotNull PanLogger logger,
        @NotNull PanItemRoller itemRoller,
        @NotNull SpecialRarityRoller rarityRoller
    )
    {
        super(
            processManager,
            logger,
            itemRoller,
            rarityRoller,
            "roll specialPan",
            "Dig for treasure in Pan's Scavenger Hunt."
        );
    }
}
