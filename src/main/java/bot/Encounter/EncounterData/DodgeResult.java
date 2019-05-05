package bot.Encounter.EncounterData;

import org.jetbrains.annotations.NotNull;

public class DodgeResult {

    private String    attackerName;
    private DodgeRoll dodgeRoll;
    private int       attackerDamageRoll;
    private int       damageResisted;

    /**
     * DodgeResult constructor
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
    ) {
        this.damageResisted = damageResisted;
        this.dodgeRoll = dodgeRoll;
        this.attackerName = attackerName;
        this.attackerDamageRoll = attackerDamageRoll;
    }

    /**
     * Get attacker name
     *
     * @return String
     */
    @NotNull
    public String getAttackerName() {
        return attackerName;
    }

    /**
     * Get attacker damage roll
     *
     * @return int
     */
    public int getAttackerDamageRoll() {
        return attackerDamageRoll;
    }

    /**
     * Get damage dealt
     *
     * @return int
     */
    public int getDamageDealt() {
        return isSuccess() ? 0 : attackerDamageRoll - damageResisted;
    }

    /**
     * Get damage resisted
     *
     * @return int
     */
    public int getDamageResisted() {
        return damageResisted;
    }

    /**
     * Get dodge roll
     *
     * @return int
     */
    public int getDodgeRoll() {
        return dodgeRoll.getRoll();
    }

    /**
     * Was the dodge successful
     *
     * @return boolean
     */
    public boolean isSuccess() {
        return !dodgeRoll.isFail();
    }
}
