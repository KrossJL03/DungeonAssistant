package bot.Encounter.EncounteredCreature;

class HitRoll
{

    private static int HIT_ROLL_FAIL = 1;
    private static int HIT_ROLL_MISS = 5;

    private int die;
    private int minCrit;
    private int roll;

    /**
     * HitRoll constructor
     *
     * @param roll    Roll
     * @param die     Die
     * @param minCrit Min crit
     */
    HitRoll(int roll, int die, int minCrit)
    {
        this.die = die;
        this.minCrit = minCrit;
        this.roll = roll;
    }

    /**
     * Get die
     *
     * @return int
     */
    int getDie()
    {
        return die;
    }

    /**
     * Get roll
     *
     * @return int
     */
    int getRoll()
    {
        return roll;
    }

    /**
     * Is roll a crit
     *
     * @return boolean
     */
    boolean isCrit()
    {
        return roll >= minCrit;
    }

    /**
     * Is roll a fail
     *
     * @return boolean
     */
    boolean isFail()
    {
        return roll <= HIT_ROLL_FAIL;
    }

    /**
     * Is roll a hit
     *
     * @return boolean
     */
    boolean isHit()
    {
        return roll > HIT_ROLL_MISS;
    }
}
