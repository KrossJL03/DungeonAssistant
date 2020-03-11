package bot.Battle.Encounter;

import bot.Hostile.Loot;
import org.jetbrains.annotations.NotNull;

public class LootRoll
{
    private int    die;
    private String hostileName;
    private Loot   loot;
    private int    roll;

    /**
     * Constructor.
     *
     * @param hostileName Hostile name
     * @param loot        Loot
     * @param die         Loot die
     * @param roll        Loot roll
     */
    public @NotNull LootRoll(@NotNull String hostileName, @NotNull Loot loot, int die, int roll)
    {
        this.die = die;
        this.hostileName = hostileName;
        this.loot = loot;
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
     * Get kill name
     *
     * @return String
     */
    @NotNull String getKillName()
    {
        return hostileName;
    }

    /**
     * Get loot
     *
     * @return Loot
     */
    @NotNull Loot getLoot()
    {
        return loot;
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
