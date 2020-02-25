package bot.Battle.PlayerVsPlayer;

import bot.Battle.BattlePhaseChange;
import bot.Battle.BattlePhaseManager;
import org.jetbrains.annotations.NotNull;

class PvpPhaseManager extends BattlePhaseManager
{
    /**
     * Constructor.
     */
    PvpPhaseManager()
    {
        super(new PvpPhaseFactory());
    }

    /**
     * Start victory phase
     *
     * @return BattleChangePhase
     */
    @NotNull BattlePhaseChange startVictoryPhase()
    {
        assertNotFinalPhase();

        return changePhase(factory.createEndPhase());
    }
}
