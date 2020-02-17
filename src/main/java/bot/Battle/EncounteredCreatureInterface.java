package bot.Battle;

import bot.Battle.EncounteredCreature.EncounteredCreatureException;
import bot.Battle.EncounteredCreature.HealActionResult;
import bot.Battle.EncounteredCreature.HurtActionResult;
import bot.Battle.EncounteredCreature.LootRoll;
import bot.Battle.EncounteredCreature.ModifyStatActionResult;
import bot.Battle.EncounteredCreature.Slayer;
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
     * @return HealActionResult
     */
    @NotNull HealActionResult healPercent(float percent);

    /**
     * Heal by points
     *
     * @param hitpoints Hitpoints to heal
     *
     * @return HealActionResult
     */
    @NotNull HealActionResult healPoints(int hitpoints);

    /**
     * Hurt
     *
     * @param hitpoints Hitpoints to hurt
     *
     * @return HurtActionResult
     *
     * @throws EncounteredCreatureException If creature is slain
     */
    @NotNull HurtActionResult hurt(int hitpoints) throws EncounteredCreatureException;

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
    @NotNull ModifyStatActionResult modifyStat(@NotNull String statName, int statModifier);

    /**
     * Roll loot
     *
     * @return ArrayList<LootRoll>
     *
     * @throws EncounteredCreatureException When attempting to loot and not slain
     */
    @NotNull ArrayList<LootRoll> rollLoot() throws EncounteredCreatureException;

    /**
     * Modify stat
     *
     * @param statName  Name of stat to modify
     * @param statValue New stat value
     */
    @NotNull ModifyStatActionResult setStat(@NotNull String statName, int statValue);

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
