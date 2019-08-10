package bot.Encounter.EncounteredCreature;

import bot.Constant;
import bot.Encounter.*;
import bot.Hostile.Hostile;
import bot.Hostile.Loot;
import org.jetbrains.annotations.NotNull;

public class EncounteredHostile implements EncounteredHostileInterface
{
    private Hostile hostile;
    private Slayer  slayer;
    private String  name;
    private int     attack;
    private int     attackRoll;
    private int     currentHp;
    private int     maxHp;

    /**
     * EncounteredHostile constructor
     *
     * @param hostile Hostile
     * @param name    Name
     */
    public EncounteredHostile(@NotNull Hostile hostile, @NotNull String name)
    {
        this.attack = hostile.getAttackDice();
        this.attackRoll = 0;
        this.currentHp = hostile.getHitpoints();
        this.maxHp = hostile.getHitpoints();
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
        return attack;
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
        return maxHp;
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
        if (currentHp + hitpoints > maxHp) {
            healedHp = maxHp - currentHp;
            currentHp = maxHp;
        } else {
            healedHp = hitpoints;
            currentHp += hitpoints;
        }

        if (isSlain()) {
            slayer = null;
        }

        return new HealActionResult(name, healedHp, currentHp, maxHp);
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
        int hitpointsHealed = (int) Math.floor(maxHp * percent);
        if (currentHp + hitpointsHealed > maxHp) {
            hitpointsHealed = maxHp - currentHp;
            currentHp = maxHp;
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

        return new HurtActionResult(name, hurtHp, currentHp, maxHp);
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
    public @NotNull ModifyStatActionResultInterface modifyStat(@NotNull String statName, int statModifier)
    {
        switch (statName.toLowerCase()) {
            case Constant.HOSTILE_STAT_ATTACK:
                return modifyAttack(statModifier);
            case Constant.HOSTILE_STAT_HITPOINTS:
                return modifyHitpoints(statModifier);
            case Constant.HOSTILE_STAT_ATTACK_SHORT:
                return modifyAttack(statModifier);
            case Constant.HOSTILE_STAT_HITPOINTS_SHORT:
                return modifyHitpoints(statModifier);
            default:
                throw EncounteredHostileException.createInvalidStatName(statName);
        }
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
    public @NotNull ModifyStatActionResultInterface setStat(@NotNull String statName, int statValue)
    {
        switch (statName.toLowerCase()) {
            case Constant.HOSTILE_STAT_ATTACK:
                return modifyAttack(attack - statValue);
            case Constant.HOSTILE_STAT_HITPOINTS:
                return modifyHitpoints(maxHp - statValue);
            case Constant.HOSTILE_STAT_ATTACK_SHORT:
                return modifyAttack(attack - statValue);
            case Constant.HOSTILE_STAT_HITPOINTS_SHORT:
                return modifyHitpoints(maxHp - statValue);
            default:
                throw EncounteredHostileException.createInvalidStatName(statName);
        }
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

    private @NotNull ModifyStatActionResultInterface modifyAttack(int statModifier)
    {
        int moddedStatValue = attack + statModifier;
        if (moddedStatValue < Constant.HOSTILE_MIN_ATTACK) {
            throw EncounteredCreatureException.createStatLessThanMin(
                name,
                Constant.HOSTILE_STAT_ATTACK,
                Constant.HOSTILE_MIN_ATTACK
            );
        }
        attack = moddedStatValue;

        return new ModifyStatActionResult(name, Constant.HOSTILE_STAT_ATTACK, statModifier, attack);
    }

    /**
     * Modify hitpoints
     *
     * @param statModifier Hitpoints modifier
     *
     * @return ModifyStatActionResultInterface
     */
    private @NotNull ModifyStatActionResultInterface modifyHitpoints(int statModifier)
    {
        int moddedStatValue = maxHp + statModifier;
        if (moddedStatValue < Constant.HOSTILE_MIN_HITPOINTS) {
            throw EncounteredCreatureException.createStatLessThanMin(
                name,
                Constant.HOSTILE_STAT_HITPOINTS,
                Constant.HOSTILE_MIN_HITPOINTS
            );
        }
        maxHp += statModifier;
        if (statModifier > 0) {
            currentHp += statModifier;
        }
        if (currentHp > maxHp) {
            currentHp = maxHp;
        }

        return new ModifyStatActionResult(name, Constant.HOSTILE_STAT_HITPOINTS, statModifier, maxHp);
    }
}
