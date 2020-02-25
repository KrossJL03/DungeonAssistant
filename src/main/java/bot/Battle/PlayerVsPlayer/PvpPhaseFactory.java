package bot.Battle.PlayerVsPlayer;

import bot.Battle.BattlePhaseFactoryInterface;
import org.jetbrains.annotations.NotNull;

public class PvpPhaseFactory implements BattlePhaseFactoryInterface
{
    /**
     * Phase constructor (ATTACK_PHASE)
     *
     * @return PvpPhase
     */
    public @NotNull PvpPhase createAttackPhase()
    {
        return new PvpPhase(PvpPhase.ATTACK_PHASE);
    }

    /**
     * Phase constructor (CREATE_PHASE)
     *
     * @return EncounterPhase
     */
    public @NotNull PvpPhase createCreatePhase()
    {
        return new PvpPhase(PvpPhase.CREATE_PHASE);
    }

    /**
     * Phase constructor (END_PHASE)
     *
     * @return PvpPhase
     */
    public @NotNull PvpPhase createEndPhase()
    {
        return new PvpPhase(PvpPhase.END_PHASE);
    }

    /**
     * Phase constructor (JOIN_PHASE)
     *
     * @return PvpPhase
     */
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
        return new PvpPhase(PvpPhase.VICTORY_PHASE);
    }
}
