package bot.Battle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NullHostileRoster implements HostileRosterInterface
{
    /**
     * {@inheritDoc}
     */
    public void addHostile(@NotNull EncounteredHostileInterface newHostile)
    {
        throw HostileRosterException.createNullRoster();
    }

    /**
     * {@inheritDoc}
     */
    public @NotNull ArrayList<EncounteredHostileInterface> getActiveHostiles()
    {
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    public @NotNull ArrayList<EncounteredHostileInterface> getAllHostiles()
    {
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull EncounteredHostileInterface getHostile(@NotNull String name)
    {
        throw HostileRosterException.createNullRoster();
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasActiveHostiles()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isNull()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void remove(@NotNull EncounteredHostileInterface hostile) throws EncounteredCreatureNotFoundException
    {
        throw HostileRosterException.createNullRoster();
    }
}
