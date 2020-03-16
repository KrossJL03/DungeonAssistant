package bot.Battle.Pvp;

import bot.Battle.BattleContext;
import bot.Battle.CombatCreature;
import bot.Battle.ExplorerRosterContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class PvpContext extends BattleContext
{
    /**
     * Constructor.
     *
     * @param type                  Type of battle
     * @param isAlwaysJoinable      Can players join the battle at any time
     * @param explorerRosterContext Explorer roster context
     */
    PvpContext(
        @NotNull String type,
        boolean isAlwaysJoinable,
        @NotNull ExplorerRosterContext explorerRosterContext
    )
    {
        super(type, isAlwaysJoinable, explorerRosterContext);
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public @NotNull ArrayList<CombatCreature> getCreatures()
    {
        return new ArrayList<>(getExplorers());
    }
}
