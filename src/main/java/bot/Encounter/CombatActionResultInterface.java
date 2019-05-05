package bot.Encounter;

import bot.Encounter.EncounterData.Slayer;
import org.jetbrains.annotations.NotNull;

public interface CombatActionResultInterface
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
}
