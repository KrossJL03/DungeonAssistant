package bot.Encounter.Logger.MessageBuilder;

import org.jetbrains.annotations.NotNull;

public interface AttackActionDataInterface extends CombatActionDataInterface {

    /**
     * Get attacker name
     *
     * @return String
     */
    @NotNull
    String getAttackerName();

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
     * Get hit roll
     *
     * @return int
     */
    int getHitRoll();

    /**
     * Get hit type
     *
     * @return String
     */
    @NotNull
    String getHitType();

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
