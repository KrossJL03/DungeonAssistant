package bot.Hostile;

import bot.Encounter.EncounteredCreature.LootRoll;
import bot.Encounter.LootRollInterface;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class Hostile
{
    private int                    attack;
    private int                    attackCount;
    private int                    dangerLevel;
    private int                    hitpoints;
    private boolean                isViewable;
    private HashMap<Integer, Loot> lootList;
    private int                    lootRollCount;
    private String                 species;

    /**
     * Constructor.
     *
     * @param species       Species
     * @param dangerLevel   Danger level
     * @param hitpoints     Hitpoints
     * @param attack        Attack die rolled for attacks
     * @param attackCount   Number of attacks per round
     * @param lootRollCount Number of loot die rolled
     * @param lootList      List of loot
     * @param isViewable    Is viewable
     */
    public Hostile(
        @NotNull String species,
        int dangerLevel,
        int hitpoints,
        int attack,
        int attackCount,
        int lootRollCount,
        @NotNull HashMap<Integer, Loot> lootList,
        boolean isViewable
    )
    {
        this.attackCount = attackCount;
        this.attack = attack;
        this.dangerLevel = dangerLevel;
        this.hitpoints = hitpoints;
        this.isViewable = isViewable;
        this.lootRollCount = lootRollCount;
        this.lootList = lootList;
        this.species = species;
    }

    /**
     * Get attack die
     *
     * @return int
     */
    public int getAttack()
    {
        return attack;
    }

    /**
     * Get hitpoints
     *
     * @return hitpoints
     */
    public int getHitpoints()
    {
        return hitpoints;
    }

    /**
     * Get loot by roll
     *
     * @param roll Roll
     *
     * @return Loot
     */
    public @NotNull Loot getLoot(int roll)
    {
        return lootList.get(roll);
    }

    /**
     * Get loot pool size
     *
     * @return int
     */
    public int getLootPoolSize()
    {
        return lootList.size();
    }

    /**
     * Get species name
     *
     * @return String
     */
    public @NotNull String getSpecies()
    {
        return species;
    }

    public @NotNull ArrayList<LootRollInterface> rollLoot()
    {
        ArrayList<LootRollInterface> lootRolls = new ArrayList<>();
        int                          lootDie   = getLootPoolSize();

        while (getLootRollCount() > lootRolls.size()) {
            int  roll = (int) Math.floor(Math.random() * lootDie) + 1;
            Loot loot = getLoot(roll);
            lootRolls.add(new LootRoll(species, loot, lootDie, roll));
        }

        return lootRolls;
    }

    /**
     * Get attack count
     *
     * @return int
     */
    int getAttackCount()
    {
        return attackCount;
    }

    /**
     * Get danger level
     *
     * @return int
     */
    int getDangerLevel()
    {
        return dangerLevel;
    }

    /**
     * Get amount of loot rolls
     *
     * @return int
     */
    int getLootRollCount()
    {
        return lootRollCount;
    }

    /**
     * Is hostile viewable
     *
     * @return boolean
     */
    boolean isViewable()
    {
        return isViewable;
    }
}
