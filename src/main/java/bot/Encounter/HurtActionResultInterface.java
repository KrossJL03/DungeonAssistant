package bot.Encounter;

import org.jetbrains.annotations.NotNull;

public interface HurtActionResultInterface extends ActionResultInterface
{
    /**
     * Get current hitpoints of hurt creature
     *
     * @return int
     */
    int getCurrentHp();

    /**
     * Get amount of hitpoints hurt
     *
     * @return int
     */
    int getHealedHp();

    /**
     * Get max hitpoints of hurt creature
     *
     * @return int
     */
    int getMaxHp();

    /**
     * Get name of hurt creature
     *
     * @return int
     */
    @NotNull String getName();

    /**
     * Is the hurt creature slain
     *
     * @return boolean
     */
    boolean isSlain();
}
