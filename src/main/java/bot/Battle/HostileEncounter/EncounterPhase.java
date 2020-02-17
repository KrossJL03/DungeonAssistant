package bot.Battle.HostileEncounter;

import bot.Battle.BattlePhase;
import org.jetbrains.annotations.NotNull;

public class EncounterPhase extends BattlePhase
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
     * Is dodge phase
     *
     * @return boolean
     */
    public boolean isDodgePhase()
    {
        return getPhaseName().equals(DODGE_PHASE);
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
     * Is loot phase
     *
     * @return boolean
     */
    public boolean isLootPhase()
    {
        return getPhaseName().equals(LOOT_PHASE);
    }

    /**
     * Is join phase
     *
     * @return bool
     */
    public boolean isRpPhase()
    {
        return getPhaseName().equals(RP_PHASE);
    }
}
