package bot.Hostile;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Hostile
{
    private HashMap<Integer, Loot> lootList;
    private String                 species;
    private int                    attack;
    private int                    attackCount;
    private int                    dangerLevel;
    private int                    hitpoints;
    private int                    lootRollCount;
    private boolean                isViewable;

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
     * Get attack count
     *
     * @return int
     */
    public int getAttackCount()
    {
        return attackCount;
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
     * Get danger level
     *
     * @return int
     */
    public int getDangerLevel()
    {
        return dangerLevel;
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
     * Get amount of loot rolls
     *
     * @return int
     */
    public int getLootRollCount()
    {
        return lootRollCount;
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

    /**
     * Is hostile viewable
     *
     * @return boolean
     */
    public boolean isViewable()
    {
        return isViewable;
    }
}
