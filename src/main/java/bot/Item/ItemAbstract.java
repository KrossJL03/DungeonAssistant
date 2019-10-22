package bot.Item;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class ItemAbstract
{
    static String FIELD_BUY_VALUE          = "buyValue";
    static String FIELD_IMAGE_URL          = "imageUrl";
    static String FIELD_IS_BUYABLE         = "isBuyable";
    static String FIELD_IS_CRAFTABLE       = "isCraftable";
    static String FIELD_IS_EVENT_EXCLUSIVE = "isEventExclusive";
    static String FIELD_IS_SEASONAL        = "isSeasonal";
    static String FIELD_ITEM_SUBTYPE       = "itemSubtype";
    static String FIELD_ITEM_TYPE          = "itemType";
    static String FIELD_NAME               = "name";
    static String FIELD_SELL_VALUE         = "sellValue";

    private int     buyValue;
    private String  imageUrl;
    private boolean isBuyable;
    private boolean isCraftable;
    private boolean isEventExclusive;
    private boolean isSeasonal;
    private String  name;
    private int     sellValue;
    private String  subtype;
    private String  type;

    /**
     * Constructor.
     *
     * @param name             Name
     * @param type             Item type
     * @param subtype          Item subtype
     * @param imageUrl         Image url
     * @param buyValue         Buy value
     * @param sellValue        Sell value
     * @param isBuyable        Is the item buyable
     * @param isCraftable      Is the item craftable
     * @param isEventExclusive Is the item event exclusive
     * @param isSeasonal       Is the item seasonal
     */
    ItemAbstract(
        @NotNull String name,
        @NotNull String type,
        @Nullable String subtype,
        @NotNull String imageUrl,
        int buyValue,
        int sellValue,
        boolean isBuyable,
        boolean isCraftable,
        boolean isEventExclusive,
        boolean isSeasonal
    )
    {
        this.buyValue = buyValue;
        this.imageUrl = imageUrl;
        this.isBuyable = isBuyable;
        this.isCraftable = isCraftable;
        this.isEventExclusive = isEventExclusive;
        this.isSeasonal = isSeasonal;
        this.name = name;
        this.sellValue = sellValue;
        this.subtype = subtype;
        this.type = type;
    }

    /**
     * Get buy value
     *
     * @return int
     */
    int getBuyValue()
    {
        return buyValue;
    }

    /**
     * Get image url
     *
     * @return String
     */
    String getImageUrl()
    {
        return imageUrl;
    }

    /**
     * Get name
     *
     * @return String
     */
    @NotNull String getName()
    {
        return name;
    }

    /**
     * Get sell value
     *
     * @return int
     */
    int getSellValue()
    {
        return sellValue;
    }

    /**
     * Get subtype
     *
     * @return String
     */
    @Nullable String getSubtype() { return subtype; }

    /**
     * Get type
     *
     * @return String
     */
    @NotNull String getType() { return type; }

    /**
     * Is buyable
     *
     * @return boolean
     */
    boolean isBuyable()
    {
        return isBuyable;
    }

    /**
     * Is craftable
     *
     * @return boolean
     */
    boolean isCraftable()
    {
        return isCraftable;
    }

    /**
     * Is event exclusive
     *
     * @return boolean
     */
    boolean isEventExclusive()
    {
        return isEventExclusive;
    }

    /**
     * Is seasonal
     *
     * @return boolean
     */
    boolean isSeasonal()
    {
        return isSeasonal;
    }
}
