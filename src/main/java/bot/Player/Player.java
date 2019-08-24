package bot.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Player
{
    private String  userId;
    private String  name;
    private int     cumulus;
    private boolean isMod;

    /**
     * Constructor.
     *
     * @param userId  User id
     * @param name    Server nickname
     * @param cumulus Cumulus
     * @param isMod   Is mod
     */
    public Player(@NotNull String userId, @NotNull String name, int cumulus, boolean isMod)
    {
        this.cumulus = cumulus;
        this.isMod = isMod;
        this.name = name;
        this.userId = userId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable Object o)
    {
        if (o instanceof Player) {
            return ((Player) o).getUserId().equals(userId);
        }
        return super.equals(o);
    }

    /**
     * Get name
     *
     * @return String
     */
    public @NotNull String getName()
    {
        return name;
    }

    /**
     * Get user id
     *
     * @return String
     */
    public @NotNull String getUserId()
    {
        return userId;
    }

    /**
     * Is this player a mod
     *
     * @return boolean
     */
    public boolean isMod()
    {
        return isMod;
    }

    /**
     * Are these players the same player
     *
     * @param player Player to compare
     *
     * @return boolean
     */
    public boolean isSamePlayer(@NotNull Player player)
    {
        return userId.equals(player.getUserId());
    }
}
