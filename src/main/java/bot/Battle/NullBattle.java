package bot.Battle;

import bot.Explorer.Explorer;
import bot.Player.Player;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Null Object Pattern implementation of Battle
 */
public class NullBattle implements BattleInterface
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
    public void endBattle(@NotNull Player player) throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CombatCreature> getAllCreatures()
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CombatExplorer> getAllExplorers() throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getBattleStyle() throws EncounterException
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
    public void logSummary()
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
    public void remove(@NotNull String name) throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPartySize(int amount) throws EncounterException
    {
        throw EncounterException.createNullEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTier(@NotNull Tier tier) throws EncounterException
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
    public void startJoinPhase(@NotNull MessageChannel channel) throws EncounterException
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
