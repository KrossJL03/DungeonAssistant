package bot.Item;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Item
{
    public static String FIELD_NAME       = "name";
    public static String FIELD_PAN_RARITY = "panRarity";

    private String name;
    private String panRarity;

    /**
     * Constructor.
     *
     * @param name      Name
     * @param panRarity Rarity for Pan's Scavenger Hunt
     */
    Item(@NotNull String name, @Nullable String panRarity)
    {
        this.name = name;
        this.panRarity = panRarity;
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
     * Get pan rarity
     *
     * @return String
     */
    @Nullable String getPanRarity()
    {
        return panRarity;
    }
}
