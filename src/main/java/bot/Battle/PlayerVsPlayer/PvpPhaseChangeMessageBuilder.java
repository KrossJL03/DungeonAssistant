package bot.Battle.PlayerVsPlayer;

import bot.Battle.AttackPhaseStartMessageFactory;
import bot.Battle.EndPhaseStartMessageFactory;
import bot.Battle.JoinPhaseStartMessageFactory;
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
                    new JoinPhaseStartMessageFactory();
                    new EndPhaseStartMessageFactory();
                    new AttackPhaseStartMessageFactory();
                }
            }
        );
    }
}
