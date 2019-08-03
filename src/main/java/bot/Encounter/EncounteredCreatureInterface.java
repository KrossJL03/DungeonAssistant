package bot.Encounter;

import bot.Encounter.EncounteredCreature.Slayer;
import bot.Hostile.Loot;
import org.jetbrains.annotations.NotNull;

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
     * Get loot
     *
     * @param roll Loot roll
     *
     * @return Loot
     */
    @NotNull Loot getLoot(int roll);

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
     * Is active
     *
     * @return boolean
     */
    boolean isActive();

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
     * Heal by points
     *
     * @param hitpoints Hitpoints to heal
     *
     * @return HealActionResultInterface
     */
    @NotNull HealActionResultInterface healPoints(int hitpoints);

    /**
     * Heal by percent
     *
     * @param percent Percent of max hp to heal
     *
     * @return int Hitpoints healed
     */
    int healPercent(float percent);

    /**
     * Hurt
     *
     * @param hitpoints Hitpoints to hurt
     *
     * @return HurtActionResultInterface
     */
    @NotNull HurtActionResultInterface hurt(int hitpoints);

    /**
     * Roll damage
     *
     * @return int
     */
    int rollDamage();

    /**
     * Take damage
     *
     * @return damage taken
     */
    int takeDamage(@NotNull EncounteredCreatureInterface attacker, int damage);
}
