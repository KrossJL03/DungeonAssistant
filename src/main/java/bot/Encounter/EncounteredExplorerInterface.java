package bot.Encounter;

import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface EncounteredExplorerInterface extends EncounterCreatureInterface
{
    /**
     * Add kill
     *
     * @param encounteredHostile Encountered hostile
     */
    void addKill(@NotNull EncounteredHostileInterface encounteredHostile);

    /**
     * Attack a target
     *
     * @param target Encountered creature that is being targeted by the attack
     *
     * @return AttackActionResultInterface
     */
    @NotNull AttackActionResultInterface attack(@NotNull EncounterCreatureInterface target);

    /**
     * Dodge encountered hostile attacks
     *
     * @param encounteredHostiles Encountered hostiles to dodge
     *
     * @return DodgeActionResultInterface
     */
    @NotNull DodgeActionResultInterface dodge(@NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles);

    /**
     * Automatically fail to dodge encountered hostile attacks
     *
     * @param encounteredHostiles Encountered hostiles to dodge
     *
     * @return DodgeActionResultInterface
     */
    @NotNull DodgeActionResultInterface failToDodge(@NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles);

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
     * Get stat
     *
     * @return int
     */
    int getStat(String statName);

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
     */
    void leave();

    /**
     * Modify stat
     *
     * @param statName  Name of stat to modify
     * @param statBoost Amount to boost stat by
     */
    void modifyStat(@NotNull String statName, int statBoost);

    /**
     * Protect recipient from encountered hostile attacks
     *
     * @param recipient           Encountered explorer being protected
     * @param encounteredHostiles Encountered hostiles to protect against
     *
     * @return ProtectActionResult
     */
    @NotNull ProtectActionResultInterface protect(
        @NotNull EncounteredExplorerInterface recipient,
        @NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles
    );

    /**
     * Rejoin encounter
     */
    void rejoin();

    /**
     * Reset actions
     *
     * @param setToMax Reset to max action count
     */
    void resetActions(boolean setToMax);

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
