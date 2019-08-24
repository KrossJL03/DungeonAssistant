package bot.Encounter;

import org.jetbrains.annotations.NotNull;

public interface HealActionResultInterface extends ActionResultInterface
{
    /**
     * Get current hitpoints of healed creature
     *
     * @return int
     */
    int getCurrentHp();

    /**
     * Get amount of hitpoints healed
     *
     * @return int
     */
    int getHealedHp();

    /**
     * Get max hitpoints of healed creature
     *
     * @return int
     */
    int getMaxHp();

    /**
     * Get name of healed creature
     *
     * @return int
     */
    @NotNull String getName();

    /**
     * Was the target revived
     *
     * @return int
     */
    boolean wasTargetRevived();
}
