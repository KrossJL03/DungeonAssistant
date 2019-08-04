package bot.Encounter;

import bot.Encounter.EncounteredCreature.EncounteredExplorerException;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface EncounteredExplorerInterface extends EncounteredCreatureInterface
{
    /**
     * Add kill
     *
     * @param encounterCreature Encountered creature
     *
     * @throws EncounteredExplorerException If explorer was not active for kill
     */
    void addKill(@NotNull EncounteredCreatureInterface encounterCreature) throws EncounteredExplorerException;

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
     * Reset actions
     */
    void resetActions();

    /**
     * Roll loot
     */
    void rollLoot();

    /**
     * Use one action
     */
    void useAction();

    /**
     * Use all actions
     */
    void useAllActions();

    /**
     * Compare to
     *
     * @param encounteredExplorer Encountered explorer to compare
     *
     * @return int
     */
    int compareTo(@NotNull EncounteredExplorerInterface encounteredExplorer);
}
