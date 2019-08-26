package bot.Lottery.Mofongo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class MofongoExplorerRollResult
{
    private MofongoItem item;
    private String      rarityName;
    private int         itemDie;
    private int         rarityDie;
    private int         rarityRoll;

    /**
     * Constructor.
     *
     * @param item       Item
     * @param rarityName Rarity name
     * @param itemDie    Item die
     * @param rarityDie  Rarity die
     * @param rarityRoll Rarity roll
     */
    MofongoExplorerRollResult(
        @Nullable MofongoItem item,
        @Nullable String rarityName,
        int itemDie,
        int rarityDie,
        int rarityRoll
    )
    {
        this.item = item;
        this.itemDie = itemDie;
        this.rarityDie = rarityDie;
        this.rarityName = rarityName != null ? rarityName : "Null";
        this.rarityRoll = rarityRoll;
    }

    /**
     * Get item
     *
     * @return MofongoItem
     */
    @Nullable MofongoItem getItem()
    {
        return item;
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
