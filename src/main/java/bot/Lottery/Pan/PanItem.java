package bot.Lottery.Pan;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class PanItem
{
    private String link;
    private String name;
    private String rarityName;
    private int    roll;

    /**
     * Constructor.
     *
     * @param name       Name of item
     * @param rarityName Item rarity
     * @param roll       Roll for item within rarity
     * @param link       A link for the item
     */
    PanItem(@NotNull String name, @NotNull String rarityName, int roll, @Nullable String link)
    {
        this.link = link;
        this.name = name;
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
}
