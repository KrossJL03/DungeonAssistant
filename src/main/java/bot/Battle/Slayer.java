package bot.Battle;

import org.jetbrains.annotations.NotNull;

public class Slayer
{
    private String name;

    /**
     * Slayer constructor
     *
     * @param name Slayer name
     */
    @NotNull
    public Slayer(String name)
    {
        this.name = name;
    }

    /**
     * Slayer constructor (empty)
     */
    @NotNull
    public Slayer()
    {
        this.name = "";
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
     * Does the supplied potential slayer match this slayer
     *
     * @param slayer Potential slayer
     *
     * @return boolean
     */
    public boolean isSlayer(Slayer slayer)
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
    public boolean isSlayer(CombatCreature slayer)
    {
        return name.equals(slayer.getName());
    }
}
