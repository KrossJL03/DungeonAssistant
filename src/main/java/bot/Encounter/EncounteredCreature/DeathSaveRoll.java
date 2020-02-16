package bot.Encounter.EncounteredCreature;

class DeathSaveRoll
{
    private int die;
    private int minSaveRoll;
    private int roll;

    /**
     * Constructor.
     *
     * @param roll        Roll
     * @param die         Die
     * @param minSaveRoll Minimum roll required to pass
     */
    DeathSaveRoll(int roll, int die, int minSaveRoll)
    {
        this.die = die;
        this.minSaveRoll = minSaveRoll;
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
     * Get min save roll
     *
     * @return int
     */
    int getMinSaveRoll()
    {
        return minSaveRoll;
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
     * Did the target survive
     *
     * @return boolean
     */
    boolean survived()
    {
        return roll >= minSaveRoll;
    }
}
