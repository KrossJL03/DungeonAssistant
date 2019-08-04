package bot.Encounter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface GuardActionResultInterface extends CombatActionResultInterface
{
    /**
     * Get number of attacks
     *
     * @return int
     */
    int getAttackCount();

    /**
     * Get individual guard results
     *
     * @return ArrayList<GuardResultInterface>
     */
    @NotNull ArrayList<GuardResultInterface> getGuardResults();

    /**
     * Get defense stat of target
     *
     * @return int
     */
    int getTargetDefense();
}
