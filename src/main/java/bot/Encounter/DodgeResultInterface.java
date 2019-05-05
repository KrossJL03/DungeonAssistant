package bot.Encounter;

import org.jetbrains.annotations.NotNull;

public interface DodgeResultInterface
{
    /**
     * Get attacker name
     *
     * @return String
     */
    @NotNull String getAttackerName();

    /**
     * Get damage roll
     *
     * @return int
     */
    int getAttackerDamageRoll();

    /**
     * Get damage dealt to target
     *
     * @return int
     */
    int getDamageDealt();

    /**
     * Get damage resisted by the target
     *
     * @return int
     */
    int getDamageResisted();

    /**
     * Get dodge roll
     *
     * @return int
     */
    int getTargetDodgeRoll();

    /**
     * Is successful dodge
     *
     * @return boolean
     */
    boolean isSuccess();

}
