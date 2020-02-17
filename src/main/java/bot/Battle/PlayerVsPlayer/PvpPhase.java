package bot.Battle.PlayerVsPlayer;

import bot.Battle.BattlePhase;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PvpPhase extends BattlePhase
{
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
}
