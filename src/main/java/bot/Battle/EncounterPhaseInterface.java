package bot.Battle;

import org.jetbrains.annotations.NotNull;

public interface EncounterPhaseInterface
{
    String ATTACK_PHASE = "ATTACK";
    String CREATE_PHASE = "CREATE";
    String DODGE_PHASE  = "DODGE";
    String END_PHASE    = "END";
    String JOIN_PHASE   = "JOIN";
    String LOOT_PHASE   = "LOOT";
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
     * Is create phase
     *
     * @return boolean
     */
    boolean isCreatePhase();

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
}
