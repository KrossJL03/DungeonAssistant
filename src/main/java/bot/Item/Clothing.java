package bot.Item;

import org.jetbrains.annotations.NotNull;

class Clothing extends ItemAbstract
{
    final static String ITEM_TYPE = "CLOTHING";

    /**
     * Type of clothing
     */
    enum ClothingType
    {
        HAT, EYEWEAR, NECKWEAR, GLOVES, TOP, BOTTOM, BACK
    }

    /**
     * Constructor.
     *
     * @param name             Name
     * @param imageUrl         Image url
     * @param buyValue         Buy value
     * @param sellValue        Sell value
     * @param isBuyable        Is the item buyable
     * @param isCraftable      Is the item craftable
     * @param isEventExclusive Is the item event exclusive
     * @param isSeasonal       Is the item seasonal
     */
    Clothing(
        @NotNull String name,
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
            null,
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
