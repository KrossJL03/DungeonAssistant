package bot.Encounter.EncounterData;

import org.jetbrains.annotations.NotNull;

public class AttackActionResult implements ActionResultInterface {

    private HitRoll hitRoll;
    private String  attackerName;
    private String  targetName;
    private int     damageDie;
    private int     damageRoll;
    private int     targetCurrentHp;
    private int     targetMaxHp;

    /**
     * AttackActionResult constructor
     *
     * @param attackerName    Attacker name
     * @param targetName      Target name
     * @param hitRoll         Roll to hit hostile
     * @param damageDie       Damage die rolled by player character
     * @param damageRoll      Damage dealt to hostile
     * @param targetCurrentHp Target's current hitpoints
     * @param targetMaxHp     Target's max hitpoints
     */
    AttackActionResult(
        @NotNull String attackerName,
        @NotNull String targetName,
        @NotNull HitRoll hitRoll,
        int damageDie,
        int damageRoll,
        int targetCurrentHp,
        int targetMaxHp
    ) {
        this.damageDie = damageDie;
        this.damageRoll = damageRoll;
        this.hitRoll = hitRoll;
        this.targetCurrentHp = targetCurrentHp;
        this.targetMaxHp = targetMaxHp;
        this.targetName = targetName;
        this.attackerName = attackerName;
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
     * Get max damageRoll an attacker could roll
     *
     * @return int
     */
    public int getDamageDie() {
        return damageDie;
    }

    /**
     * Get damageRoll delt to target
     *
     * @return int
     */
    public int getDamageRoll() {
        return damageRoll;
    }

    /**
     * Get roll to hit target
     *
     * @return int
     */
    public int getHitRoll() {
        return hitRoll.getRoll();
    }

    /**
     * Get target current hitpoints
     *
     * @return int
     */
    public int getTargetCurrentHp() {
        return targetCurrentHp;
    }

    /**
     * Get target max hitpoints
     *
     * @return int
     */
    public int getTargetMaxHp() {
        return targetMaxHp;
    }

    /**
     * Get target name
     *
     * @return String
     */
    @NotNull
    public String getTargetName() {
        return targetName;
    }

    /**
     * Is the hit roll a crit
     *
     * @return bool
     */
    public boolean isCrit() {
        return this.hitRoll.isCrit();
    }

    /**
     * Is the hit roll a fail
     *
     * @return bool
     */
    public boolean isFail() {
        return this.hitRoll.isFail();
    }

    /**
     * Is the hit roll a hit
     *
     * @return bool
     */
    public boolean isHit() {
        return this.hitRoll.isHit();
    }
}
