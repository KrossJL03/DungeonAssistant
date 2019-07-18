package bot.Encounter.Phase;

import bot.Encounter.EncounterPhaseInterface;
import org.jetbrains.annotations.NotNull;

public class EncounterPhase implements EncounterPhaseInterface
{
    private String phaseName;

    /**
     * Phase constructor
     *
     * @param phaseName Phase name
     */
    @NotNull EncounterPhase(String phaseName)
    {
        this.phaseName = phaseName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getPhaseName()
    {
        return phaseName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAttackPhase()
    {
        return phaseName.equals(ATTACK_PHASE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDodgePhase()
    {
        return phaseName.equals(DODGE_PHASE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEndPhase()
    {
        return phaseName.equals(END_PHASE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isJoinPhase()
    {
        return phaseName.equals(JOIN_PHASE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLootPhase()
    {
        return phaseName.equals(LOOT_PHASE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRpPhase()
    {
        return phaseName.equals(RP_PHASE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCreatePhase()
    {
        return phaseName.equals(CREATE_PHASE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFinalPhase()
    {
        return isLootPhase() || isEndPhase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInitiativePhase()
    {
        return isAttackPhase() || isDodgePhase();
    }
}
