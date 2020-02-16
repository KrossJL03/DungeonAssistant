package bot.Encounter;

import bot.Encounter.EncounteredCreature.Slayer;
import org.jetbrains.annotations.NotNull;

public interface CombatActionResultInterface extends ActionResultInterface
{
    /**
     * Get damage dealt to target
     *
     * @return int
     */
    int getDamageDealt();

    /**
     * Get damage resisted by the target
     *
     * @return int
     */
    int getDamageResisted();

    /**
     * Get death min save roll
     *
     * @return int
     */
    int getDeathMinSaveRoll();

    /**
     * Get death save die
     *
     * @return int
     */
    int getDeathSaveDie();

    /**
     * Get death save roll
     *
     * @return int
     */
    int getDeathSaveRoll();

    /**
     * Get target current hitpoints
     *
     * @return int
     */
    int getTargetCurrentHp();

    /**
     * Get target max hitpoints
     *
     * @return int
     */
    int getTargetMaxHp();

    /**
     * Get target name
     *
     * @return String
     */
    @NotNull
    String getTargetName();

    /**
     * Get target slayer
     *
     * @return Slayer
     */
    @NotNull
    Slayer getTargetSlayer();

    /**
     * Is the target an explorer
     *
     * @return boolean
     */
    boolean isTargetExplorer();

    /**
     * Is target slain
     *
     * @return boolean
     */
    boolean isTargetSlain();

    /**
     * Did the target roll a death save
     *
     * @return boolean
     */
    boolean rolledDeathSave();

    /**
     * Did the target survive a death save
     *
     * @return boolean
     */
    boolean survivedDeathSave();
}
