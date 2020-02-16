package bot.Encounter.EncounteredCreature;

class DeathSaveRoll
{
    private int die;
    private int minPassRoll;
    private int roll;

    /**
     * Constructor.
     *
     * @param roll        Roll
     * @param die         Die
     * @param minPassRoll Minimum roll required to pass
     */
    DeathSaveRoll(int roll, int die, int minPassRoll)
    {
        this.die = die;
        this.minPassRoll = minPassRoll;
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
     * Did the target survive
     *
     * @return boolean
     */
    boolean survived()
    {
        return roll >= minPassRoll;
    }
}
