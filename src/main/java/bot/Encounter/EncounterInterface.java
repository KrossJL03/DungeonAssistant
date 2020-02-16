package bot.Encounter;

import bot.Explorer.Explorer;
import bot.Player.Player;
import bot.ProcessInterface;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public interface EncounterInterface extends ProcessInterface
{
    /**
     * Attack action
     *
     * @param player      Player
     * @param hostileName Hostile name
     *
     * @throws EncounterPhaseException If not attack phase
     * @throws NotYourTurnException    If it is not the player's turn
     */
    void attackAction(@NotNull Player player, @NotNull String hostileName)
        throws EncounterPhaseException, NotYourTurnException;

    /**
     * Get all explorers
     *
     * @return ArrayList<EncounteredHostileInterface>
     */
    @NotNull ArrayList<EncounteredExplorerInterface> getAllExplorers();

    /**
     * Get string value representing the type of encounter
     *
     * @return String
     */
    @NotNull String getEncounterType();

    /**
     * Heal encountered creature with given name by given amount of hitpoints
     *
     * @param name      Encountered creature name
     * @param hitpoints Hitpoints
     *
     * @throws EncounterPhaseException If encounter is over
     */
    void heal(@NotNull String name, int hitpoints) throws EncounterPhaseException;

    /**
     * Hurt encountered creature with given name by given amount of hitpoints
     *
     * @param name      Encountered creature name
     * @param hitpoints Hitpoints
     *
     * @throws EncounterPhaseException If encounter is over
     */
    void hurt(@NotNull String name, int hitpoints) throws EncounterPhaseException;

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
     * @throws EncounterPhaseException If encounter is over or has not started
     */
    void join(@NotNull Explorer explorer, @Nullable String nickname) throws EncounterPhaseException;

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
     * Manual command to make the current explorer protect a target. Heals current explorer by given hitpoints.
     *
     * @param targetName Name of target to protect
     * @param hitpoints  Hitpoints to heal
     */
    void manualProtectAction(@NotNull String targetName, int hitpoints) throws EncounterPhaseException;

    /**
     * Modify stat
     *
     * @param name         Name of creature to modify stat for
     * @param statName     Name of stat to modify
     * @param statModifier Modifier to apply to stat
     *
     * @throws EncounterPhaseException If the encounter is over
     */
    void modifyStat(
        @NotNull String name,
        @NotNull String statName,
        int statModifier
    ) throws EncounterPhaseException;

    /**
     * Player is rejoining
     *
     * @param player Player
     */
    void rejoin(@NotNull Player player);

    /**
     * Remove encountered creature from encounter
     *
     * @param name Name of creature
     *
     * @throws EncounterPhaseException If encounter is over
     */
    void removeCreature(@NotNull String name) throws EncounterPhaseException;

    /**
     * Set max player count
     *
     * @param maxPlayerCount Max amount of players allowed for this encounter
     *
     * @throws EncounterPhaseException If encounter is over
     */
    void setMaxPlayerCount(int maxPlayerCount) throws EncounterPhaseException;

    /**
     * Set stat
     *
     * @param name      Name of creature to modify stat for
     * @param statName  Name of stat to modify
     * @param statValue New stat value
     *
     * @throws EncounterPhaseException If the encounter is over
     */
    void setStat(
        @NotNull String name,
        @NotNull String statName,
        int statValue
    ) throws EncounterPhaseException;

    /**
     * Set tier
     *
     * @param tier Tier
     *
     * @throws EncounterPhaseException If not create phase
     */
    void setTier(@NotNull TierInterface tier) throws EncounterPhaseException;

    /**
     * Skip current player turn
     *
     * @throws EncounterPhaseException If encounter is over
     *                                 If not in initiative
     */
    void skipCurrentPlayerTurn() throws EncounterPhaseException;

    /**
     * Start attack phase
     *
     * @throws EncounterException      If no players have joined
     * @throws EncounterPhaseException If the encounter is over
     *                                 If the encounter has not started
     *                                 If attack phase is in progress
     */
    void startAttackPhase() throws EncounterPhaseException;

    /**
     * Start end phase on command
     */
    void startEndPhaseForced();

    /**
     * Start join phase
     *
     * @throws EncounterPhaseException If encounter is over
     *                                 If encounter has already started
     *                                 If max players has not beet set
     *                                 If hostiles have not been added
     */
    void startJoinPhase() throws EncounterPhaseException;

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
