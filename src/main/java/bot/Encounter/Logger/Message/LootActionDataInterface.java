package bot.Encounter.Logger.Message;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface LootActionDataInterface {

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
    ArrayList<String> getFinalBlows();

    /**
     * Get kill count
     *
     * @return int
     */
    int getKillCount();

    /**
     * Get loot die
     *
     * @return int
     */
    int getLootDie();

    /**
     * Get mention
     *
     * @return String
     */
    String getMention();

    /**
     * Get name
     *
     * @return String
     */
    String getName();

    /**
     * Get individual loot actions
     *
     * @return ArrayList
     */
    ArrayList<LootSubActionDataInterface> getSubActions();

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
