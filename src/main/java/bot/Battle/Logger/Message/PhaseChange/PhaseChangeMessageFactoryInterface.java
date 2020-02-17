package bot.Battle.Logger.Message.PhaseChange;

import bot.Battle.BattlePhase;
import bot.Battle.BattlePhaseChangeResult;
import bot.MessageInterface;
import org.jetbrains.annotations.NotNull;

interface PhaseChangeMessageFactoryInterface
{
    /**
     * Create message
     *
     * @param result Phase change result
     */
    @NotNull MessageInterface createMessage(@NotNull BattlePhaseChangeResult result);

    /**
     * Does this factory handle this type of phase change
     *
     * @param previousPhase Previous phase
     * @param nextPhase     Next phase
     *
     * @return boolean
     */
    boolean handles(
        @NotNull BattlePhase previousPhase,
        @NotNull BattlePhase nextPhase
    );
}
