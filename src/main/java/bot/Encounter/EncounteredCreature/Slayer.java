package bot.Encounter.EncounteredCreature;

import bot.Encounter.EncounteredCreatureInterface;
import org.jetbrains.annotations.NotNull;

public class Slayer
{
    private String name;

    /**
     * Slayer constructor
     *
     * @param name Slayer name
     */
    @NotNull Slayer(String name)
    {
        this.name = name;
    }

    /**
     * Slayer constructor (empty)
     */
    @NotNull Slayer()
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
    boolean isSlayer(EncounteredCreatureInterface slayer)
    {
        return name.equals(slayer.getName());
    }
}
