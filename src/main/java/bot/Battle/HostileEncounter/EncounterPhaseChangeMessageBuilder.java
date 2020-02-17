package bot.Battle.HostileEncounter;

import bot.Battle.AttackPhaseStartMessageFactory;
import bot.Battle.EndPhaseStartMessageFactory;
import bot.Battle.JoinPhaseStartMessageFactory;
import bot.Battle.PhaseChangeMessageBuilder;
import bot.Battle.PhaseChangeMessageFactoryInterface;

import java.util.ArrayList;

class EncounterPhaseChangeMessageBuilder extends PhaseChangeMessageBuilder
{
    /**
     * Constructor.
     */
    EncounterPhaseChangeMessageBuilder()
    {
        super(
            new ArrayList<PhaseChangeMessageFactoryInterface>()
            {
                {
                    // order matters
                    new JoinPhaseStartMessageFactory();
                    new EndPhaseStartMessageFactory();
                    new LootPhaseStartMessageFactory();
                    new AttackPhaseStartMessageFactory();
                    new DodgePhaseStartMessageFactory();
                    new AttackPhaseEndMessageFactory();
                    new DodgePhaseEndMessageFactory();
                }
            }
        );
    }
}
