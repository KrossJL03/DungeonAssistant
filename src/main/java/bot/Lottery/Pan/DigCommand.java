package bot.Lottery.Pan;

import bot.Command;
import bot.ProcessManager;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

abstract class DigCommand extends Command
{
    private PanItemRoller itemRoller;
    private PanLogger     logger;
    private RarityRoller  rarityRoller;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param logger         Logger
     * @param itemRoller     Item roller
     * @param rarityRoller   Rarity roller
     * @param name           Command name
     * @param description    Command description
     */
    DigCommand(
        @NotNull ProcessManager processManager,
        @NotNull PanLogger logger,
        @NotNull PanItemRoller itemRoller,
        @NotNull RarityRoller rarityRoller,
        @NotNull String name,
        @NotNull String description
    )
    {
        super(
            processManager,
            name,
            new ArrayList<>(),
            description
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
