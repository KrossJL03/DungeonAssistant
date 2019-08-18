package bot.Encounter;

import bot.Encounter.Logger.Mention;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface LootActionResultInterface extends ActionResultInterface
{

    /**
     * Get bonus for all final bows
     *
     * @return int
     */
    int getFinalBlowBonus();

    /**
     * Get list of names of final blows
     *
     * @return ArrayList
     */
    @NotNull
    ArrayList<String> getFinalBlowNames();

    /**
     * Get kill count
     *
     * @return int
     */
    int getKillCount();

    /**
     * Get loot roll count
     *
     * @return int
     */
    int getLootRollCount();

    /**
     * Get individual loot rolls
     *
     * @return ArrayList
     */
    ArrayList<LootRollInterface> getLootRolls();

    /**
     * Get mention
     *
     * @return Mention
     */
    Mention getMention();

    /**
     * Get name
     *
     * @return String
     */
    String getName();

    /**
     * Has any final blows
     *
     * @return boolean
     */
    boolean hasFinalBlows();

    /**
     * Has no loot
     *
     * @return boolean
     */
    boolean noLoot();
}
