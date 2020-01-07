package bot.Item;

import bot.Command;
import bot.CommandParameter;
import bot.ProcessManager;
import net.dv8tion.jda.core.entities.Message.Attachment;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ImportItemsCommand extends Command
{
    private ItemImporter itemImporter;

    /**
     * Constructor
     *
     * @param processManager Process manager
     * @param itemImporter   Item importer
     */
    ImportItemsCommand(@NotNull ProcessManager processManager, @NotNull ItemImporter itemImporter)
    {
        super(
            processManager,
            "import items",
            new ArrayList<CommandParameter>() {},
            "Import items using a CSV."
        );
        this.itemImporter = itemImporter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event) throws ItemException
    {
        verifyMod(event.getAuthor());
        verifyAttachmentCount(event);

        MessageChannel channel = event.getChannel();
        int            count   = itemImporter.importItems(event);

        channel.sendMessage(String.format("%d item records have been saved!", count)).queue();
    }

    /**
     * Get CSV attachment from message
     *
     * @param event Message
     *
     * @throws ItemException If no attachment is provided
     *                       If too many attachments are provided
     */
    private void verifyAttachmentCount(MessageReceivedEvent event) throws ItemException
    {
        List<Attachment> attachments = event.getMessage().getAttachments();
        if (attachments.size() == 0) {
            throw ItemException.createNoAttachment(getCommandName());
        } else if (attachments.size() > 1) {
            throw ItemException.createMultipleAttachments(getCommandName());
        }
    }
}
