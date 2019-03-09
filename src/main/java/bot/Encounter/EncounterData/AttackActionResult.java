package bot.Encounter.EncounterData;

import org.jetbrains.annotations.NotNull;

public class AttackActionResult implements ActionResultInterface {

    private HitRoll hitRoll;
    private String  hostileName;
    private String  pcName;
    private int     damageDie;
    private int     damageRoll;
    private int     hostileCurrentHp;
    private int     hostileMaxHp;

    /**
     * AttackActionResult constructor
     *
     * @param pcName           PlayerCharacter name
     * @param hostileName      Hostile name
     * @param hitRoll          Roll to hit hostile
     * @param damageDie        Damage die rolled by player character
     * @param damageRoll       Damage dealt to hostile
     * @param hostileCurrentHp Hostile's current hitpoints
     * @param hostileMaxHp     Hostile's max hitpoints
     */
    AttackActionResult(
        @NotNull String pcName,
        @NotNull String hostileName,
        @NotNull HitRoll hitRoll,
        int damageDie,
        int damageRoll,
        int hostileCurrentHp,
        int hostileMaxHp
    ) {
        this.damageDie = damageDie;
        this.damageRoll = damageRoll;
        this.hitRoll = hitRoll;
        this.hostileCurrentHp = hostileCurrentHp;
        this.hostileMaxHp = hostileMaxHp;
        this.hostileName = hostileName;
        this.pcName = pcName;
    }

    /**
     * Get max damageRoll a pc could roll
     *
     * @return int
     */
    public int getDamageDie() {
        return damageDie;
    }

    /**
     * Get damageRoll delt to hostile
     *
     * @return int
     */
    public int getDamageRoll() {
        return damageRoll;
    }

    /**
     * Get roll to hit hostile
     *
     * @return int
     */
    public int getHitRoll() {
        return hitRoll.getRoll();
    }

    /**
     * Get hostile current hitpoints
     *
     * @return int
     */
    public int getHostileCurrentHp() {
        return hostileCurrentHp;
    }

    /**
     * Get hostile max hitpoints
     *
     * @return int
     */
    public int getHostileMaxHp() {
        return hostileMaxHp;
    }

    /**
     * Get hostile name
     *
     * @return String
     */
    @NotNull
    public String getHostileName() {
        return hostileName;
    }

    /**
     * Get player character name
     *
     * @return String
     */
    @NotNull
    public String getPcName() {
        return pcName;
    }

    public boolean isCrit() {
        return this.hitRoll.isCrit();
    }

    public boolean isFail() {
        return this.hitRoll.isFail();
    }

    public boolean isHit() {
        return this.hitRoll.isHit();
    }

    public boolean isMiss() {
        return this.hitRoll.isMiss();
    }

    /**
     * Is hostile slain
     *
     * @return boolean
     */
    public boolean isHostileSlain() {
        return this.hostileCurrentHp < 1;
    }
}
