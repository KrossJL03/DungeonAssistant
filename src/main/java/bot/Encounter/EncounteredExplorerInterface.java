package bot.Encounter;

import bot.Encounter.EncounteredCreature.EncounteredExplorerException;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.util.ArrayList;

public interface EncounteredExplorerInterface extends EncounteredCreatureInterface
{
    /**
     * Add creature as opponent when they have been fought long enough to qualify for loot
     *
     * @param opponent Opponent that was fought against
     *
     * @throws EncounteredExplorerException If explorer is not active
     */
    void addOpponent(@NotNull EncounteredCreatureInterface opponent) throws EncounteredExplorerException;

    /**
     * Attack a target
     *
     * @param target Encountered creature that is being targeted by the attack
     *
     * @return AttackActionResultInterface
     *
     * @throws EncounteredExplorerException If explorer has no actions
     */
    @NotNull AttackActionResultInterface attack(@NotNull EncounteredCreatureInterface target)
        throws EncounteredExplorerException;

    /**
     * Dodge encountered hostile attacks
     *
     * @param encounteredHostiles Encountered hostiles to dodge
     *
     * @return DodgeActionResultInterface
     *
     * @throws EncounteredExplorerException If explorer has no actions
     */
    @NotNull DodgeActionResultInterface dodge(@NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles)
        throws EncounteredExplorerException;

    /**
     * Automatically fail to dodge encountered hostile attacks
     *
     * @param encounteredHostiles Encountered hostiles to dodge
     *
     * @return DodgeActionResultInterface
     *
     * @throws EncounteredExplorerException If explorer has no actions
     */
    @NotNull DodgeActionResultInterface failToDodge(@NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles)
        throws EncounteredExplorerException;

    /**
     * If the creature is listed as an active opponent then save them as a kill for loot
     *
     * @param opponent Opponent to add to kills
     *
     * @throws EncounteredExplorerException If opponent is not slain
     */
    void finalizeKill(@NotNull EncounteredCreatureInterface opponent) throws EncounteredExplorerException;

    /**
     * Get agility
     *
     * @return int
     */
    int getAgility();

    /**
     * Get defense
     *
     * @return int
     */
    int getDefense();

    /**
     * Get dodge dice
     *
     * @return int
     */
    int getDodgeDice();

    /**
     * Get the time the explorer joined the battle
     *
     * @return ZonedDateTime
     */
    @NotNull ZonedDateTime getJoinedAt();

    /**
     * Get loot
     *
     * @return LootActionResultInterface
     */
    @NotNull LootActionResultInterface getLoot();

    /**
     * Get max actions
     *
     * @return int
     */
    int getMaxActions();

    /**
     * Get min crit
     *
     * @return int
     */
    int getMinCrit();

    /**
     * Get owner
     *
     * @return Player
     */
    @NotNull Player getOwner();

    /**
     * Get remaining actions
     *
     * @return int
     */
    int getRemainingActions();

    /**
     * Get stat points
     *
     * @return int
     */
    int getStatPoints();

    /**
     * Get strength
     *
     * @return int
     */
    int getStrength();

    /**
     * Get wisdom
     *
     * @return int
     */
    int getWisdom();

    /**
     * Give this explorer a protect action
     */
    void giveProtectAction();

    /**
     * Guard against encountered hostile attacks
     *
     * @param encounteredHostiles Encountered hostiles to guard against
     *
     * @return GuardActionResultInterface
     *
     * @throws EncounteredExplorerException If explorer has no actions
     */
    @NotNull GuardActionResultInterface guard(@NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles)
        throws EncounteredExplorerException;

    /**
     * Has actions
     *
     * @return boolean
     */
    boolean hasActions();

    /**
     * Is given player the owner of this encountered explorer
     *
     * @param player Player
     *
     * @return boolean
     */
    boolean isOwner(@NotNull Player player);

    /**
     * Is present in encounter
     *
     * @return boolean
     */
    boolean isPresent();

    /**
     * Leave the encounter
     *
     * @throws EncounteredExplorerException If explorer has already left
     */
    void leave() throws EncounteredExplorerException;

    /**
     * Protect recipient from encountered hostile attacks
     *
     * @param recipient           Encountered explorer being protected
     * @param encounteredHostiles Encountered hostiles to protect against
     *
     * @return ProtectActionResult
     *
     * @throws EncounteredExplorerException If explorer has no actions
     *                                      If explorer does not have a protect action available
     *                                      If explorer attempts to protect themselves
     *                                      If recipient is slain
     *                                      If recipient has no actions
     */
    @NotNull ProtectActionResultInterface protect(
        @NotNull EncounteredExplorerInterface recipient,
        @NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles
    ) throws EncounteredExplorerException;

    /**
     * Rejoin encounter
     *
     * @throws EncounteredExplorerException If explorer is already present
     */
    void rejoin() throws EncounteredExplorerException;

    /**
     * Remove opponent that is no longer eligible for loot
     *
     * @param opponent Opponent that was fought against
     */
    void removeOpponent(@NotNull EncounteredCreatureInterface opponent);

    /**
     * Reset actions
     *
     * @param singleAction Reset a single action only as opposed to all actions
     */
    void resetActions(boolean singleAction);

    /**
     * Roll loot for kills
     */
    void rollKillLoot();

    /**
     * Use one action
     */
    void useAction();

    /**
     * Use all actions
     */
    void useAllActions();
}
