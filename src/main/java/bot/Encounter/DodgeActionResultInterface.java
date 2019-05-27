package bot.Encounter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface DodgeActionResultInterface extends CombatActionResultInterface
{
    /**
     * Get number of attacks
     *
     * @return int
     */
    int getAttackCount();

    /**
     * Get individual dodge results
     *
     * @return ArrayList<DodgeResultInterface>
     */
    @NotNull ArrayList<DodgeResultInterface> getDodgeResults();

    /**
     * Get minimum roll needed for a successful dodge
     *
     * @return int
     */
    int getMinSucessDodgeRoll();

    /**
     * Get target dodge die
     *
     * @return int
     */
    int getTargetDodgeDie();

    /**
     * Was this action a forced fail
     *
     * @return boolean
     */
    boolean isForceFail();
}
