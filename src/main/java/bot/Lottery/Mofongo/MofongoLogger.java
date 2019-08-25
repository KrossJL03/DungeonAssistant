package bot.Lottery.Mofongo;

import bot.Message;
import bot.MessageInterface;
import bot.TextFormatter;
import bot.XlCodeFormatter;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

class MofongoLogger
{
    private XlCodeFormatter codeFormatter;
    private TextFormatter   textFormatter;

    /**
     * Constructor.
     */
    MofongoLogger()
    {
        this.codeFormatter = new XlCodeFormatter();
        this.textFormatter = new TextFormatter();
    }

    /**
     * Log item rolled by explorer
     *
     * @param channel Channel to log to
     * @param result  Roll result
     */
    void logExplorerItemRolled(@NotNull MessageChannel channel, @NotNull MofongoExplorerRollResult result)
    {
        Message message = new Message();
        message.startCodeBlock(codeFormatter.getStyle());
        message.add(String.format(
            "Rarity Roll d%3d %s %-2d => %s",
            result.getRarityDie(),
            MessageInterface.DOUBLE_ARROW,
            result.getRarityRoll(),
            result.getRarityName()
        ));
        MofongoItem item = result.getItem();
        if (item != null) {
            message.add(String.format(
                "Item   Roll d%3d %s %2d => %d %s",
                result.getItemDie(),
                MessageInterface.DOUBLE_ARROW,
                item.getRoll(),
                item.getQuantity(),
                item.getName()
            ));
            message.endCodeBlock();
            if (item.hasLink()) {
                message.add(textFormatter.makeLinkPreviewless(item.getLink()));
            }
        } else {
            message.endCodeBlock();
        }

        logMessage(channel, message.getAsString());
    }

    /**
     * Log item rolled by pet
     *
     * @param channel Channel to log to
     * @param result  Roll result
     */
    void logPetItemRolled(@NotNull MessageChannel channel, @NotNull MofongoPetRollResult result)
    {
        Message message = new Message();
        message.startCodeBlock(codeFormatter.getStyle());
        for (MofongoItem item : result.getItems()) {
            message.add(String.format(
                "Item Roll d%2d %s %2d => %s",
                result.getItemDie(),
                MessageInterface.DOUBLE_ARROW,
                item.getRoll(),
                item.isNothing() ? item.getName() : item.getQuantity() + " " + item.getName()
            ));
        }

        message.endCodeBlock();
        for (MofongoItem item : result.getItems()) {
            if (item.hasLink()) {
                message.add(textFormatter.makeLinkPreviewless(item.getLink()));
            }
        }

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