package bot.Encounter;

import org.jetbrains.annotations.NotNull;

public interface GuardResultInterface
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
}
