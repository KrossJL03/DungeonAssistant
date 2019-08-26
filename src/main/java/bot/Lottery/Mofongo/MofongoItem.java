package bot.Lottery.Mofongo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class MofongoItem
{
    final static String RARITY_ITEM_COMMON     = "Common";
    final static String RARITY_ITEM_UNCOMMON   = "Uncommon";
    final static String RARITY_ITEM_RARE       = "Rare";
    final static String RARITY_ITEM_ULTRA_RARE = "UltraRare";

    private String link;
    private String name;
    private String rarityName;
    private int    roll;
    private int    quantity;

    /**
     * Constructor.
     *
     * @param name       Name of item
     * @param rarityName Item rarity
     * @param roll       Roll for item within rarity
     * @param quantity   Quantity
     * @param link       A link for the item
     */
    MofongoItem(@NotNull String name, @NotNull String rarityName, int roll, int quantity, @Nullable String link)
    {
        this.link = link;
        this.name = name;
        this.quantity = quantity;
        this.rarityName = rarityName;
        this.roll = roll;
    }

    /**
     * Get link
     *
     * @return String
     */
    @Nullable String getLink()
    {
        return link;
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
     * Get quantity
     *
     * @return int
     */
    int getQuantity()
    {
        return quantity;
    }

    /**
     * Get rarity name
     *
     * @return String
     */
    @NotNull String getRarityName()
    {
        return rarityName;
    }

    /**
     * Get roll
     *
     * @return int
     */
    int getRoll()
    {
        return roll;
    }

    /**
     * Has link
     *
     * @return boolean
     */
    boolean hasLink()
    {
        return link != null;
    }

    /**
     * Is nothing
     *
     * @return boolean
     */
    boolean isNothing()
    {
        return name.equals("Nothing");
    }
}
