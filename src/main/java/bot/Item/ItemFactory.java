package bot.Item;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

class ItemFactory
{
    /**
     * Create item from hash map
     *
     * @param map Map containing item information
     *
     * @return Item
     */
    @NotNull Item createFromMap(@NotNull Map<String, String> map)
    {
        return new Item(
            map.get(Item.FIELD_NAME),
            map.get(Item.FIELD_PAN_RARITY)
        );
    }
}
