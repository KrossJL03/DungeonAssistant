package bot.Encounter.EncounteredCreature;

import bot.Encounter.HurtActionResultInterface;
import org.jetbrains.annotations.NotNull;

class HurtActionResult implements HurtActionResultInterface
{
    private String name;
    private int    currentHp;
    private int    healedHp;
    private int    maxHp;

    /**
     * HurtActionResult constructor
     *
     * @param name Name of hurt creature
     * @param healedHp Amount of hitpoints hurt
     * @param currentHp Current hitpoints of hurt creature
     * @param maxHp Max hitpoints of hurt creature
     */
    @NotNull HurtActionResult(@NotNull String name, int healedHp, int currentHp, int maxHp)
    {
        this.currentHp = currentHp;
        this.healedHp = healedHp;
        this.maxHp = maxHp;
        this.name = name;
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
    public int getHealedHp()
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
}
