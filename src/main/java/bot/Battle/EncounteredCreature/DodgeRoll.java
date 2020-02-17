package bot.Battle.EncounteredCreature;

class DodgeRoll
{
    static int DODGE_ROLL_PASS = 10;

    private int roll;

    /**
     * DodgeRoll constructor
     *
     * @param roll Roll
     */
    DodgeRoll(int roll)
    {
        this.roll = roll;
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
     * Is failed dodge
     *
     * @return boolean
     */
    boolean isFail()
    {
        return roll < DODGE_ROLL_PASS;
    }
}
