package bot.Battle;

public class DeathSaveRoll
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
    public int getDie()
    {
        return die;
    }

    /**
     * Get min save roll
     *
     * @return int
     */
    public int getMinSaveRoll()
    {
        return minSaveRoll;
    }

    /**
     * Get roll
     *
     * @return int
     */
    public int getRoll()
    {
        return roll;
    }

    /**
     * Did the target survive
     *
     * @return boolean
     */
    public boolean survived()
    {
        return roll >= minSaveRoll;
    }
}
