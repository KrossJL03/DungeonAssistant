package bot.Battle.Encounter;

import bot.Battle.BattleContext;
import bot.Battle.CombatCreature;
import bot.Battle.ExplorerRosterContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class EncounterContext extends BattleContext
{
    private HostileRosterContext hostileRosterContext;

    /**
     * Constructor.
     *
     * @param type                  Type of battle
     * @param isAlwaysJoinable      Can players join the battle at any time
     * @param explorerRosterContext Explorer roster context
     */
    EncounterContext(
        @NotNull String type,
        boolean isAlwaysJoinable,
        @NotNull ExplorerRosterContext explorerRosterContext,
        @NotNull HostileRosterContext hostileRosterContext
    )
    {
        super(type, isAlwaysJoinable, explorerRosterContext);
        this.hostileRosterContext = hostileRosterContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CombatCreature> getCreatures()
    {
        ArrayList<CombatCreature> creatures = new ArrayList<>();
        creatures.addAll(getExplorers());
        creatures.addAll(hostileRosterContext.getHostiles());

        return creatures;
    }
}
