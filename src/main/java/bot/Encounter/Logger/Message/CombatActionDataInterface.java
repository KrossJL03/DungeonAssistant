package bot.Encounter.Logger.Message;

import org.jetbrains.annotations.NotNull;

interface CombatActionDataInterface {

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
     * Is target slain
     *
     * @return boolean
     */
    boolean isTargetSlain();
}
