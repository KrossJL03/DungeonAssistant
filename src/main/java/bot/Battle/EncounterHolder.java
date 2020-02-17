package bot.Battle;

import bot.Battle.HostileEncounter.HostileEncounter;
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
     * Create hostile encounter
     *
     * @param channel Channel to set for logger
     * @param dmId    Dungeon master id
     */
    public void createHostileEncounter(@NotNull MessageChannel channel, @NotNull String dmId)
    {
        encounter = new HostileEncounter(channel, Mention.createForRole(dmId));
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
    public @NotNull BattleInterface getEncounter()
    {
        return encounter;
    }
}
