package bot.Encounter.EncounteredCreature;

import bot.Encounter.HealActionResultInterface;
import org.jetbrains.annotations.NotNull;

class HealActionResult implements HealActionResultInterface
{
    private String  name;
    private int     currentHp;
    private int     healedHp;
    private int     maxHp;
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
    public boolean wasTargetRevived()
    {
        return wasTargetRevived;
    }
}
