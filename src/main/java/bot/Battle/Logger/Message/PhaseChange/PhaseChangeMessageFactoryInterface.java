package bot.Battle.Logger.Message.PhaseChange;

import bot.Battle.EncounterPhaseInterface;
import bot.MessageInterface;
import bot.Battle.PhaseChangeResult;
import org.jetbrains.annotations.NotNull;

interface PhaseChangeMessageFactoryInterface
{
    /**
     * Does this factory handle this type of phase change
     *
     * @param previousPhase Previous phase
     * @param nextPhase     Next phase
     *
     * @return boolean
     */
    boolean handles(
        @NotNull EncounterPhaseInterface previousPhase,
        @NotNull EncounterPhaseInterface nextPhase
    );

    /**
     * Create message
     *
     * @param result Phase change result
     */
    @NotNull MessageInterface createMessage(@NotNull PhaseChangeResult result);
}
