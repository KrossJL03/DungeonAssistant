package bot.Encounter.EncounterData;

import org.jetbrains.annotations.NotNull;

public class ProtectActionResult implements ActionResultInterface {

    private Slayer targetSlayer;
    private String protectedName;
    private String protectedOwnerId;
    private String targetName;
    private int    damageDealt;
    private int    damageResisted;
    private int    targetCurrentHp;
    private int    targetMaxHp;

    /**
     * ProtectActionResult constructor
     *
     * @param targetName      Target name
     * @param protectedName   Name of protected explorer
     * @param protectedOwnerId  User id of protected owner
     * @param damageDealt     Damage dealt to target
     * @param damageResisted  Damage resisted by target
     * @param targetCurrentHp Target current hp
     * @param targetMaxHp     Target max hp
     * @param targetSlayer    Target slayer
     */
    ProtectActionResult(
        String targetName,
        @NotNull String protectedName,
        @NotNull String protectedOwnerId,
        int damageDealt,
        int damageResisted,
        int targetCurrentHp,
        int targetMaxHp,
        Slayer targetSlayer
    ) {
        this.damageDealt = damageDealt;
        this.damageResisted = damageResisted;
        this.protectedName = protectedName;
        this.protectedOwnerId = protectedOwnerId;
        this.targetCurrentHp = targetCurrentHp;
        this.targetMaxHp = targetMaxHp;
        this.targetName = targetName;
        this.targetSlayer = targetSlayer;
    }

    /**
     * Get damage dealt
     *
     * @return int
     */
    public int getDamageDealt() {
        return damageDealt;
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
     * Get protected name
     *
     * @return String
     */
    public @NotNull String getProtectedName() {
        return protectedName;
    }

    /**
     * Get protected owner id
     *
     * @return String
     */
    public @NotNull String getProtectedOwnerId() {
        return protectedOwnerId;
    }

    /**
     * Get target current hp
     *
     * @return int
     */
    public int getTargetCurrentHp() {
        return targetCurrentHp;
    }

    /**
     * Get target max hp
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
    public @NotNull String getTargetName() {
        return targetName;
    }

    /**
     * Get target slayer
     *
     * @return Slayer
     */
    public @NotNull Slayer getTargetSlayer() {
        return targetSlayer;
    }
}
