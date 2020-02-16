package bot.Encounter;

import bot.Explorer.Explorer;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Null Object Pattern implementation of Encounter
 */
public class NullEncounter implements EncounterInterface
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void attackAction(@NotNull Player player, @NotNull String hostileName) throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<EncounteredExplorerInterface> getAllExplorers() throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getEncounterType() throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void heal(@NotNull String name, int hitpoints) throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void manualProtectAction(@NotNull String targetName, int hitpoints) throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hurt(@NotNull String name, int hitpoints) throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLockingDatabase()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNull()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOver()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void join(@NotNull Explorer explorer, @Nullable String nickname) throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void kick(@NotNull String name) throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void leave(@NotNull Player player) throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyStat(@NotNull String name, @NotNull String statName, int statModifier)
        throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rejoin(@NotNull Player player) throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCreature(@NotNull String name) throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMaxPlayerCount(int maxPlayerCount) throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStat(@NotNull String name, @NotNull String statName, int statValue) throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTier(@NotNull TierInterface tier) throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void skipCurrentPlayerTurn() throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startAttackPhase() throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startEndPhaseForced() throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startJoinPhase() throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAllCurrentExplorerActions() throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useCurrentExplorerAction() throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useItemAction(Player player)
    {
        throw EncounterException.createNullEncounter();
    }
}
