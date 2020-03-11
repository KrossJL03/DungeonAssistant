package bot.Battle;

import bot.Battle.Encounter.Encounter;
import bot.Battle.PlayerVsPlayer.PlayerVsPlayer;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

public class EncounterHolder
{
    private BattleInterface encounter;

    /**
     * Constructor.
     */
    public EncounterHolder()
    {
        this.encounter = new NullBattle();
    }

    /**
     * Create encounter
     *
     * @param channel Channel to set for logger
     * @param dmId    Dungeon master id
     */
    public void createEncounter(@NotNull MessageChannel channel, @NotNull String dmId)
    {
        encounter = new Encounter(channel, Mention.createForRole(dmId));
    }
    /**
     * Is there a battle currently in progress
     *
     * @return boolean
     */
    public boolean hasActiveBattle()
    {
        return !encounter.isNull() && !encounter.isOver();
    }

    /**
     * Create pvp
     *
     * @param channel Channel to set for logger
     * @param dmId    Dungeon master id
     */
    public void createPvp(@NotNull MessageChannel channel, @NotNull String dmId)
    {
        encounter = new PlayerVsPlayer(channel, Mention.createForRole(dmId));
    }

    /**
     * Get encounter
     *
     * @return BattleInterface
     */
    public @NotNull BattleInterface getBattle()
    {
        return encounter;
    }
}
