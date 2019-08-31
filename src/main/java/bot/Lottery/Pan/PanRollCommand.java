package bot.Lottery.Pan;

import bot.Command;
import bot.ProcessManager;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PanRollCommand extends Command
{
    private PanItemRoller   itemRoller;
    private PanLogger       logger;
    private PanRarityRoller rarityRoller;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param logger         Logger
     * @param itemRoller     Item roller
     * @param rarityRoller   Rarity roller
     */
    PanRollCommand(
        @NotNull ProcessManager processManager,
        @NotNull PanLogger logger,
        @NotNull PanItemRoller itemRoller,
        @NotNull PanRarityRoller rarityRoller
    )
    {
        super(
            processManager,
            "roll pan",
            new ArrayList<>(),
            "Roll pan."
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
        PanRarity      rarity  = rarityRoller.roll();
        PanRollResult  result  = itemRoller.roll(rarity);

        logger.logItemRolled(channel, result);
    }
}
