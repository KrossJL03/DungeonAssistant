package bot.Lottery.Pan;

import bot.Message;
import bot.MessageInterface;
import bot.PyCodeFormatter;
import bot.TextFormatter;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

class PanLogger
{
    private PyCodeFormatter codeFormatter;
    private TextFormatter   textFormatter;

    /**
     * Constructor.
     */
    PanLogger()
    {
        this.codeFormatter = new PyCodeFormatter();
        this.textFormatter = new TextFormatter();
    }

    /**
     * Log roll
     *
     * @param channel Channel to log to
     * @param result  Roll result
     */
    void logItemRolled(@NotNull MessageChannel channel, @NotNull PanRollResult result)
    {
        Message message = new Message();
        message.startCodeBlock(codeFormatter.getStyle());
        message.add(String.format(
            "Rarity Roll d%3d %s %3d => %s",
            result.getRarityDie(),
            MessageInterface.DOUBLE_ARROW,
            result.getRarityRoll(),
            result.getRarityName()
        ));
        message.add(String.format(
            "Item   Roll d%-3d %s %3d => %s",
            result.getItemDie(),
            MessageInterface.DOUBLE_ARROW,
            result.getItemRoll(),
            result.getItemName()
        ));
        message.endCodeBlock();
//        message.add(textFormatter.makeLinkPreviewless(result.getItemLink()));


        logMessage(channel, message.getAsString());
    }

    /**
     * Log message
     *
     * @param channel Channel to log message to
     * @param message Message to log
     */
    private void logMessage(MessageChannel channel, String message)
    {
        channel.sendMessage(message).queue();
    }
}
