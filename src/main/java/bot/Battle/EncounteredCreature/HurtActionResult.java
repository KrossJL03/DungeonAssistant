package bot.Battle.EncounteredCreature;

import bot.Battle.HurtActionResultInterface;
import org.jetbrains.annotations.NotNull;

class HurtActionResult implements HurtActionResultInterface
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
     * {@inheritDoc}
     */
    @Override
    public int getCurrentHp()
    {
        return currentHp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHurtHp()
    {
        return healedHp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxHp()
    {
        return maxHp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getName()
    {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSlain()
    {
        return currentHp < 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean wasBloodied()
    {
        return wasBloodied;
    }
}
