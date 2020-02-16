package bot.Battle;

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
    int getHurtHp();

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

    /**
     * Was the target bloodied before being hurt
     *
     * @return boolean
     */
    boolean wasBloodied();
}
