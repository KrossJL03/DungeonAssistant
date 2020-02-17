package bot.Battle;

import bot.Constant;
import bot.CustomException;
import org.jetbrains.annotations.NotNull;

abstract public class CombatCreature
{
    private int    currentHp;
    private int    maxHp;
    private String name;

    /**
     * Constructor.
     *
     * @param maxHp Max hp
     * @param name  Name
     */
    protected CombatCreature(int maxHp, @NotNull String name)
    {
        this.currentHp = maxHp;
        this.maxHp = maxHp;
        this.name = name;
    }

    /**
     * Get attack dice
     *
     * @return int
     */
    abstract public int getAttackDice();

    /**
     * Get current hitpoints
     *
     * @return int
     */
    public int getCurrentHP()
    {
        return currentHp;
    }

    /**
     * Get max hitpoints
     *
     * @return int
     */
    public int getMaxHP()
    {
        return maxHp;
    }

    /**
     * Get name
     *
     * @return String
     */
    public @NotNull String getName()
    {
        return name;
    }

    /**
     * Get slayer
     *
     * @return Slayer
     */
    abstract public @NotNull Slayer getSlayer();

    /**
     * Heal by percent
     *
     * @param percent Percent of max hp to heal
     *
     * @return HealActionResult
     */
    public @NotNull HealActionResult healPercent(float percent)
    {
        return healPoints((int) Math.floor(maxHp * percent));
    }

    /**
     * Is active
     *
     * @return boolean
     */
    abstract public boolean isActive();

    /**
     * Is the creature at low health
     *
     * @return boolean
     */
    public boolean isBloodied()
    {
        return currentHp < (maxHp / 4);
    }

    /**
     * Does this name match the name of the creature
     *
     * @param name Name
     *
     * @return boolean
     */
    public boolean isName(@NotNull String name)
    {
        return this.name.toLowerCase().equals(name.toLowerCase());
    }

    /**
     * Is slain
     *
     * @return boolean
     */
    public boolean isSlain()
    {
        return currentHp < 1;
    }

    /**
     * Modify stat
     *
     * @param statName     Name of stat to modify
     * @param statModifier Modifier to apply to stat
     */
    abstract public @NotNull ModifyStatActionResult modifyStat(@NotNull String statName, int statModifier);

    /**
     * Set name
     *
     * @param name Name
     */
    public void setName(@NotNull String name)
    {
        this.name = name;
    }

    /**
     * Modify stat
     *
     * @param statName  Name of stat to modify
     * @param statValue New stat value
     */
    abstract public @NotNull ModifyStatActionResult setStat(@NotNull String statName, int statValue);

    /**
     * Was this creature slain by the given creature
     *
     * @param creature Creature to check as slayer
     *
     * @return boolean
     */
    abstract public boolean wasSlainBy(@NotNull CombatCreature creature);

    /**
     * Heal by points
     *
     * @param hitpoints Hitpoints to heal
     *
     * @return HealActionResult
     */
    @NotNull HealActionResult healPoints(int hitpoints)
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

        postHeal();

        return new HealActionResult(name, healedHp, currentHp, maxHp, wasRevived);
    }

    /**
     * Hurt
     *
     * @param hitpoints Hitpoints to hurt
     *
     * @return HurtActionResult
     *
     * @throws CombatCreatureException If creature is slain
     */
    @NotNull
    protected HurtActionResult hurt(int hitpoints) throws CombatCreatureException
    {
        if (isSlain()) {
            throw CombatCreatureException.createIsSlain(name, getSlayer().getName());
        } else if (hitpoints < 0) {
            throw new CustomException("The amount of HP to hurt must be a positive number.");
        }

        boolean wasBloodied = isBloodied();
        int     hurtHp;

        if (currentHp - hitpoints < 0) {
            hurtHp = currentHp;
            currentHp = 0;
        } else {
            hurtHp = hitpoints;
            currentHp -= hitpoints;
        }

        return new HurtActionResult(name, hurtHp, currentHp, maxHp, wasBloodied);
    }

    /**
     * Take damage
     *
     * @return damage taken
     *
     * @throws CombatCreatureException If creature is slain
     */
    protected int takeDamage(@NotNull CombatCreature attacker, int damage) throws CombatCreatureException
    {
        if (isSlain()) {
            throw CombatCreatureException.createIsSlain(name, getSlayer().getName());
        }

        damage = damage - getEndurance();
        damage = Math.max(1, damage);
        if (currentHp > 0 && currentHp - damage < 1) {
            addSlayer(new Slayer(attacker.getName()));
            currentHp = 0;
        } else {
            currentHp -= damage;
        }

        return damage;
    }

    /**
     * Add slayer
     *
     * @param slayer Slayer
     */
    abstract protected void addSlayer(@NotNull Slayer slayer);

    /**
     * Get endurance
     */
    abstract protected int getEndurance();

    /**
     * Modify hitpoints
     *
     * @param statModifier Hitpoints modifier
     *
     * @return ModifyStatActionResult
     */
    final protected @NotNull ModifyStatActionResult modifyHitpoints(int statModifier)
    {
        preModifyHitpoints(statModifier);

        maxHp += statModifier;
        if (statModifier > 0) {
            healPoints(statModifier);
        }

        return new ModifyStatActionResult(name, Constant.EXPLORER_STAT_HITPOINTS, statModifier, maxHp);
    }

    /**
     * Handle any additional post heal processes
     */
    abstract protected void postHeal();

    /**
     * Handle any additional pre modify hitpoints processes
     *
     * @param statModifier Stat modifier
     */
    abstract protected void preModifyHitpoints(int statModifier);

    /**
     * Roll a die of the given size
     *
     * @param die Die to roll
     *
     * @return int
     */
    final protected int roll(int die)
    {
        return (int) Math.floor(Math.random() * die) + 1;
    }
}
