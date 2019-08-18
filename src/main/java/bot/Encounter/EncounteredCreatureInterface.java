package bot.Encounter;

import bot.Encounter.EncounteredCreature.EncounteredCreatureException;
import bot.Encounter.EncounteredCreature.LootRoll;
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
     * Modify stat
     *
     * @param statName  Name of stat to modify
     * @param statValue New stat value
     */
    @NotNull ModifyStatActionResultInterface setStat(@NotNull String statName, int statValue);

    /**
     * Roll loot
     *
     * @return ArrayList<LootRoll>
     *
     * @throws EncounteredCreatureException When attempting to loot and not slain
     */
    @NotNull ArrayList<LootRoll> rollLoot() throws EncounteredCreatureException;

    /**
     * Take damage
     *
     * @return damage taken
     */
    int takeDamage(@NotNull EncounteredCreatureInterface attacker, int damage);
}
