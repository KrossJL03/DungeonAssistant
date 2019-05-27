package bot.Encounter;

import org.jetbrains.annotations.NotNull;

public class EncounterPhase
{
    public static  String ATTACK_PHASE = "ATTACK";
    public static  String DODGE_PHASE  = "DODGE";
    public static  String LOOT_PHASE   = "LOOT";
    private static String CREATE_PHASE = "CREATE";
    private static String END_PHASE    = "END";
    private static String JOIN_PHASE   = "JOIN";
    private static String RP_PHASE     = "RP";

    private String phaseName;

    /**
     * EncounterPhase constructor
     *
     * @param phaseName Phase name
     */
    private @NotNull EncounterPhase(String phaseName)
    {
        this.phaseName = phaseName;
    }

    /**
     * EncounterPhase constructor (ATTACK_PHASE)
     *
     * @return EncounterPhase
     */
    static @NotNull EncounterPhase createAttackPhase()
    {
        return new EncounterPhase(ATTACK_PHASE);
    }

    /**
     * EncounterPhase constructor (CREATE_PHASE)
     *
     * @return EncounterPhase
     */
    static @NotNull EncounterPhase createCreatePhase()
    {
        return new EncounterPhase(CREATE_PHASE);
    }

    /**
     * EncounterPhase constructor (DODGE_PHASE)
     *
     * @return EncounterPhase
     */
    static @NotNull EncounterPhase createDodgePhase()
    {
        return new EncounterPhase(DODGE_PHASE);
    }

    /**
     * EncounterPhase constructor (END_PHASE)
     *
     * @return EncounterPhase
     */
    static @NotNull EncounterPhase createEndPhase()
    {
        return new EncounterPhase(END_PHASE);
    }

    /**
     * EncounterPhase constructor (JOIN_PHASE)
     *
     * @return EncounterPhase
     */
    static @NotNull EncounterPhase createJoinPhase()
    {
        return new EncounterPhase(JOIN_PHASE);
    }

    /**
     * EncounterPhase constructor (LOOT_PHASE)
     *
     * @return EncounterPhase
     */
    static @NotNull EncounterPhase createLootPhase()
    {
        return new EncounterPhase(LOOT_PHASE);
    }

    /**
     * EncounterPhase constructor (RP_PHASE)
     *
     * @return EncounterPhase
     */
    static @NotNull EncounterPhase createRpPhase()
    {
        return new EncounterPhase(RP_PHASE);
    }

    /**
     * Get phase name
     *
     * @return String
     */
    @NotNull String getPhaseName()
    {
        return phaseName;
    }

    /**
     * Is attack phase
     *
     * @return boolean
     */
    public boolean isAttackPhase()
    {
        return phaseName.equals(ATTACK_PHASE);
    }

    /**
     * Is dodge phase
     *
     * @return boolean
     */
    public boolean isDodgePhase()
    {
        return phaseName.equals(DODGE_PHASE);
    }

    /**
     * Is end phase
     *
     * @return bool
     */
    public boolean isEndPhase()
    {
        return phaseName.equals(END_PHASE);
    }

    /**
     * Is join phase
     *
     * @return bool
     */
    public boolean isJoinPhase()
    {
        return phaseName.equals(JOIN_PHASE);
    }

    /**
     * Is loot phase
     *
     * @return boolean
     */
    public boolean isLootPhase()
    {
        return phaseName.equals(LOOT_PHASE);
    }

    /**
     * Is rp phase
     *
     * @return boolean
     */
    public boolean isRpPhase()
    {
        return phaseName.equals(RP_PHASE);
    }

    /**
     * Is create phase
     *
     * @return boolean
     */
    boolean isCreatePhase()
    {
        return phaseName.equals(CREATE_PHASE);
    }

    /**
     * Is phase a final phase
     *
     * @return boolean
     */
    boolean isFinalPhase()
    {
        return isLootPhase() || isEndPhase();
    }

    /**
     * Is initiative phase
     *
     * @return boolean
     */
    boolean isInitiativePhase()
    {
        return isAttackPhase() || isDodgePhase();
    }
}
