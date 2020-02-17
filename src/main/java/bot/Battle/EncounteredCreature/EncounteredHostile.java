package bot.Battle.EncounteredCreature;

import bot.Battle.EncounteredCreatureInterface;
import bot.Battle.EncounteredHostileInterface;
import bot.Constant;
import bot.CustomException;
import bot.Hostile.Hostile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class EncounteredHostile implements EncounteredHostileInterface
{
    private int               attack;
    private int               attackRoll;
    private int               currentHp;
    private boolean           hasNickname;
    private Hostile           hostile;
    private int               maxHp;
    private String            name;
    private ArrayList<Slayer> slayers;
    private String            species;

    /**
     * EncounteredHostile constructor
     *
     * @param hostile Hostile
     * @param name    Name
     */
    public EncounteredHostile(@NotNull Hostile hostile, @Nullable String name)
    {
        this.attack = hostile.getAttack();
        this.attackRoll = 0;
        this.currentHp = hostile.getHitpoints();
        this.hasNickname = name != null;
        this.hostile = hostile;
        this.maxHp = hostile.getHitpoints();
        this.name = name != null ? name : hostile.getSpecies();
        this.slayers = new ArrayList<>();
        this.species = hostile.getSpecies();
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
        return slayers.isEmpty() ? new Slayer() : slayers.get(slayers.size() - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getSpecies()
    {
        return species;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNickname()
    {
        return hasNickname;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull HealActionResult healPercent(float percent)
    {
        return healPoints((int) Math.floor(maxHp * percent));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull HealActionResult healPoints(int hitpoints)
    {
        if (hitpoints < 0) {
            throw new CustomException("The amount of HP to heal must be a positive number.");
        }

        boolean wasRevived = false;
        if (isSlain()) {
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
    public @NotNull HurtActionResult hurt(int hitpoints) throws EncounteredCreatureException
    {
        if (isSlain()) {
            throw EncounteredCreatureException.createIsSlain(name, getSlayer().getName());
        } else if (hitpoints < 0) {
            throw new CustomException("The amount of HP to hurt must be a positive number.");
        }

        boolean wasBloodied = isBloodied();
        int     hurtHp;

        if (this.currentHp - hitpoints < 0) {
            hurtHp = currentHp - hitpoints;
            currentHp = 0;
        } else {
            hurtHp = hitpoints;
            currentHp -= hitpoints;
        }

        return new HurtActionResult(name, hurtHp, currentHp, maxHp, wasBloodied);
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
    public @NotNull ModifyStatActionResult modifyStat(@NotNull String statName, int statModifier)
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
    public @NotNull ArrayList<LootRoll> rollLoot() throws EncounteredCreatureException
    {
        if (!isSlain()) {
            throw EncounteredCreatureException.createLootWhenNotSlain(name);
        }

        ArrayList<LootRoll> lootRolls = new ArrayList<>();
        for (LootRoll lootRoll : hostile.rollLoot()) {
            lootRolls.add(new LootRoll(name, lootRoll.getLoot(), lootRoll.getDie(), lootRoll.getRoll()));
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
    public @NotNull ModifyStatActionResult setStat(@NotNull String statName, int statValue)
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
            throw EncounteredCreatureException.createIsSlain(name, getSlayer().getName());
        }

        if (currentHp > 0 && currentHp - damage < 1) {
            addSlayer(attacker.getName());
            currentHp = 0;
        } else {
            currentHp -= damage;
        }
        return damage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean wasSlainBy(@NotNull EncounteredCreatureInterface creature)
    {
        for (Slayer slayer : slayers) {
            if (slayer.isSlayer(creature)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Add slayer
     * Does not add slayer if slayer with the same name already exists
     *
     * @param slayerName Name of slayer
     */
    private void addSlayer(@NotNull String slayerName)
    {
        for (Slayer slayer : slayers) {
            if (slayer.getName().equals(slayerName)) {
                return;
            }
        }

        slayers.add(new Slayer(slayerName));
    }

    /**
     * Modify attack
     *
     * @param statModifier Attack modifier
     *
     * @return ModifyStatActionResult
     */
    private @NotNull ModifyStatActionResult modifyAttack(int statModifier)
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
     * @return ModifyStatActionResult
     */
    private @NotNull ModifyStatActionResult modifyHitpoints(int statModifier)
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

    /**
     * Roll damage
     *
     * @return int
     */
    private int rollDamage()
    {
        return (int) Math.floor(Math.random() * getAttackDice()) + 1;
    }
}
