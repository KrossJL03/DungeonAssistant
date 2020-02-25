package bot.Battle.PlayerVsPlayer;

import bot.Battle.PhaseChangeMessageBuilder;
import bot.Battle.PhaseChangeMessageFactoryInterface;

import java.util.ArrayList;

class PvpPhaseChangeMessageBuilder extends PhaseChangeMessageBuilder
{
    /**
     * Constructor.
     */
    PvpPhaseChangeMessageBuilder()
    {
        super(
            new ArrayList<PhaseChangeMessageFactoryInterface>()
            {
                {
                    // order matters
                    add(new JoinPhaseStartMessageFactory());
                    add(new EndPhaseStartMessageFactory());
                    add(new AttackPhaseStartMessageFactory());
                    add(new VictoryPhaseStartMessageFactory());
                }
            }
        );
    }
}
