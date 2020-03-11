package bot.Battle.Encounter;

import bot.Battle.ActionMessageBuilder;
import bot.Battle.ActionMessageFactory;
import bot.Battle.AttackActionMessageFactory;
import bot.Battle.JoinActionMessageFactory;

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
                    add(new AttackActionMessageFactory());
                    add(new DodgeActionMessageFactory());
                    add(new GuardActionMessageFactory());
                    add(new HealActionMessageFactory());
                    add(new HurtActionMessageFactory());
                    add(new JoinActionMessageFactory());
                    add(new LootActionMessageFactory());
                    add(new ModifyStatActionMessageFactory());
                    add(new ProtectActionMessageFactory());
                }
            }
        );
    }
}
