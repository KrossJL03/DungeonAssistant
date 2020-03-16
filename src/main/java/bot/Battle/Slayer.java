package bot.Battle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.ZonedDateTime;

public class Slayer
{
    private String        name;
    private ZonedDateTime slainAt;

    /**
     * Slayer constructor
     *
     * @param name Slayer name
     */
    public Slayer(@NotNull String name)
    {
        this.name = name;
        this.slainAt = ZonedDateTime.now();
    }

    /**
     * Slayer constructor (empty)
     */

    public Slayer()
    {
        this.name = "";
        this.slainAt = null;
    }

    /**
     * Does a slayer exist
     *
     * @return boolean
     */
    public boolean exists()
    {
        return !name.isEmpty();
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
     * Get slain at
     *
     * @return ZonedDateTime
     */
    @Nullable
    public ZonedDateTime getSlainAt()
    {
        return slainAt;
    }

    /**
     * Does the supplied potential slayer match this slayer
     *
     * @param slayer Potential slayer
     *
     * @return boolean
     */
    public boolean isSlayer(@NotNull CombatCreature slayer)
    {
        return name.equals(slayer.getName());
    }

    /**
     * Does the supplied potential slayer match this slayer
     *
     * @param slayer Potential slayer
     *
     * @return boolean
     */
    public boolean isSlayer(@NotNull Slayer slayer)
    {
        return name.equals(slayer.getName());
    }
}
