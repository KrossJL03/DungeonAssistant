package bot.Battle;

import bot.Battle.ActionResultInterface;
import org.jetbrains.annotations.NotNull;

public class HealActionResult implements ActionResultInterface
{
    private int     currentHp;
    private int     healedHp;
    private int     maxHp;
    private String  name;
    private boolean wasTargetRevived;

    /**
     * HealActionResult constructor
     *
     * @param name             Name of healed creature
     * @param healedHp         Amount of hitpoints healed
     * @param currentHp        Current hitpoints of healed creature
     * @param maxHp            Max hitpoints of healed creature
     * @param wasTargetRevived Was the target revived
     */
    @NotNull HealActionResult(@NotNull String name, int healedHp, int currentHp, int maxHp, boolean wasTargetRevived)
    {
        this.currentHp = currentHp;
        this.healedHp = healedHp;
        this.maxHp = maxHp;
        this.name = name;
        this.wasTargetRevived = wasTargetRevived;
    }

    /**
     * Get current hitpoints of healed creature
     *
     * @return int
     */
    public int getCurrentHp()
    {
        return currentHp;
    }

    /**
     * Get amount of hitpoints healed
     *
     * @return int
     */
    public int getHealedHp()
    {
        return healedHp;
    }

    /**
     * Get max hitpoints of healed creature
     *
     * @return int
     */
    public int getMaxHp()
    {
        return maxHp;
    }

    /**
     * Get name of healed creature
     *
     * @return int
     */
    public @NotNull String getName()
    {
        return name;
    }

    /**
     * Was the target revived
     *
     * @return int
     */
    public boolean wasTargetRevived()
    {
        return wasTargetRevived;
    }
}
