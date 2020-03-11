package bot.Battle.Encounter;

import bot.Battle.BattlePhase;
import org.jetbrains.annotations.NotNull;

class EncounterPhase extends BattlePhase
{
    final static String DODGE_PHASE = "DODGE";
    final static String LOOT_PHASE  = "LOOT";
    final static String RP_PHASE    = "RP";

    /**
     * Constructor.
     *
     * @param phaseName Phase name
     */
    EncounterPhase(@NotNull String phaseName)
    {
        super(phaseName);
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

    /**
     * Is dodge phase
     *
     * @return boolean
     */
    boolean isDodgePhase()
    {
        return getPhaseName().equals(DODGE_PHASE);
    }

    /**
     * Is loot phase
     *
     * @return boolean
     */
    boolean isLootPhase()
    {
        return getPhaseName().equals(LOOT_PHASE);
    }

    /**
     * Is join phase
     *
     * @return bool
     */
    boolean isRpPhase()
    {
        return getPhaseName().equals(RP_PHASE);
    }
}
