package bot.Encounter.Logger.MessageBuilder;

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
