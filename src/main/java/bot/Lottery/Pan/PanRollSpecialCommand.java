package bot.Lottery.Pan;

import bot.Command;
import bot.ProcessManager;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PanRollSpecialCommand extends Command
{
    private PanItemRoller   itemRoller;
    private PanLogger       logger;
    private PanRarityRoller rarityRoller;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param logger         Logger
     * @param rarityRoller   Rarity roller
     */
    PanRollSpecialCommand(
        @NotNull ProcessManager processManager,
        @NotNull PanLogger logger,
        @NotNull PanItemRoller itemRoller,
        @NotNull PanRarityRoller rarityRoller
    )
    {
        super(
            processManager,
            "roll specialPan",
            new ArrayList<>(),
            "Roll special pan."
        );
        this.itemRoller = itemRoller;
        this.logger = logger;
        this.rarityRoller = rarityRoller;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event)
    {
        MessageChannel channel = event.getChannel();
        PanRarity      rarity  = rarityRoller.rollSpecial();
        PanRollResult  result  = itemRoller.roll(rarity);

        logger.logItemRolled(channel, result);
    }
}
