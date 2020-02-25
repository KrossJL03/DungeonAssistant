package bot.Battle.PlayerVsPlayer;

import bot.Battle.ActionMessageBuilder;
import bot.Battle.ActionMessageFactory;
import bot.Battle.AttackActionMessageFactory;
import bot.Battle.JoinActionMessageFactory;

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
                    add(new JoinActionMessageFactory());
                }
            }
        );
    }
}
