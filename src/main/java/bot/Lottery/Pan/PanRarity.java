package bot.Lottery.Pan;

import org.jetbrains.annotations.NotNull;

public class PanRarity
{
    final static String RARITY_NAME_COMMON     = "Common";
    final static String RARITY_NAME_UNCOMMON   = "Uncommon";
    final static String RARITY_NAME_RARE       = "Rare";
    final static String RARITY_NAME_ULTRA_RARE = "UltraRare";
    final static String RARITY_NAME_SPECIAL    = "Special";

    private String name;
    private int    die;
    private int    roll;

    /**
     * Constructor.
     *
     * @param name Rarity name
     * @param die  Die rolled to get rarity roll
     * @param roll Rarity rolled
     */
    PanRarity(@NotNull String name, int die, int roll)
    {
        this.die = die;
        this.name = name;
        this.roll = roll;
    }

    /**
     * Get die
     *
     * @return int
     */
    public int getDie()
    {
        return die;
    }

    /**
     * Get name
     *
     * @return String
     */
    public @NotNull String getName()
    {
        return name;
    }

    /**
     * Get roll
     *
     * @return int
     */
    public int getRoll()
    {
        return roll;
    }
}
