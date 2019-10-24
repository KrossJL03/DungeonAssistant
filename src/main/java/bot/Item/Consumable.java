package bot.Item;

import org.jetbrains.annotations.NotNull;

class Consumable extends ItemAbstract
{
    final static String ITEM_TYPE = "CONSUMABLE";

    /**
     * Type of consumable
     */
    enum ConsumableType
    {
        POTION, COMBAT, SUB_MODIFIER, TICKET, HEALING, PET_DYE, CUSTOMIZATION
    }

    /**
     * Constructor.
     *
     * @param name             Name
     * @param subtype          Type of consumable
     * @param imageUrl         Image url
     * @param buyValue         Buy value
     * @param sellValue        Sell value
     * @param isBuyable        Is the item buyable
     * @param isCraftable      Is the item craftable
     * @param isEventExclusive Is the item event exclusive
     * @param isSeasonal       Is the item seasonal
     */
    Consumable(
        @NotNull String name,
        @NotNull ConsumableType subtype,
        @NotNull String imageUrl,
        int buyValue,
        int sellValue,
        boolean isBuyable,
        boolean isCraftable,
        boolean isEventExclusive,
        boolean isSeasonal
    )
    {
        super(
            name,
            ITEM_TYPE,
            subtype.toString(),
            imageUrl,
            buyValue,
            sellValue,
            isBuyable,
            isCraftable,
            isEventExclusive,
            isSeasonal
        );
    }
}
