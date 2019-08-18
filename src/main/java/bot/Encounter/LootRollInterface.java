package bot.Encounter;

import bot.Hostile.Loot;
import org.jetbrains.annotations.NotNull;

public interface LootRollInterface
{
    /**
     * Get kill name
     *
     * @return String
     */
    @NotNull
    String getKillName();

    /**
     * Get loot
     *
     * @return Loot
     */
    @NotNull
    Loot getLoot();

    /**
     * Get loot die
     *
     * @return int
     */
    int getLootDie();

    /**
     * Get loot roll
     *
     * @return int
     */
    int getLootRoll();
}
