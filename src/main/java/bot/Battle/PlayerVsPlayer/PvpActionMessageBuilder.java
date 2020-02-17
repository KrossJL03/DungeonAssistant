package bot.Battle.PlayerVsPlayer;

import bot.Battle.ActionMessageBuilder;
import bot.Battle.ActionMessageFactory;
import bot.Battle.AttackActionMessageFactory;
import bot.Battle.HealActionMessageFactory;
import bot.Battle.HurtActionMessageFactory;
import bot.Battle.JoinActionMessageFactory;
import bot.Battle.ModifyStatActionMessageFactory;

import java.util.ArrayList;

class PvpActionMessageBuilder extends ActionMessageBuilder
{
    /**
     * Constructor.
     */
    PvpActionMessageBuilder()
    {
        super(
            new ArrayList<ActionMessageFactory>()
            {
                {
                    add(new AttackActionMessageFactory());
                    add(new HealActionMessageFactory());
                    add(new HurtActionMessageFactory());
                    add(new JoinActionMessageFactory());
                    add(new ModifyStatActionMessageFactory());
                }
            }
        );
    }
}
