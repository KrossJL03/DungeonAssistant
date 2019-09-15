package bot.Encounter.EncounteredCreature;

import bot.Constant;
import bot.Encounter.*;
import bot.Hostile.Hostile;
import bot.Hostile.Loot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EncounteredHostile implements EncounteredHostileInterface
{
    private int     attack;
    private int     attackRoll;
    private int     currentHp;
    private Hostile hostile;
    private int     maxHp;
    private String  name;
    private Slayer  slayer;

    /**
     * EncounteredHostile constructor
     *
     * @param hostile Hostile
     * @param name    Name
     */
    public EncounteredHostile(@NotNull Hostile hostile, @NotNull String name)
    {
        this.attack = hostile.getAttack();
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
    public @NotNull HealActionResultInterface healPercent(float percent)
    {
        return healPoints((int) Math.floor(maxHp * percent));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull HealActionResultInterface healPoints(int hitpoints)
    {
        boolean wasRevived = false;
        if (isSlain()) {
            slayer = new Slayer();
            wasRevived = true;
        }

        int healedHp;
        if (currentHp + hitpoints > maxHp) {
            healedHp = maxHp - currentHp;
            currentHp = maxHp;
        } else {
            healedHp = hitpoints;
            currentHp += hitpoints;
        }

        return new HealActionResult(name, healedHp, currentHp, maxHp, wasRevived);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull HurtActionResultInterface hurt(int hitpoints) throws EncounteredCreatureException
    {
        if (isSlain()) {
            throw EncounteredCreatureException.createIsSlain(name, slayer.getName());
        }

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
    public boolean isBloodied()
    {
        return currentHp < (maxHp / 4);
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
    public @NotNull ArrayList<LootRoll> rollLoot() throws EncounteredCreatureException
    {
        if (!isSlain()) {
            throw EncounteredCreatureException.createLootWhenNotSlain(name);
        }

        ArrayList<LootRoll> lootRolls = new ArrayList<>();
        int                 lootDie   = hostile.getLootPoolSize();

        while (hostile.getLootRollCount() > lootRolls.size()) {
            int  roll = (int) Math.floor(Math.random() * lootDie) + 1;
            Loot loot = hostile.getLoot(roll);
            lootRolls.add(new LootRoll(name, loot, lootDie, roll));
        }

        return lootRolls;
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
                return modifyAttack(statValue - attack);
            case Constant.HOSTILE_STAT_HITPOINTS:
                return modifyHitpoints(statValue - maxHp);
            case Constant.HOSTILE_STAT_ATTACK_SHORT:
                return modifyAttack(statValue - attack);
            case Constant.HOSTILE_STAT_HITPOINTS_SHORT:
                return modifyHitpoints(statValue - maxHp);
            default:
                throw EncounteredHostileException.createInvalidStatName(statName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int takeDamage(@NotNull EncounteredCreatureInterface attacker, int damage)
        throws EncounteredCreatureException
    {
        if (isSlain()) {
            throw EncounteredCreatureException.createIsSlain(name, slayer.getName());
        }

        if (currentHp > 0 && currentHp - damage < 1) {
            slayer = new Slayer(attacker.getName());
            currentHp = 0;
        } else {
            currentHp -= damage;
        }
        return damage;
    }

    /**
     * Modify attack
     *
     * @param statModifier Attack modifier
     *
     * @return ModifyStatActionResultInterface
     */
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
