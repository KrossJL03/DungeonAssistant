package bot.Encounter;

import bot.Encounter.EncounteredCreature.EncounteredCreatureException;
import bot.Encounter.EncounteredCreature.Slayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface EncounteredCreatureInterface
{
    /**
     * Get attack dice
     *
     * @return int
     */
    int getAttackDice();

    /**
     * Get current hitpoints
     *
     * @return int
     */
    int getCurrentHP();

    /**
     * Get max hitpoints
     *
     * @return int
     */
    int getMaxHP();

    /**
     * Get name
     *
     * @return String
     */
    @NotNull String getName();

    /**
     * Get slayer
     *
     * @return Slayer
     */
    @NotNull Slayer getSlayer();

    /**
     * Heal by percent
     *
     * @param percent Percent of max hp to heal
     *
     * @return HealActionResultInterface
     */
    @NotNull HealActionResultInterface healPercent(float percent);

    /**
     * Heal by points
     *
     * @param hitpoints Hitpoints to heal
     *
     * @return HealActionResultInterface
     */
    @NotNull HealActionResultInterface healPoints(int hitpoints);

    /**
     * Hurt
     *
     * @param hitpoints Hitpoints to hurt
     *
     * @return HurtActionResultInterface
     *
     * @throws EncounteredCreatureException If creature is slain
     */
    @NotNull HurtActionResultInterface hurt(int hitpoints) throws EncounteredCreatureException;

    /**
     * Is active
     *
     * @return boolean
     */
    boolean isActive();

    /**
     * Is the creature at low health
     *
     * @return boolean
     */
    boolean isBloodied();

    /**
     * Does this name match the name of the creature
     *
     * @param name Name
     *
     * @return boolean
     */
    boolean isName(@NotNull String name);

    /**
     * Is slain
     *
     * @return boolean
     */
    boolean isSlain();

    /**
     * Modify stat
     *
     * @param statName     Name of stat to modify
     * @param statModifier Modifier to apply to stat
     */
    @NotNull ModifyStatActionResultInterface modifyStat(@NotNull String statName, int statModifier);

    /**
     * Roll damage
     *
     * @return int
     */
    int rollDamage();

    /**
     * Roll loot
     *
     * @return ArrayList<LootRoll>
     *
     * @throws EncounteredCreatureException When attempting to loot and not slain
     */
    @NotNull ArrayList<LootRollInterface> rollLoot() throws EncounteredCreatureException;

    /**
     * Modify stat
     *
     * @param statName  Name of stat to modify
     * @param statValue New stat value
     */
    @NotNull ModifyStatActionResultInterface setStat(@NotNull String statName, int statValue);

    /**
     * Take damage
     *
     * @return damage taken
     *
     * @throws EncounteredCreatureException If creature is slain
     */
    int takeDamage(@NotNull EncounteredCreatureInterface attacker, int damage) throws EncounteredCreatureException;

    /**
     * Was this creature slain by the given creature
     *
     * @param creature Creature to check as slayer
     *
     * @return boolean
     */
    boolean wasSlainBy(@NotNull EncounteredCreatureInterface creature);
}
