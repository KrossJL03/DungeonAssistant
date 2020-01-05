package bot.Lottery.Pan;

import org.jetbrains.annotations.NotNull;

class PanItem
{
    private int    die;
    private String name;
    private int    roll;

    /**
     * Constructor.
     *
     * @param name Name of item
     * @param die  Die rolled to get item roll
     * @param roll Roll for item within rarity
     */
    PanItem(@NotNull String name, int die, int roll)
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
    int getDie()
    {
        return die;
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
     * Get roll
     *
     * @return int
     */
    int getRoll()
    {
        return roll;
    }
}
