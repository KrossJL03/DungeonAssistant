package bot.Battle.HostileEncounter;

import bot.Battle.ActionMessageBuilder;
import bot.Battle.ActionMessageFactory;
import bot.Battle.AttackActionMessageFactory;
import bot.Battle.HealActionMessageFactory;
import bot.Battle.HurtActionMessageFactory;
import bot.Battle.JoinActionMessageFactory;
import bot.Battle.ModifyStatActionMessageFactory;

import java.util.ArrayList;

class EncounterActionMessageBuilder extends ActionMessageBuilder
{
    /**
     * Constructor.
     */
    EncounterActionMessageBuilder()
    {
        super(
            new ArrayList<ActionMessageFactory>()
            {
                {
                    new AttackActionMessageFactory();
                    new DodgeActionMessageFactory();
                    new GuardActionMessageFactory();
                    new HealActionMessageFactory();
                    new HurtActionMessageFactory();
                    new JoinActionMessageFactory();
                    new LootActionMessageFactory();
                    new ModifyStatActionMessageFactory();
                    new ProtectActionMessageFactory();
                }
            }
        );
    }
}
