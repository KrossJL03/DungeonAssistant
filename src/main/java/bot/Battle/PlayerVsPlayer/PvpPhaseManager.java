package bot.Battle.PlayerVsPlayer;

import bot.Battle.BattlePhaseManager;

class PvpPhaseManager extends BattlePhaseManager
{
    /**
     * Constructor.
     */
    PvpPhaseManager()
    {
        super(new PvpPhaseFactory());
    }
}
