package bot.Encounter.EncounteredCreature;

import bot.Encounter.EncounteredCreatureInterface;
import bot.Encounter.EncounteredHostileInterface;
import bot.Encounter.HealActionResultInterface;
import bot.Encounter.HurtActionResultInterface;
import bot.Hostile.Hostile;
import bot.Hostile.Loot;
import org.jetbrains.annotations.NotNull;

public class EncounteredHostile implements EncounteredHostileInterface
{
    private Hostile hostile;
    private int     currentHp;
    private String  name;
    private Slayer  slayer;
    private int     attackRoll;

    /**
     * EncounteredHostile constructor
     *
     * @param hostile Hostile
     * @param name    Name
     */
    public EncounteredHostile(@NotNull Hostile hostile, @NotNull String name)
    {
        this.attackRoll = 0;
        this.currentHp = hostile.getHitpoints();
        this.hostile = hostile;
        this.name = name;
        this.slayer = new Slayer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attack()
    {
        attackRoll = rollDamage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAttackDice()
    {
        return hostile.getAttackDice();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAttackRoll()
    {
        return attackRoll;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCurrentHP()
    {
        return currentHp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Hostile getHostile()
    {
        return hostile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxHP()
    {
        return hostile.getHitpoints();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Loot getLoot(int roll)
    {
        return hostile.getLoot(roll);
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
    public @NotNull Slayer getSlayer()
    {
        return slayer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getSpecies()
    {
        return hostile.getSpecies();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull HealActionResultInterface healPoints(int hitpoints)
    {
        int healedHp;
        if (currentHp + hitpoints > getMaxHP()) {
            healedHp = getMaxHP() - currentHp;
            currentHp = getMaxHP();
        } else {
            healedHp = hitpoints;
            currentHp += hitpoints;
        }

        if (isSlain()) {
            slayer = null;
        }

        return new HealActionResult(name, healedHp, currentHp, getMaxHP());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int healPercent(float percent)
    {
        if (isSlain()) {
            slayer = null;
        }
        int hitpointsHealed = (int) Math.floor(getMaxHP() * percent);
        if (currentHp + hitpointsHealed > getMaxHP()) {
            hitpointsHealed = getMaxHP() - currentHp;
            currentHp = getMaxHP();
        } else {
            currentHp += hitpointsHealed;
        }
        return hitpointsHealed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull HurtActionResultInterface hurt(int hitpoints)
    {
        int hurtHp;
        if (this.currentHp - hitpoints < 0) {
            hurtHp = currentHp - hitpoints;
            currentHp = 0;
        } else {
            hurtHp = hitpoints;
            currentHp -= hitpoints;
        }

        return new HurtActionResult(name, hurtHp, currentHp, getMaxHP());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive()
    {
        return !this.isSlain();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isName(@NotNull String name)
    {
        return this.name.toLowerCase().equals(name.toLowerCase());
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
    public int rollDamage()
    {
        return (int) Math.floor(Math.random() * getAttackDice()) + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(@NotNull String name)
    {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int takeDamage(@NotNull EncounteredCreatureInterface attacker, int damage)
    {
        if (currentHp > 0 && currentHp - damage < 1) {
            slayer = new Slayer(attacker.getName());
            currentHp = 0;
        } else {
            currentHp -= damage;
        }
        return damage;
    }
}
