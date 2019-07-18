package bot.Encounter;

import org.jetbrains.annotations.NotNull;

public interface EncounterPhaseInterface
{
    String ATTACK_PHASE = "ATTACK";
    String DODGE_PHASE  = "DODGE";
    String LOOT_PHASE   = "LOOT";
    String CREATE_PHASE = "CREATE";
    String END_PHASE    = "END";
    String JOIN_PHASE   = "JOIN";
    String RP_PHASE     = "RP";

    /**
     * Get phase name
     *
     * @return String
     */
    @NotNull String getPhaseName();

    /**
     * Is attack phase
     *
     * @return boolean
     */
    boolean isAttackPhase();

    /**
     * Is dodge phase
     *
     * @return boolean
     */
    boolean isDodgePhase();

    /**
     * Is end phase
     *
     * @return bool
     */
    boolean isEndPhase();

    /**
     * Is join phase
     *
     * @return bool
     */
    boolean isJoinPhase();

    /**
     * Is loot phase
     *
     * @return boolean
     */
    boolean isLootPhase();

    /**
     * Is rp phase
     *
     * @return boolean
     */
    boolean isRpPhase();

    /**
     * Is create phase
     *
     * @return boolean
     */
    boolean isCreatePhase();

    /**
     * Is phase a final phase
     *
     * @return boolean
     */
    boolean isFinalPhase();

    /**
     * Is initiative phase
     *
     * @return boolean
     */
    boolean isInitiativePhase();
}
