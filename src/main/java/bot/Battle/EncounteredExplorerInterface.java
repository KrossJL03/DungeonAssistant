package bot.Battle;

import bot.Battle.EncounteredCreature.AttackActionResult;
import bot.Battle.EncounteredCreature.DodgeActionResult;
import bot.Battle.EncounteredCreature.EncounteredExplorerException;
import bot.Battle.EncounteredCreature.GuardActionResult;
import bot.Battle.EncounteredCreature.LootActionResult;
import bot.Battle.EncounteredCreature.ProtectActionResult;
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
     * @return AttackActionResult
     *
     * @throws EncounteredExplorerException If explorer has no actions
     */
    @NotNull AttackActionResult attack(@NotNull EncounteredCreatureInterface target)
        throws EncounteredExplorerException;

    /**
     * Compare to
     *
     * @param encounteredExplorer Encountered explorer to compare
     *
     * @return int
     */
    int compareTo(@NotNull EncounteredExplorerInterface encounteredExplorer);

    /**
     * Dodge encountered hostile attacks
     *
     * @param encounteredHostiles Encountered hostiles to dodge
     *
     * @return DodgeActionResult
     *
     * @throws EncounteredExplorerException If explorer has no actions
     */
    @NotNull DodgeActionResult dodge(@NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles)
        throws EncounteredExplorerException;

    /**
     * Automatically fail to dodge encountered hostile attacks
     *
     * @param encounteredHostiles Encountered hostiles to dodge
     *
     * @return DodgeActionResult
     *
     * @throws EncounteredExplorerException If explorer has no actions
     */
    @NotNull DodgeActionResult failToDodge(@NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles)
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
     * @return LootActionResult
     */
    @NotNull LootActionResult getLoot();

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
     * @return GuardActionResult
     *
     * @throws EncounteredExplorerException If explorer has no actions
     */
    @NotNull GuardActionResult guard(@NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles)
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
    void markAsNotPresent() throws EncounteredExplorerException;

    /**
     * Rejoin encounter
     *
     * @throws EncounteredExplorerException If explorer is already present
     */
    void markAsPresent() throws EncounteredExplorerException;

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
    @NotNull ProtectActionResult protect(
        @NotNull EncounteredExplorerInterface recipient,
        @NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles
    ) throws EncounteredExplorerException;

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
