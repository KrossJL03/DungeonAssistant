package bot.Battle;

import bot.Explorer.Explorer;
import bot.Player.Player;
import bot.ProcessInterface;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public interface BattleInterface extends ProcessInterface
{
    /**
     * Attack action
     *
     * @param player      Player
     * @param hostileName Hostile name
     *
     * @throws BattlePhaseException If not attack phase
     * @throws NotYourTurnException If it is not the player's turn
     */
    void attackAction(@NotNull Player player, @NotNull String hostileName)
        throws BattlePhaseException, NotYourTurnException;

    /**
     * End the battle
     *
     * @param player Player attempting to forcibly end the battle
     */
    void endBattle(@NotNull Player player);

    /**
     * Get all creatures
     *
     * @return ArrayList
     */
    @NotNull ArrayList<CombatCreature> getAllCreatures();

    /**
     * Get all explorers
     *
     * @return ArrayList<EncounteredExplorer>
     */
    @NotNull ArrayList<CombatExplorer> getAllExplorers();

    /**
     * Get string value representing the type of battle
     *
     * @return String
     */
    @NotNull String getBattleStyle();

    /**
     * Is this a null encounter
     *
     * @return boolean
     */
    boolean isNull();

    /**
     * Is the encounter over
     *
     * @return boolean
     */
    boolean isOver();

    /**
     * Join encounter
     *
     * @param explorer Explorer
     * @param nickname Optional nickname
     *
     * @throws BattlePhaseException If encounter is over or has not started
     */
    void join(@NotNull Explorer explorer, @Nullable String nickname) throws BattlePhaseException;

    /**
     * Kick
     *
     * @param name Explorer name
     */
    void kick(@NotNull String name);

    /**
     * Leave encounter
     *
     * @param player Player
     */
    void leave(@NotNull Player player);

    /**
     * Log summary
     */
    void logSummary();

    /**
     * Player is rejoining
     *
     * @param player Player
     */
    void rejoin(@NotNull Player player);

    /**
     * Remove creature from encounter
     *
     * @param name Name of creature
     *
     * @throws BattlePhaseException If encounter is over
     */
    void remove(@NotNull String name) throws BattlePhaseException;

    /**
     * Set party size
     *
     * @param amount Max amount of players allowed for this encounter
     *
     * @throws BattlePhaseException If encounter is over
     */
    void setPartySize(int amount) throws BattlePhaseException;

    /**
     * Set tier
     *
     * @param tier Tier
     *
     * @throws BattlePhaseException If not create phase
     */
    void setTier(@NotNull Tier tier) throws BattlePhaseException;

    /**
     * Skip current player turn
     *
     * @throws BattlePhaseException If encounter is over
     *                              If not in initiative
     */
    void skipCurrentPlayerTurn() throws BattlePhaseException;

    /**
     * Start attack phase
     *
     * @throws EncounterException   If no players have joined
     * @throws BattlePhaseException If the encounter is over
     *                              If the encounter has not started
     *                              If attack phase is in progress
     */
    void startAttackPhase() throws BattlePhaseException;

    /**
     * Start join phase
     *
     * @param channel Channel encounter will be hosted in
     *
     * @throws BattlePhaseException If encounter is over
     *                              If encounter has already started
     *                              If max players has not beet set
     *                              If hostiles have not been added
     */
    void startJoinPhase(@NotNull MessageChannel channel) throws BattlePhaseException;

    /**
     * Use all current explorer actions
     */
    void useAllCurrentExplorerActions();

    /**
     * Use current explorer action
     */
    void useCurrentExplorerAction();

    /**
     * Use item action
     *
     * @param player Player who used the action
     */
    void useItemAction(Player player);
}
