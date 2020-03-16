package bot.Battle.Pvp;

import bot.Battle.BattlePhase;
import org.jetbrains.annotations.NotNull;

class PvpPhase extends BattlePhase
{
    static String VICTORY_PHASE = "VICTORY";

    /**
     * Constructor.
     *
     * @param phaseName Phase name
     */
    PvpPhase(@NotNull String phaseName)
    {
        super(phaseName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFinalPhase()
    {
        return isEndPhase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInitiativePhase()
    {
        return isAttackPhase();
    }

    /**
     * Is victory phase
     */
    boolean isVictoryPhase()
    {
        return getPhaseName().equals(VICTORY_PHASE);
    }
}