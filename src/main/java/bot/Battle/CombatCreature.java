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
     * Heal by points
     *
     * @param hitpoints Hitpoints to heal
     *
     * @return HealActionResult
     */
    @NotNull
    public HealActionResult healPoints(int hitpoints)
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
     * @throws CustomException If hitpoints to hurt is less than 0
     */
    @NotNull
    public HurtActionResult hurt(int hitpoints) throws CustomException
    {
        assertNotSlain();
        if (hitpoints < 0) {
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
     * Get maximum hitpoint value
     *
     * @return int
     */
    abstract protected int getMaxHitpointStatValue();

    /**
     * Get minimum hitpoint value
     *
     * @return int
     */
    abstract protected int getMinHitpointStatValue();

    /**
     * Modify hitpoints
     *
     * @param statModifier Hitpoints modifier
     *
     * @return ModifyStatActionResult
     */
    final protected @NotNull ModifyStatActionResult modifyHitpoints(int statModifier)
    {
        statModifier = validateStatMod(
            statModifier,
            maxHp,
            getMinHitpointStatValue(),
            getMaxHitpointStatValue()
        );

        maxHp += statModifier;
        if (statModifier > 0) {
            healPoints(statModifier);
        }

        return new ModifyStatActionResult(name, Constant.CREATURE_STAT_HITPOINTS, statModifier, maxHp);
    }

    /**
     * Handle any additional post heal processes
     */
    abstract protected void postHeal();

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

    /**
     * Take damage
     *
     * @return damage taken
     */
    protected int takeDamage(@NotNull CombatCreature attacker, int damage)
    {
        assertNotSlain();

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
     * Validate stat modifier
     *
     * @param modifier     Stat modifier
     * @param currentValue Current value of the stat the modifier will be applied to
     * @param minValue     Minimum value of the stat the modifier will be applied to
     * @param maxValue     Maximum value of the stat the modifier will be applied to
     *
     * @return int Modifier adjusted to if min and max criteria
     */
    final protected int validateStatMod(int modifier, int currentValue, int minValue, int maxValue)
    {
        int modifiedValue = currentValue + modifier;
        if (modifiedValue > maxValue) {
            modifier = maxValue - currentValue;
        } else if (modifiedValue < minValue) {
            modifier = currentValue - minValue;
        }

        return modifier;
    }

    /**
     * Assert this creature is not slain
     *
     * @throws CustomException If creature is slain
     */
    private void assertNotSlain() throws CustomException
    {
        if (isSlain()) {
            throw new CustomException(String.format("%s was slain by %s", name, getSlayer().getName()));
        }
    }
}
