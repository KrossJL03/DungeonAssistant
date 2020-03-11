package bot.Battle.Encounter;

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
                    add(new JoinPhaseStartMessageFactory());
                    add(new EndPhaseStartMessageFactory());
                    add(new LootPhaseStartMessageFactory());
                    add(new AttackPhaseStartMessageFactory());
                    add(new DodgePhaseStartMessageFactory());
                    add(new AttackPhaseEndMessageFactory());
                    add(new DodgePhaseEndMessageFactory());
                }
            }
        );
    }
}
