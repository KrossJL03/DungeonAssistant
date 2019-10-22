package bot.Item;

import bot.CustomException;
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
    @NotNull ItemAbstract createFromMap(@NotNull String itemType, @NotNull Map<String, String> map)
    {
        switch (itemType.toUpperCase()) {
            case Clothing.ITEM_TYPE:
                return createClothingFromMap(map);
            case Consumable.ITEM_TYPE:
                return createConsumableFromMap(map);
            case Equipment.ITEM_TYPE:
                return createEquipmentFromMap(map);
            case Material.ITEM_TYPE:
                return createMaterialFromMap(map);
            default:
                throw new CustomException(String.format("%s is not a valid item type", itemType));
        }
    }

    /**
     * Create clothing item from map
     *
     * @return Clothing
     */
    private @NotNull Clothing createClothingFromMap(@NotNull Map<String, String> map)
    {
        return new Clothing(
            map.get(ItemAbstract.FIELD_NAME),
            map.get(ItemAbstract.FIELD_IMAGE_URL),
            Integer.parseInt(map.get(ItemAbstract.FIELD_BUY_VALUE)),
            Integer.parseInt(map.get(ItemAbstract.FIELD_SELL_VALUE)),
            Boolean.parseBoolean(map.get(ItemAbstract.FIELD_IS_BUYABLE)),
            Boolean.parseBoolean(map.get(ItemAbstract.FIELD_IS_CRAFTABLE)),
            Boolean.parseBoolean(map.get(ItemAbstract.FIELD_IS_EVENT_EXCLUSIVE)),
            Boolean.parseBoolean(map.get(ItemAbstract.FIELD_IS_SEASONAL))
        );
    }

    /**
     * Create consumable item from map
     *
     * @return Consumable
     */
    private @NotNull Consumable createConsumableFromMap(@NotNull Map<String, String> map)
    {
        return new Consumable(
            map.get(ItemAbstract.FIELD_NAME),
            Consumable.ConsumableType.valueOf(map.get(ItemAbstract.FIELD_ITEM_SUBTYPE)),
            map.get(ItemAbstract.FIELD_IMAGE_URL),
            Integer.parseInt(map.get(ItemAbstract.FIELD_BUY_VALUE)),
            Integer.parseInt(map.get(ItemAbstract.FIELD_SELL_VALUE)),
            Boolean.parseBoolean(map.get(ItemAbstract.FIELD_IS_BUYABLE)),
            Boolean.parseBoolean(map.get(ItemAbstract.FIELD_IS_CRAFTABLE)),
            Boolean.parseBoolean(map.get(ItemAbstract.FIELD_IS_EVENT_EXCLUSIVE)),
            Boolean.parseBoolean(map.get(ItemAbstract.FIELD_IS_SEASONAL))
        );
    }

    /**
     * Create equipment item from map
     *
     * @return Equipment
     */
    private @NotNull Equipment createEquipmentFromMap(@NotNull Map<String, String> map)
    {
        return new Equipment(
            map.get(ItemAbstract.FIELD_NAME),
            Equipment.EquipmentType.valueOf(map.get(ItemAbstract.FIELD_ITEM_SUBTYPE)),
            map.get(ItemAbstract.FIELD_IMAGE_URL),
            Integer.parseInt(map.get(ItemAbstract.FIELD_BUY_VALUE)),
            Integer.parseInt(map.get(ItemAbstract.FIELD_SELL_VALUE)),
            Boolean.parseBoolean(map.get(ItemAbstract.FIELD_IS_BUYABLE)),
            Boolean.parseBoolean(map.get(ItemAbstract.FIELD_IS_CRAFTABLE)),
            Boolean.parseBoolean(map.get(ItemAbstract.FIELD_IS_EVENT_EXCLUSIVE)),
            Boolean.parseBoolean(map.get(ItemAbstract.FIELD_IS_SEASONAL)),
            map.get(Equipment.FIELD_ARMOR_SET_NAME),
            Integer.parseInt(map.get(Equipment.FIELD_STAT_VALUE_HP)),
            Integer.parseInt(map.get(Equipment.FIELD_STAT_VALUE_STR)),
            Integer.parseInt(map.get(Equipment.FIELD_STAT_VALUE_WIS)),
            Integer.parseInt(map.get(Equipment.FIELD_STAT_VALUE_AGI)),
            Integer.parseInt(map.get(Equipment.FIELD_STAT_VALUE_DEF))
        );
    }

    /**
     * Create materials item from map
     *
     * @return Material
     */
    private @NotNull Material createMaterialFromMap(@NotNull Map<String, String> map)
    {
        return new Material(
            map.get(ItemAbstract.FIELD_NAME),
            map.get(ItemAbstract.FIELD_IMAGE_URL),
            Integer.parseInt(map.get(ItemAbstract.FIELD_BUY_VALUE)),
            Integer.parseInt(map.get(ItemAbstract.FIELD_SELL_VALUE)),
            Boolean.parseBoolean(map.get(ItemAbstract.FIELD_IS_BUYABLE)),
            Boolean.parseBoolean(map.get(ItemAbstract.FIELD_IS_CRAFTABLE)),
            Boolean.parseBoolean(map.get(ItemAbstract.FIELD_IS_EVENT_EXCLUSIVE)),
            Boolean.parseBoolean(map.get(ItemAbstract.FIELD_IS_SEASONAL))
        );
    }
}
