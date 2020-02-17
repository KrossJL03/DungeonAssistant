package bot.Battle;

import org.jetbrains.annotations.NotNull;

public interface BattlePhaseFactoryInterface
{
    /**
     * Phase constructor (ATTACK_PHASE)
     *
     * @return EncounterPhase
     */
    @NotNull BattlePhase createAttackPhase();

    /**
     * Phase constructor (CREATE_PHASE)
     *
     * @return BattlePhase
     */
    @NotNull BattlePhase createCreatePhase();

    /**
     * Phase constructor (END_PHASE)
     *
     * @return BattlePhase
     */
    @NotNull BattlePhase createEndPhase();

    /**
     * Phase constructor (JOIN_PHASE)
     *
     * @return BattlePhase
     */
    @NotNull BattlePhase createJoinPhase();
}
