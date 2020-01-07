package bot.Item;

import net.dv8tion.jda.core.entities.Message.Attachment;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

class ItemImporter
{
    private ItemFactory  itemFactory;
    private ItemRegistry itemRegistry;

    /**
     * Constructor.
     *
     * @param itemFactory  Item factory
     * @param itemRegistry Item registry
     */
    ItemImporter(
        @NotNull ItemFactory itemFactory,
        @NotNull ItemRegistry itemRegistry
    )
    {
        this.itemFactory = itemFactory;
        this.itemRegistry = itemRegistry;
    }

    /**
     * Import items from event
     *
     * @param event Event to import items from
     *
     * @return Number of items imported
     */
    int importItems(MessageReceivedEvent event)
    {
        itemRegistry.reset();

        int count = 0;
        for (Attachment attachment : event.getMessage().getAttachments()) {
            count += processAttachment(attachment);
        }

        return count;
    }

    /**
     * Process individual attachment
     *
     * @param attachment Attachment to process
     *
     * @return Number of items saved
     */
    private int processAttachment(Attachment attachment)
    {
        AttachmentReader reader = new AttachmentReader(attachment);
        int              count  = 0;
        for (Map<String, String> record : reader.read()) {
            processRecord(record);
            count++;
        }

        return count;
    }

    /**
     * Process a single record
     *
     * @param record CSV record
     */
    private void processRecord(Map<String, String> record)
    {
        Item item = itemFactory.createFromMap(record);
        itemRegistry.insertItem(item);
    }
}