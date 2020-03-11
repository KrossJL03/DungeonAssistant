package bot.Battle.Pvp;

import bot.Battle.BattlePhaseChangeResult;
import bot.Message;
import org.jetbrains.annotations.NotNull;

class JoinPhaseStartMessageFactory extends bot.Battle.JoinPhaseStartMessageFactory
{
    /**
     * Constructor.
     */
    JoinPhaseStartMessageFactory()
    {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addPostMessageText(@NotNull Message message, @NotNull BattlePhaseChangeResult result)
    {
        // do nothing
    }
}
