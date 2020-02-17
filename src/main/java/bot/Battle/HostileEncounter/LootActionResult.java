package bot.Battle.HostileEncounter;

import bot.Battle.ActionResultInterface;
import bot.Battle.CombatCreature;
import bot.Battle.Logger.Mention;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LootActionResult implements ActionResultInterface
{
    private int                       finalBlowBonus;
    private ArrayList<CombatCreature> finalBlows;
    private boolean                   isRolled;
    private int                       killCount;
    private ArrayList<LootRoll>       lootRolls;
    private String                    name;
    private Player                    owner;

    /**
     * Empty constructor.
     *
     * @param name  Name
     * @param owner Owner
     */
    @NotNull LootActionResult(@NotNull String name, @NotNull Player owner)
    {
        this.finalBlowBonus = 0;
        this.finalBlows = new ArrayList<>();
        this.isRolled = false;
        this.killCount = 0;
        this.lootRolls = new ArrayList<>();
        this.name = name;
        this.owner = owner;
    }

    /**
     * Constructor.
     *
     * @param name           Name
     * @param owner          Owner
     * @param lootRolls      Loot rolls
     * @param finalBlows     Kills
     * @param killCount      Number of kills present for
     * @param finalBlowBonus Bonus cumulus for final blows
     */
    @NotNull LootActionResult(
        @NotNull String name,
        @NotNull Player owner,
        @NotNull ArrayList<LootRoll> lootRolls,
        @NotNull ArrayList<CombatCreature> finalBlows,
        int killCount,
        int finalBlowBonus
    )
    {
        this.finalBlowBonus = finalBlowBonus;
        this.finalBlows = finalBlows;
        this.isRolled = true;
        this.killCount = killCount;
        this.lootRolls = lootRolls;
        this.name = name;
        this.owner = owner;
    }

    /**
     * Get bonus for all final bows
     *
     * @return int
     */
    public int getFinalBlowBonus()
    {
        return finalBlowBonus;
    }

    /**
     * Get list of names of final blows
     *
     * @return ArrayList
     */
    public @NotNull ArrayList<String> getFinalBlowNames()
    {
        ArrayList<String> finalBlowNames = new ArrayList<>();
        for (CombatCreature finalBlow : finalBlows) {
            finalBlowNames.add(finalBlow.getName());
        }

        return finalBlowNames;
    }

    /**
     * Get kill count
     *
     * @return int
     */
    public int getKillCount()
    {
        return killCount;
    }

    /**
     * Get loot roll count
     *
     * @return int
     */
    public int getLootRollCount()
    {
        return lootRolls.size();
    }

    /**
     * Get individual loot rolls
     *
     * @return ArrayList
     */
    public ArrayList<LootRoll> getLootRolls()
    {
        return lootRolls;
    }

    /**
     * Get mention
     *
     * @return Mention
     */
    public @NotNull Mention getMention()
    {
        return Mention.createForPlayer(owner.getUserId());
    }

    /**
     * Get name
     *
     * @return String
     */
    public @NotNull String getName()
    {
        return name;
    }

    /**
     * Has any final blows
     *
     * @return boolean
     */
    public boolean hasFinalBlows()
    {
        return finalBlows.size() > 0;
    }

    /**
     * Has no loot
     *
     * @return boolean
     */
    public boolean noLoot()
    {
        return !isRolled;
    }
}
