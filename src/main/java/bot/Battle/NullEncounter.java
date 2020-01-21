package bot.Battle;

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
    public void attackAction(
        @NotNull Player player, @NotNull String hostileName
    ) throws EncounterPhaseException, NotYourTurnException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<EncounteredExplorerInterface> getAllExplorers()
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getEncounterType()
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void heal(@NotNull String name, int hitpoints) throws EncounterPhaseException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hurt(@NotNull String name, int hitpoints) throws EncounterPhaseException
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
    public void join(@NotNull Explorer explorer, @Nullable String nickname) throws EncounterPhaseException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void kick(@NotNull String name)
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void leave(@NotNull Player player)
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyStat(@NotNull String name, @NotNull String statName, int statModifier)
        throws EncounterPhaseException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rejoin(@NotNull Player player)
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCreature(@NotNull String name) throws EncounterPhaseException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMaxPlayerCount(int maxPlayerCount) throws EncounterPhaseException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStat(@NotNull String name, @NotNull String statName, int statValue) throws EncounterPhaseException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void skipCurrentPlayerTurn() throws EncounterPhaseException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startAttackPhase() throws EncounterPhaseException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startEndPhaseForced()
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startJoinPhase() throws EncounterPhaseException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTier(@NotNull TierInterface tier) throws EncounterPhaseException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAllCurrentExplorerActions()
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useCurrentExplorerAction()
    {
        throw EncounterException.createNullEncounter();
    }
}
