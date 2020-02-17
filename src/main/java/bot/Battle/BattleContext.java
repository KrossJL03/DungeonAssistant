package bot.Battle;

import org.jetbrains.annotations.NotNull;

class BattleContext
{
    private int     currentPartySize;
    private boolean isAlwaysJoinable;
    private int     maxPartySize;
    private String  type;

    /**
     * Constructor.
     *
     * @param type             Type of battle
     * @param isAlwaysJoinable Can players join the battle at any time
     * @param maxPartySize     Max amount of players that can participate
     * @param currentPartySize Current amount of players participating
     */
    BattleContext(@NotNull String type, boolean isAlwaysJoinable, int maxPartySize, int currentPartySize)
    {
        this.currentPartySize = currentPartySize;
        this.isAlwaysJoinable = isAlwaysJoinable;
        this.maxPartySize = maxPartySize;
        this.type = type;
    }

    /**
     * Get max amount of players that can participate
     */
    int getCurrentPartySize()
    {
        return currentPartySize;
    }

    /**
     * Get current amount of players participating
     */
    int getMaxPartySize()
    {
        return maxPartySize;
    }

    /**
     * Get battle type
     */
    @NotNull String getType()
    {
        return type;
    }

    /**
     * Can players join at any time
     */
    boolean isAlwaysJoinable()
    {
        return isAlwaysJoinable;
    }
}
