package bot.Battle.EncounteredCreature;

public class DamageRoll
{
    private int damageDealt;
    private int damageResisted;
    private int die;
    private int roll;

    /**
     * Constructor.
     *
     * @param die            Damage die rolled by attacker
     * @param roll           Damage rolled by attacker
     * @param damageDealt    Damage dealt to target
     * @param damageResisted Damage resisted by target
     */
    DamageRoll(int die, int roll, int damageDealt, int damageResisted)
    {
        this.damageDealt = damageDealt;
        this.damageResisted = damageResisted;
        this.die = die;
        this.roll = roll;
    }

    /**
     * Get damage dealt
     *
     * @return int
     */
    public int getDamageDealt()
    {
        return damageDealt;
    }

    /**
     * Get damage resisted
     *
     * @return int
     */
    public int getDamageResisted()
    {
        return damageResisted;
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
     * Get roll
     *
     * @return int
     */
    public int getRoll()
    {
        return roll;
    }
}
