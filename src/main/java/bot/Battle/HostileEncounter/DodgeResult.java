package bot.Battle.HostileEncounter;

import org.jetbrains.annotations.NotNull;

public class DodgeResult
{
    private int       attackerDamageRoll;
    private String    attackerName;
    private int       damageResisted;
    private DodgeRoll dodgeRoll;

    /**
     * Constructor.
     *
     * @param attackerName       Attacker name
     * @param dodgeRoll          Dodge roll
     * @param attackerDamageRoll Attack damage roll
     * @param damageResisted     Damage resisted
     */
    DodgeResult(
        @NotNull String attackerName,
        @NotNull DodgeRoll dodgeRoll,
        int attackerDamageRoll,
        int damageResisted
    )
    {
        this.attackerDamageRoll = attackerDamageRoll;
        this.attackerName = attackerName;
        this.damageResisted = damageResisted;
        this.dodgeRoll = dodgeRoll;
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
        return isSuccess() ? 0 : attackerDamageRoll - damageResisted;
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

    /**
     * Get dodge roll
     *
     * @return int
     */
    public int getTargetDodgeRoll()
    {
        return dodgeRoll.getRoll();
    }

    /**
     * Is successful dodge
     *
     * @return boolean
     */
    public boolean isSuccess()
    {
        return !dodgeRoll.isFail();
    }
}
