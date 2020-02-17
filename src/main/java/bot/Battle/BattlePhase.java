package bot.Battle;

import org.jetbrains.annotations.NotNull;

abstract public class BattlePhase
{
    final public static String ATTACK_PHASE = "ATTACK";
    final public static String CREATE_PHASE = "CREATE";
    final public static String END_PHASE    = "END";
    final public static String JOIN_PHASE   = "JOIN";

    private String phaseName;

    /**
     * Constructor.
     *
     * @param phaseName Phase name
     */
    protected BattlePhase(@NotNull String phaseName)
    {
        this.phaseName = phaseName;
    }

    /**
     * Get phase name
     *
     * @return String
     */
    public @NotNull String getPhaseName()
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
        return getPhaseName().equals(ATTACK_PHASE);
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
     * Is phase a final phase
     *
     * @return boolean
     */
    abstract public boolean isFinalPhase();

    /**
     * Is initiative phase
     *
     * @return boolean
     */
    abstract public boolean isInitiativePhase();

    /**
     * Is join phase
     *
     * @return boolean
     */
    public boolean isJoinPhase()
    {
        return phaseName.equals(JOIN_PHASE);
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
}
