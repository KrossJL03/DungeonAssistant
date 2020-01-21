package bot.Battle.EncounteredCreature;

class HitRoll {

    private static int HIT_ROLL_MISS = 5;
    private static int HIT_ROLL_FAIL = 1;

    private int minCrit;
    private int roll;

    /**
     * HitRoll constructor
     *
     * @param roll    Roll
     * @param minCrit Min crit
     */
    HitRoll(int roll, int minCrit) {
        this.minCrit = minCrit;
        this.roll = roll;
    }

    /**
     * Get roll
     *
     * @return int
     */
    int getRoll() {
        return this.roll;
    }

    /**
     * Is roll a crit
     *
     * @return boolean
     */
    boolean isCrit() {
        return this.roll >= this.minCrit;
    }

    /**
     * Is roll a fail
     *
     * @return boolean
     */
    boolean isFail() {
        return this.roll <= HIT_ROLL_FAIL;
    }

    /**
     * Is roll a hit
     *
     * @return boolean
     */
    boolean isHit() {
        return this.roll > HIT_ROLL_MISS;
    }
}
