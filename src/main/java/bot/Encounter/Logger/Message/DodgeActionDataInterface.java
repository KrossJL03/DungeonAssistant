package bot.Encounter.Logger.Message;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface DodgeActionDataInterface extends CombatActionDataInterface {

    /**
     * Get number of attacks
     *
     * @return int
     */
    int getAttackCount();

    /**
     * Get total damage dealt to the target
     *
     * @return int
     */
    int getDamage();

    /**
     * Get target dodge die
     *
     * @return int
     */
    int getDodgeDie();

    /**
     * Get minimum roll needed for a successful dodge
     *
     * @return int
     */
    int getMinSucessDodgeRoll();

    /**
     * Get total damage resisted by the target
     *
     * @return int
     */
    int getResistedDamage();

    /**
     * Get sub action data
     *
     * @return ArrayList<DodgeSubActionDataInterface>
     */
    @NotNull
    ArrayList<DodgeSubActionDataInterface> getSubActionData();

    /**
     * Was this action a forced fail
     *
     * @return boolean
     */
    boolean isForceFail();
}
