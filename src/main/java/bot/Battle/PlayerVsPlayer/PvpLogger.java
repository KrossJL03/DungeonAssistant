package bot.Battle.PlayerVsPlayer;

import bot.Battle.BattleLogger;
import bot.Battle.Logger.Message.Summary.SummaryMessageBuilder;
import bot.Battle.Mention;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

class PvpLogger extends BattleLogger
{
    /**
     * Constructor.
     *
     * @param channel   Channel
     * @param dmMention DM mention
     */
    PvpLogger(@NotNull MessageChannel channel, @NotNull Mention dmMention)
    {
        super(
            new PvpActionMessageBuilder(),
            new PvpPhaseChangeMessageBuilder(),
            new SummaryMessageBuilder(),
            channel,
            dmMention
        );
    }
}
