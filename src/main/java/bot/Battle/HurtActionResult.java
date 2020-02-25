package bot.Battle;

import org.jetbrains.annotations.NotNull;

public class HurtActionResult implements ActionResultInterface
{
    private int     currentHp;
    private int     healedHp;
    private int     maxHp;
    private String  name;
    private boolean wasBloodied;

    /**
     * Constructor.
     *
     * @param name        Name of hurt target
     * @param healedHp    Amount of hitpoints hurt
     * @param currentHp   Current hitpoints of hurt target
     * @param maxHp       Max hitpoints of hurt target
     * @param wasBloodied Ws the target bloodied before being hurt
     */
    @NotNull HurtActionResult(@NotNull String name, int healedHp, int currentHp, int maxHp, boolean wasBloodied)
    {
        this.currentHp = currentHp;
        this.healedHp = healedHp;
        this.maxHp = maxHp;
        this.name = name;
        this.wasBloodied = wasBloodied;
    }

    /**
     * Get current hitpoints of hurt creature
     *
     * @return int
     */
    public int getCurrentHp()
    {
        return currentHp;
    }

    /**
     * Get amount of hitpoints hurt
     *
     * @return int
     */
    public int getHurtHp()
    {
        return healedHp;
    }

    /**
     * Get max hitpoints of hurt creature
     *
     * @return int
     */
    public int getMaxHp()
    {
        return maxHp;
    }

    /**
     * Get name of hurt creature
     *
     * @return int
     */
    public @NotNull String getName()
    {
        return name;
    }

    /**
     * Is the hurt creature slain
     *
     * @return boolean
     */
    public boolean isSlain()
    {
        return currentHp < 1;
    }

    /**
     * Was the target bloodied before being hurt
     *
     * @return boolean
     */
    public boolean wasBloodied()
    {
        return wasBloodied;
    }
}
