package bot.Lottery.Pan;

import org.jetbrains.annotations.NotNull;

class PanRollResult
{
    private int    itemDie;
    private String itemName;
    private int    itemRoll;
    private int    rarityDie;
    private String rarityName;
    private int    rarityRoll;

    /**
     * Constructor.
     *
     * @param item   Item
     * @param rarity Rarity
     */
    PanRollResult(@NotNull PanItem item, @NotNull PanRarity rarity)
    {
        this.itemDie = item.getDie();
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
