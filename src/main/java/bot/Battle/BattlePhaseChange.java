package bot.Battle;

import org.jetbrains.annotations.NotNull;

public class BattlePhaseChange
{
    private BattlePhase newPhase;
    private BattlePhase oldPhase;

    /**
     * Constructor.
     *
     * @param newPhase Next phase
     * @param oldPhase Previous phase
     */
    BattlePhaseChange(@NotNull BattlePhase newPhase, @NotNull BattlePhase oldPhase)
    {
        this.newPhase = newPhase;
        this.oldPhase = oldPhase;
    }

    /**
     * Get new phase
     *
     * @return BattlePhase
     */
    @NotNull BattlePhase getNewPhase()
    {
        return newPhase;
    }

    /**
     * Get old phase
     *
     * @return BattlePhase
     */
    @NotNull BattlePhase getOldPhase()
    {
        return oldPhase;
    }
}
