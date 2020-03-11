package bot.Battle.Pvp;

import bot.Battle.BattlePhaseFactoryInterface;
import org.jetbrains.annotations.NotNull;

class PvpPhaseFactory implements BattlePhaseFactoryInterface
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PvpPhase createAttackPhase()
    {
        return new PvpPhase(PvpPhase.ATTACK_PHASE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PvpPhase createCreatePhase()
    {
        return new PvpPhase(PvpPhase.CREATE_PHASE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PvpPhase createEndPhase()
    {
        return new PvpPhase(PvpPhase.END_PHASE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PvpPhase createJoinPhase()
    {
        return new PvpPhase(PvpPhase.JOIN_PHASE);
    }

    /**
     * Phase constructor (VICTORY_PHASE)
     *
     * @return PvpPhase
     */
    public @NotNull PvpPhase createVictoryPhase()
    {
        // todo
        return new PvpPhase(PvpPhase.VICTORY_PHASE);
    }
}
