package bot.Battle.PlayerVsPlayer;

import bot.Battle.BattlePhase;
import org.jetbrains.annotations.NotNull;

public class PvpPhase extends BattlePhase
{
    private static String VICTORY_PHASE = "VICTORY";

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
