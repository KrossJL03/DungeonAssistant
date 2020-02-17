package bot.Battle.HostileEncounter;

import org.jetbrains.annotations.NotNull;

public class GuardResult
{
    private int    attackerDamageRoll;
    private String attackerName;
    private int    damageResisted;

    /**
     * GuardResult constructor
     *
     * @param attackerName       Attacker name
     * @param attackerDamageRoll Attack damage roll
     * @param damageResisted     Damage resisted
     */
    GuardResult(
        @NotNull String attackerName,
        int attackerDamageRoll,
        int damageResisted
    )
    {
        this.attackerDamageRoll = attackerDamageRoll;
        this.attackerName = attackerName;
        this.damageResisted = damageResisted;
    }

    /**
     * Get damage roll
     *
     * @return int
     */
    public int getAttackerDamageRoll()
    {
        return attackerDamageRoll;
    }

    /**
     * Get attacker name
     *
     * @return String
     */
    public @NotNull String getAttackerName()
    {
        return attackerName;
    }

    /**
     * Get damage dealt to target
     *
     * @return int
     */
    public int getDamageDealt()
    {
        return attackerDamageRoll - damageResisted;
    }

    /**
     * Get damage resisted by the target
     *
     * @return int
     */
    public int getDamageResisted()
    {
        return damageResisted;
    }
}
