package bot.Encounter;

import org.jetbrains.annotations.NotNull;

public interface AttackActionResultInterface extends CombatActionResultInterface
{
    /**
     * Get attacker name
     *
     * @return String
     */
    @NotNull String getAttackerName();

    /**
     * Get damage die
     *
     * @return int
     */
    int getDamageDie();

    /**
     * Get damage roll
     *
     * @return int
     */
    int getDamageRoll();

    /**
     * Get hit die
     *
     * @return int
     */
    int getHitDie();

    /**
     * Get hit roll
     *
     * @return int
     */
    int getHitRoll();

    /**
     * Get hit type as string
     *
     * @return String
     */
    @NotNull String getHitTypeString();

    /**
     * Is crit hit roll
     *
     * @return int
     */
    boolean isCrit();

    /**
     * Is crit hit roll
     *
     * @return int
     */
    boolean isFail();

    /**
     * Is crit hit roll
     *
     * @return int
     */
    boolean isHit();
}
