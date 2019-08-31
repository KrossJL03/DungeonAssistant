package bot.Lottery.Pan;

import org.jetbrains.annotations.NotNull;

class PanRollResult
{
    private String itemLink;
    private String itemName;
    private String rarityName;
    private int    itemDie;
    private int    itemRoll;
    private int    rarityDie;
    private int    rarityRoll;

    /**
     * Constructor.
     *
     * @param item    Item
     * @param rarity  Rarity
     * @param itemDie Item die
     */
    PanRollResult(
        @NotNull PanItem item,
        @NotNull PanRarity rarity,
        int itemDie
    )
    {
        this.itemDie = itemDie;
        this.itemLink = item.getLink();
        this.itemName = item.getName();
        this.itemRoll = item.getRoll();
        this.rarityDie = rarity.getDie();
        this.rarityName = rarity.getName();
        this.rarityRoll = rarity.getRoll();
    }

    /**
     * Get item die
     *
     * @return int
     */
    int getItemDie()
    {
        return itemDie;
    }

    /**
     * Get item link
     *
     * @return String
     */
    @NotNull String getItemLink()
    {
        return itemLink;
    }

    /**
     * Get item name
     *
     * @return String
     */
    @NotNull String getItemName()
    {
        return itemName;
    }

    /**
     * Get item roll
     *
     * @return int
     */
    int getItemRoll()
    {
        return itemRoll;
    }

    /**
     * Get rarity die
     *
     * @return int
     */
    int getRarityDie()
    {
        return rarityDie;
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
     * Get rarity roll
     *
     * @return int
     */
    int getRarityRoll()
    {
        return rarityRoll;
    }
}
